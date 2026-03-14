import io
import re
import urllib.parse
import numpy as np
import requests
import urllib3
import json
from fastapi import FastAPI
from pydantic import BaseModel
from bs4 import BeautifulSoup
from docx import Document
import fitz  
from ddgs import DDGS
import os
from dotenv import load_dotenv
from openai import OpenAI

app = FastAPI() 

base_dir = os.path.dirname(os.path.abspath(__file__))
env_path = os.path.join(base_dir, '.env')


if load_dotenv(dotenv_path=env_path):
    print(f"✅ Đã tìm thấy và tải file .env tại: {env_path}")
else:
    print(f"❌ KHÔNG tìm thấy file .env tại: {env_path}")

OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")


if not OPENAI_API_KEY:
    print("❌ LỖI: Biến OPENAI_API_KEY đang trống!")
else:
    print(f"🚀 Key đã sẵn sàng (4 ký tự đầu): {OPENAI_API_KEY[:4]}")


client = OpenAI(api_key=OPENAI_API_KEY)


class ThesisRequest(BaseModel):
    report_url: str
    
class TopicRelevanceRequest(BaseModel):        
    report_url: str
    topic_title: str
    
print("--- Hệ thống Plagiarism Hybrid PRO  - FPT University Edition ---")

def get_content_from_url(url: str, timeout=7):
    """Cào dữ liệu với timeout ngắn để tránh treo hệ thống"""
    try:
        parsed_url = urllib.parse.urlparse(url)
        safe_url = f"{parsed_url.scheme}://{parsed_url.netloc}{urllib.parse.quote(parsed_url.path)}"
        headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
        
        response = requests.get(safe_url, headers=headers, timeout=timeout, verify=False)
        response.raise_for_status()
        content = response.content

        if url.lower().endswith(".pdf"):
            with fitz.open(stream=io.BytesIO(content), filetype="pdf") as doc:
                return " ".join([page.get_text() for page in doc]).strip()
        elif url.lower().endswith(".docx"):
            doc = Document(io.BytesIO(content))
            return " ".join([p.text for p in doc.paragraphs]).strip()
        else:
            soup = BeautifulSoup(content, 'html.parser')
            for r in soup(["script", "style", "nav", "footer", "header", "aside"]): r.decompose()
            return " ".join([t.get_text().strip() for t in soup.find_all(['p', 'h1', 'h2', 'h3'])]).strip()
    except:
        return ""

def embed_texts_batch(texts):
    if not texts: return []
    try:
        resp = client.embeddings.create(model="text-embedding-3-small", input=texts)
        return [item.embedding for item in resp.data]
    except Exception as e:
        print(f"      [!] Lỗi API Embedding: {e}")
        return []

def split_into_sentences(text: str, min_words: int = 15):
    raw = re.split(r'(?<=[\.!?])\s+', text)
    sentences, buffer = [], ""
    for s in raw:
        s = s.strip()
        if not s: continue
        if len((buffer + " " + s).split()) < min_words:
            buffer = (buffer + " " + s).strip()
        else:
            if buffer: sentences.append(buffer)
            buffer = s
    if buffer: sentences.append(buffer)
    return sentences

@app.post("/check-plagiarism-auto")
async def check_plagiarism_auto(request: ThesisRequest):
    print(f"\n[BƯỚC 1] Nhận file: {request.report_url.split('/')[-1]}")
    thesis_text = get_content_from_url(request.report_url)
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
    thesis_sentences = split_into_sentences(thesis_text)
    thesis_vecs = np.array(embed_texts_batch(thesis_sentences))
    
    source_rankings = []
    for url in found_urls:
        src_text = get_content_from_url(url)
        if len(src_text) < 150: continue
        
        src_sentences = split_into_sentences(src_text)
        src_vecs = np.array(embed_texts_batch(src_sentences))
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
        response = client.chat.completions.create(
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

@app.post("/check-topic-relevance")
async def check_topic_relevance(request: TopicRelevanceRequest):
    print(f"\n[KIỂM TRA LẠC ĐỀ] Đang xử lý đề tài: {request.topic_title}")
    
    
    doc_text = get_content_from_url(request.report_url)
    if not doc_text or len(doc_text) < 100:
        return {"relevance_score": 0.0, "is_on_topic": False, "analysis": "Tài liệu trống hoặc quá ngắn."}

    
    content_to_analyze = doc_text[:2000]

    try:
        response = client.chat.completions.create(
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