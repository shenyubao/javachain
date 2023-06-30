import sys
import json
from langchain.document_loaders import GitLoader

def load(repoPath, branch):
    loader = GitLoader(repo_path=repoPath, branch=branch)
    docs = loader.load()
    return docs

if __name__ == '__main__':
    repoPath = sys.argv[1]
    branch = sys.argv[2]
    docs = load(repoPath, branch)
    print(json.dumps(docs, default=lambda docs: docs.__dict__))