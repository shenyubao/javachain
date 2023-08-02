# 🦜️ JavaChain

# 1. What is this?
JavaChain 用于快速搭建LLM应用。 JavaChain 参考了 LangChain 的架构设计，基于Java8实现。

# 2. QuickStart
## 2.1 引入POM依赖
获取最新的Jar包 [MAVEN仓库](https://s01.oss.sonatype.org/index.html#nexus-search;quick~javachain)

```xml
<dependency>
    <groupId>io.github.shenyubao</groupId>
    <artifactId>javachain</artifactId>
    <version>${javachain.latest.version}</version>
</dependency>
```

## 2.2 LLM
`LLM` 负责和大语言模型的直接交互，并屏蔽众多LLM供应商(OpenAI、Azure、ChatGLM、通义前问)之间的差异。

```java
OpenAI openAI = new OpenAI(endpoint,apiKey);
String result = openAI.predict("使用Java写一段代码，获取本机IP地址").outputString();
```
JavaChain同时提供流式内容返回
```java
OpenAI openAI = new OpenAI(endpoint,apiKey);
OpenAIConsoleStreamListener eventSourceListener = new OpenAIConsoleStreamListener();
openAI.streamPredict("使用Java写一段代码，获取本机IP地址", eventSourceListener);
```

## 2.3 Prompt
在LLM应用中通常需要根据模板渲染Prompt，`promptTemplate`用于渲染Prompt
```java
String template = "I want you to act as a naming consultant for new companies.\nWhat is a good name for a company that makes {product}?";
PromptTemplate promptTemplate = new PromptTemplate();
promptTemplate.setTemplate(template);
Map<String, Object> context = new HashMap<>();
ontext.put("product", "adidas");
String prompt = promptTemplate.format(context);
```
## 2.4 Chain
Chain是LangChain中最核心的组件，用于将各种LLM的动作合并起来，构建成LLM应用。

## 2.4.1 控制类 Chain
`SequentialChain` 用于合并多个Chain，并将其串行执行
```java
FakeChain fakeChain1 = new FakeChain("fakeChain1");
FakeChain fakeChain2 = new FakeChain("fakeChain2");
SequentialChain sequentialChain = new SequentialChain();
sequentialChain.setChains(Arrays.asList(fakeChain1,fakeChain2));
```

## 2.4.2 动作类 Chain
`ConversationChain` 用于合并历史对话，实现上下文对话
```java
List<BaseMessage> historyMessages = new ArrayList<>();
historyMessages.add(new HumanMessage("百灵AI都有哪些产品"));
historyMessages.add(new AIMessage("百灵AI有企业端和用户端，每个端都提供Chat工作台与浏览器插件"));

ConversationChain conversation = new ConversationChain();
conversation.setVerbose(true);
conversation.setMessages(historyMessages);

ChainContext context = new ChainContext();
context.setInput("使用英语描述");
ChainContext result = conversation.call(context);
```

`RetrievalChain` 用于对接外部数据库，检索私有内容
```
MilvusStore milvusStore = new MilvusStore(milvus_endpoint,milvus_apiKey);
milvusStore.setEmbedding(new OpenAIEmbeddings(endpoint, apiKey)); //openai提供的embeddings
milvusStore.init();

RetrievalChain retrievalChain = new RetrievalChain();
retrievalChain.setRetriever(milvusStore.asRetriever());
retrievalChain.setRecommendDocumentCount(2);
retrievalChain.setDatasetId("10001");
```

`StuffDocumentChain` 用于合并外部数据，可以与`RetrievalChain` 等组件组合实现私有知识库的对话
```java
StuffDocumentChain stuffDocumentChain = new StuffDocumentChain();

LLMChain llmChain = new LLMChain();
llmChain.setPrompt(PromptConstants.QA_CONVERSATION_CH);
llmChain.setLlm(new OpenAI(endpoint,apiKey));

SequentialChain sequentialChain = new SequentialChain();
sequentialChain.setChains(Arrays.asList(retrievalChain, stuffDocumentChain, llmChain));

String response = sequentialChain.call("百灵AI的浏览器插件是做什么的");
```

更多种类的Chain请查阅 User Guide

## 2.5 Data Connector
Data Connector 是 JavaChain中的次核心模块，包含多个组件，实现从外部读取文档，处理文档，将文档向量化，向量数据库读写的整个流程
![image](https://github.com/shenyubao/javachain/assets/1533087/943e5a0b-3874-4800-a51d-37f100925224)

## 2.5.1 Document Loader
JavaChain 直接复用了 LangChain 的 API，以复用LangChain社区丰富的Docuemnt Loader组件。对应的，部分Loader也是通过调用Python脚本来实现。
!!! 使用Loader前需要先安装 langchain, unstructured 等python组件（TBD）

```java
Docx2txtLoader loader = new Docx2txtLoader();
loader.setFilePath("path_to_docx");
List<Document> documents = loader.load();
```
现已支持的组件：Docx2txtLoader、PdfLoader、BSHTMLLoader、CSVLoader、GitLoader 等，详见 UserGuide

## 2.5.1 Document Transformers
`Transformers` 实现文档的格式化，例如将原始文档按自然段落拆分后，再根据设定的Token数合并成Chunk，以提高知识库内容使用效果
```java
PdfLoader loader = new PdfLoader();
loader.setFilePath(path);
loader.registerTransformer(new RecursiveCharacterTextSplitter());
List<Document> documents = loader.load("10001");
```

## 2.5.2  Embedding
`Embedding` 集成向量化引擎，实现文本的向量化
```java
MilvusStore milvusStore = new MilvusStore(milvus_endpoint,milvus_apiKey);
milvusStore.setEmbedding(new OpenAIEmbeddings(endpoint, apiKey)); //openai提供的embeddings
milvusStore.init();
```
现已支持的组件：OpenAIEmbedding、AzureOpenAIEmbdding 等，详见 UserGuide

## 2.5.3 Vector Stores
`Embedding` 实现向量数据库的集成与交互，实现文本的存取
```
MilvusStore milvusStore = new MilvusStore(milvus_endpoint,milvus_apiKey);
milvusStore.setEmbedding(new OpenAIEmbeddings(endpoint, apiKey)); //openai提供的embeddings
milvusStore.init();

List<String> knowledge = Arrays.asList(
  "百灵AI123开放平台创建机器人，可以参考：https://www.bailing.ai/knowledge/123",
  "openapi接口传数据是有大小限制在10M以内，这个是nginx限制的要求");
milvusStore.addTexts(knowledge,"10001");

//内容检索
int topK =3;
List<Document> documents = milvusStore.similaritySearch(datasetId,"OPENAPI有接口大小限制不，有的话是多少",topK);

```
现已支持的组件：MilvusStore，详见 UserGuide

## 2.5.4 Retriever
`Retriever` 用于实现内容检索，通常配合 `RetrievalChain` 使用，使用案例可查看 `RetrievalChain` 章节
现已支持的组件：VectorStoreRetriever，详见 UserGuide


# 3. Run the tests
`./gradlew testClasses`

# 4. ErrorCodes
详见 UserGuide

# 5. Roadmap
- [ ] 支持 Agent
- [ ] 支持 LLM: CHATGLM
- [ ] 支持 VectorStore: Aliyun OpenSearch
- [ ] 支持 VectorStore: Aliyun ADB for pg
- [ ] 错误码列表及处理建议

# 6. 用户社区
![image](https://github.com/shenyubao/javachain/assets/1533087/1ccb6b02-0ea7-4c61-84a4-4eaa6de1cb98)


