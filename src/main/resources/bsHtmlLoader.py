import sys
import json
from langchain.document_loaders import BSHTMLLoader

def load(filePath):
    loader = BSHTMLLoader(filePath)
    docs = loader.load()
    return docs

if __name__ == '__main__':
    input = sys.argv[1]
    docs = load(input)
    print(json.dumps(docs, default=lambda docs: docs.__dict__))