from dotenv import load_dotenv
import os
import sys

load_dotenv()

if not os.getenv("OPENAI_API_KEY"):
    print("❌ LỖI: Không tìm thấy OPENAI_API_KEY trong file .env")
    sys.exit(1)

from fastapi import FastAPI
from app.api.v1.endpoints import router as v1_router

app = FastAPI(
    title="FPT Plagiarism Hybrid PRO",
    description="Hệ thống thẩm định đạo văn và hỗ trợ luận án - FPT University",
    version="1.0.0"
)

app.include_router(v1_router, prefix="/api/v1")

@app.get("/")
async def root():
    return {
        "status": "running",
        "system": "FPT Plagiarism Hybrid PRO",
        "version": "1.0.0"
    }