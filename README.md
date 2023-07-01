# 🦜️ JavaChain
基于 Java 快速创建 LLM 应用

# 1. What is this?
JavaChain 是参考 Langchain 架构的 Java 实现版，支持Java8。

# 2. QuickStart

# 3. User Guide
## 3.1 Model I/O
## 3.1.1 Prompt
使用示例
```java
String template = "I want you to act as a naming consultant for new companies.\nWhat is a good name for a company that makes {product}?";
PromptTemplate promptTemplate = new PromptTemplate();
promptTemplate.setTemplate(template);
Map<String, Object> context = new HashMap<>();
ontext.put("product", "adidas");
String prompt = promptTemplate.format(context);
```
## 3.1.2 LLM
使用示例：
```java
OpenAI openAI = new OpenAI(endpoint,apiKey);
String result = openAI.predict("使用Java写一段代码，获取本机IP地址").outputString();
```
已支持的组件
* OpenAI
* AzureOpenAI
## 3.2 Data Connector
![image](https://github.com/shenyubao/javachain/assets/1533087/943e5a0b-3874-4800-a51d-37f100925224)
## 3.2.1 Document Loader
使用示例
```java
String path = Objects.requireNonNull(getClass().getClassLoader().getResource("loader/docx2txt.docx")).getPath();
Docx2txtLoader loader = new Docx2txtLoader();
loader.setFilePath(path);
List<Document> documents = loader.load();
```
已支持的组件
* Docx2txtLoader
* PdfLoader
* BSHTMLLoader
* CSVLoader
* GitLoader
## 3.2.2 Document Transformers
使用示例
```java
String path = Objects.requireNonNull(getClass().getClassLoader().getResource("loader/layout-parser-paper.pdf")).getPath();
PdfLoader loader = new PdfLoader();
loader.setFilePath(path);
loader.registerTransformer(new RecursiveCharacterTextSplitter());
List<Document> documents = loader.load("10001");
```
已支持的组件
* CharacterTextSplitter
* RecursiveCharacterTextSplitter
* TextSplitter
* TokenTextSplitter

## 3.2.3 Embedding
## 3.2.4 Vector Stores
## 3.2.5 Retrievers
## 3.3 Chain
## 3.3.1 Basic Chain
## 3.3.2 Flow Chain


# 4. User Case
## 4.1 包含上下文的QA应用
## 4.2 基于知识库的QA应用

# 5. Run the tests

# 6. ErrorCodes

# 7. Roadmap


