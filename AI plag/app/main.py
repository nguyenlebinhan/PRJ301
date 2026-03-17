from dotenv import load_dotenv
import os
import sys

# 1. Nạp biến môi trường
load_dotenv()

# 2. Kiểm tra nhanh API Key trước khi khởi động FastAPI
if not os.getenv("OPENAI_API_KEY"):
    print("❌ LỖI: Không tìm thấy OPENAI_API_KEY trong file .env")
    # Có thể dừng chương trình nếu đây là thành phần bắt buộc
    # sys.exit(1) 

from fastapi import FastAPI
from app.api.v1.endpoints import router as v1_router

app = FastAPI(
    title="FPT Plagiarism Hybrid PRO",
    description="Hệ thống thẩm định đạo văn và hỗ trợ luận án - FPT University",
    version="1.0.0"
)

# 3. Kết nối Router
app.include_router(v1_router, prefix="/api/v1")

@app.get("/")
async def root():
    return {
        "status": "running",
        "system": "FPT Plagiarism Hybrid PRO",
        "version": "1.0.0"
    }