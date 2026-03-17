import re
import json
from unittest import result
import numpy as np
from fastapi import APIRouter
from ddgs import DDGS

# Import từ các module bạn đã chia
from app.models.schemas import ThesisRequest, TopicRelevanceRequest, ImprovementRequest
from app.utils import scraper, text_process
from app.services import ai_service

router = APIRouter()

@router.post("/check-plagiarism-auto")
async def check_plagiarism_auto(request: ThesisRequest):
    print(f"\n[BƯỚC 1] Nhận file: {request.report_url.split('/')[-1]}")
    thesis_text = scraper.get_content_from_url(request.report_url)
    word_count = len(thesis_text.split())
    
    if word_count < 30:
        return {"similarity_score": 0.0, "status": "An toàn", "best_source": "N/A", "analysis": "Văn bản quá ngắn."}

    
    print(f"[BƯỚC 2] Đang tìm kiếm nguồn nghi vấn...")
    clean_words = re.sub(r'[^\w\s]', ' ', thesis_text).split()
    
    query = " ".join(clean_words[10:60]) 
    
    found_urls = []
    try:
        with DDGS() as ddgs:
           
            results = ddgs.text(query, region='vi-vnm', max_results=10) 
            found_urls = [r['href'] for r in results if "localhost" not in r['href']]
    except Exception as e:
        print(f"   [!] Lỗi DuckDuckGo: {e}")

    if not found_urls:
        return {"similarity_score": 0.0, "status": "An toàn", "best_source": "N/A"}

    
    print(f"[BƯỚC 3] Đang quét {len(found_urls)} website...")
    thesis_sentences = text_process.split_into_sentences(thesis_text)
    thesis_vecs = np.array(ai_service.embed_texts_batch(thesis_sentences))
    
    source_rankings = []
    for url in found_urls:
        src_text = scraper.get_content_from_url(url)
        if len(src_text) < 150: continue
        
        src_sentences = text_process.split_into_sentences(src_text)
        src_vecs = np.array(ai_service.embed_texts_batch(src_sentences))
        if src_vecs.size == 0: continue

        sim_matrix = np.dot(thesis_vecs, src_vecs.T)
        norms = np.linalg.norm(thesis_vecs, axis=1, keepdims=True) * np.linalg.norm(src_vecs, axis=1, keepdims=True).T
        sim_matrix = sim_matrix / (norms + 1e-9)

       
        max_sim = np.max(sim_matrix)
        coverage = np.mean(np.max(sim_matrix, axis=1) > 0.70) 
        
        source_rankings.append({"url": url, "score": max_sim, "coverage": coverage, "content": src_text})

    if not source_rankings:
        return {"similarity_score": 0.0, "status": "An toàn", "best_source": "N/A"}

    
    best_match = max(source_rankings, key=lambda x: x["coverage"])


    print(f"[BƯỚC 4] Sentence embedding models đang thẩm định ...")
    try:
        response = ai_service.client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content": "Bạn là chuyên gia bắt đạo văn khắt khe của FPT. Nhiệm vụ của bạn là tìm ra sự trùng lặp ý tưởng. Kể cả khi sinh viên thay đổi từ ngữ nhưng cấu trúc và ý chính giống nguồn, bạn vẫn phải cho điểm đạo văn cao."},
                {"role": "user", "content": f"""
                    HÃY SO SÁNH CHI TIẾT:
                    BÀI LÀM: {thesis_text[:1500]}
                    ---
                    NGUỒN NGHI VẤN: {best_match['content'][:1500]}
                    ---
                    YÊU CẦU: 
                    1. Nếu giống trên 60% ý tưởng, similarity_score phải >= 75.
                    2. Trả về JSON: {{"similarity_score": float, "status": "An toàn/Cần kiểm tra/Đạo văn", "reason": "Giải thích rõ câu nào bị trùng"}}
                """}
            ],
            response_format={ "type": "json_object" }
        )
        res = json.loads(response.choices[0].message.content)
        final_score = res.get('similarity_score', 0)
        ai_reason = res.get('reason', 'Không có lý do cụ thể.') 
        
        status = "An toàn"
        if final_score > 70: status = "Nguy cơ đạo văn cao"
        elif final_score > 30: status = "Cần kiểm tra lại"

        
        print("-" * 50)
        print(f"[AI REASONING]: {ai_reason}") 
        print(f"[KẾT QUẢ]: {final_score}% - {status}")
        print(f"[Link Nguồn]: {best_match['url']}")
        print("-" * 50)

        return {
            "similarity_score": final_score,
            "plagiarism_status": status,
            "best_source": best_match['url'],
            "plagiarism_analysis": ai_reason 
        }
    except Exception as e:
        print(f"   [!] Lỗi GPT: {e}")
        return {"similarity_score": round(best_match['score']*100, 2), "plagiarism_status": "Lỗi AI", "best_source": best_match['url']}

