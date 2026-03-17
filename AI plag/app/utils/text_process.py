import re
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

def chunk_text(text,chunk_size = 3000):
    words = text.split()
    return [" ".join(words[i:i+chunk_size]) for i in range(0,len(words),chunk_size)]