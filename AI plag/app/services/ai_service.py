import numpy as np
import json
import os
from openai import OpenAI
import os
from openai import OpenAI
from dotenv import load_dotenv

load_dotenv() 

client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

def embed_texts_batch(texts):
    if not texts: return []
    try:
        resp = client.embeddings.create(model="text-embedding-3-small", input=texts)
        return [item.embedding for item in resp.data]
    except Exception as e:
        print(f"[!] Lỗi API Embedding: {e}")
        return []

def calculate_similarity(thesis_vecs, src_vecs):
    if thesis_vecs.size == 0 or src_vecs.size == 0:
        return 0, 0
    sim_matrix = np.dot(thesis_vecs, src_vecs.T)
    norms = np.linalg.norm(thesis_vecs, axis=1, keepdims=True) * np.linalg.norm(src_vecs, axis=1, keepdims=True).T
    sim_matrix = sim_matrix / (norms + 1e-9)
    
    max_sim = np.max(sim_matrix)
    coverage = np.mean(np.max(sim_matrix, axis=1) > 0.70)
    return max_sim, coverage

async def get_ai_judgment(system_prompt, user_content):
    try:
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_content}
            ],
            response_format={ "type": "json_object" }
        )
        return json.loads(response.choices[0].message.content)
    except Exception as e:
        print(f"[!] Lỗi GPT: {e}")
        return None