@router.post("/check-topic-relevance")
async def check_topic_relevance(request: TopicRelevanceRequest):
    print(f"\n[KIỂM TRA LẠC ĐỀ] Đang xử lý đề tài: {request.topic_title}")
    
    
    doc_text = scraper.get_content_from_url(request.report_url)
    if not doc_text or len(doc_text) < 100:
        return {"relevance_score": 0.0, "is_on_topic": False, "analysis": "Tài liệu trống hoặc quá ngắn."}

    
    content_to_analyze = doc_text[:2000]

    try:
        response = ai_service.client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {
                    "role": "system", 
                    "content": "Bạn là chuyên gia thẩm định đề tài đồ án tại FPT University. Nhiệm vụ của bạn là kiểm tra xem nội dung tài liệu có khớp với tên đề tài không."
                },
                {
                    "role": "user", 
                    "content": f"""
                        TÊN ĐỀ TÀI: {request.topic_title}
                        ---
                        NỘI DUNG TÀI LIỆU: {content_to_analyze}
                        ---
                        YÊU CẦU TRẢ VỀ JSON:
                        1. relevance_score: float (0-100).
                        2. is_on_topic: boolean (True nếu >= 60).
                        3. relevance_status: string. Hãy chọn 1 trong 3 trạng thái sau:
                           - "Đúng chủ đề": Nếu score >= 80.
                           - "Cần điều chỉnh": Nếu score từ 40 đến dưới 80.
                           - "Lạc đề": Nếu score < 40.
                        4. relevance_analysis: string (Giải thích ngắn gọn lý do tại sao chọn trạng thái đó).
                        
                        {{
                            "relevance_score": float,
                            "relevance_status": "string",
                            "relevance_analysis": "string"
                        }}
                    """
                }
            ],
            response_format={ "type": "json_object" }
        )
        
        result = json.loads(response.choices[0].message.content)
        
        print("-" * 50)
        print(f" ĐỀ TÀI: {request.topic_title}")
        print(f" ĐIỂM LIÊN QUAN: {result['relevance_score']}%")
        print(f" TRẠNG THÁI: {result['relevance_status']}")
        print(f" LÝ DO: {result['relevance_analysis']}")
        print("-" * 50)
        
        return result

    except Exception as e:
        print(f" [!] Lỗi AI Relevance: {e}")
        return {"relevance_score": 0.0, "is_on_topic": False, "relevance_analysis": "Lỗi hệ thống khi thẩm định."}    


def safe_text(text):
    if not text: return ""
    text = text.replace("{", "[").replace("}", "]").replace('"', "'")
    text = re.sub(r'[\x00-\x1f\x7f-\x9f]', '', text)
    return text.strip()
    
@router.post("/suggest-improvements")
async def suggest_improvements(request: ImprovementRequest):
    focus = request.focus_area if request.focus_area else "tổng thể"
    print(f"\n[ĐỀ XUẤT CẢI THIỆN] Đang xử lý khía cạnh: {focus}")
    
    
    doc_text = scraper.get_content_from_url(request.report_url)
    if not doc_text or len(doc_text) < 100:
        return {
            "focus_analysis": "N/A",
            "general_observations": "Tài liệu trống hoặc quá ngắn.",
            "top_3_priorities": ["Kiểm tra lại file upload"]
        }
    chunk_list = text_process.chunk_text(doc_text)
    print(f"[HỆ THỐNG] Đã chia luận án thành {len(chunk_list)} phần. Bắt đầu phân tích chuyên sâu...")
    
    try:
        partial_analyses = []
       
        for i, chunk in enumerate(chunk_list[:5]):
            print(f"   [+] Đang thẩm định phần {i+1}...")
            resp = ai_service.client.chat.completions.create(
                model="gpt-4o-mini",
                messages=[
                    {"role": "system", "content": f"Bạn là chuyên gia thẩm định luận án tại FPT. Hãy phân tích đoạn văn sau, tập trung vào: {focus}."},
                    {"role": "user", "content": chunk}
                ]
            )
            partial_analyses.append(resp.choices[0].message.content)

       
        print(f"[BƯỚC CUỐI] Mentor AI đang tổng hợp báo cáo cải thiện...")
        final_response = ai_service.client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {
                    "role": "system", 
                    "content": "Bạn là Mentor hướng dẫn đồ án. Dựa trên các phân tích chi tiết, hãy trả về kết quả JSON."
                },
                {
                    "role": "user", 
                    "content": f"""
                        YÊU CẦU TRỌNG TÂM: {focus}
                        DỮ LIỆU PHÂN TÍCH: {' '.join(partial_analyses)}
                        ---
                        YÊU CẦU TRẢ VỀ JSON:
                        1. focus_analysis: Nhận xét chi tiết về phần {focus}.
                        2. general_observations: Các nhận xét tổng quát khác về bài làm.
                        3. top_3_priorities: Danh sách (array) 3 việc cần làm ngay để cải thiện lỗi {focus}.

                        {{
                            "focus_analysis": "string",
                            "general_observations": "string",
                            "top_3_priorities": ["việc 1", "việc 2", "việc 3"]
                        }}
                    """
                }
            ],
            response_format={ "type": "json_object" }
        )
        
        result = json.loads(final_response.choices[0].message.content)

        
        print("-" * 50)
        print(f" YÊU CẦU TẬP TRUNG: {focus}")
        print(f" NHẬN XÉT: {result.get('focus_analysis')[:150]}...")
        print(f" ƯU TIÊN: {', '.join(result.get('top_3_priorities', []))}")
        print("-" * 50)
        
        return {
            "focus_analysis": result.get("focus_analysis", "Không có phân tích."),
            "general_observations": result.get("general_observations", "Không có nhận xét."),
            "top_3_priorities": result.get("top_3_priorities", ["Cần xem xét thêm"])
        }

    except Exception as e:
        print(f" [!] Lỗi AI Improvement: {e}")
        return {
            "focus_analysis": "Lỗi hệ thống khi phân tích chuyên sâu.",
            "general_observations": str(e),
            "top_3_priorities": ["Thử lại sau", "Liên hệ hỗ trợ kỹ thuật"]
        }