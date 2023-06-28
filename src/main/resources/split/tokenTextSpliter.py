import sys
import json
from langchain.text_splitter import TokenTextSplitter

def splitText(text):
    text_splitter = TokenTextSplitter(chunk_size=200)
    texts = text_splitter.split_text(text)
    return texts

if __name__ == '__main__':
    text = sys.argv[1]
    docs = splitText(text)
    print(json.dumps(docs, default=lambda docs: docs.__dict__))