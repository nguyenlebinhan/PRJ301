import io
import requests
import urllib.parse
from bs4 import BeautifulSoup
from docx import Document
import fitz
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