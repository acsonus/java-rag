# TestRAG - Java RAG Implementation with Ollama

A Retrieval Augmented Generation (RAG) system implemented in Java, using Ollama for LLM inference and embeddings.

## Features

- 🤖 Integration with Ollama for LLM operations
- 📚 In-memory vector store for document embeddings
- 🔍 Semantic search using cosine similarity
- 💬 Interactive console interface
- 🎯 Context-aware answer generation

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- [Ollama](https://ollama.ai/) installed and running
- An Ollama model pulled (default: `llama2`)

## Installation

1. **Install Ollama** (if not already installed):
   ```bash
   # macOS/Linux
   curl https://ollama.ai/install.sh | sh
   
   # Or visit https://ollama.ai for other platforms
   ```

2. **Pull a model**:
   ```bash
   ollama pull llama2
   ```

3. **Start Ollama server**:
   ```bash
   ollama serve
   ```

4. **Build the project**:
   ```bash
   mvn clean package
   ```

## Usage

### Run the application

```bash
mvn exec:java -Dexec.mainClass="com.testrag.Main"
```

Or run the compiled JAR:

```bash
java -jar target/TestRAG-1.0-SNAPSHOT.jar
```

### Console Commands

Once the application starts, you can use the following commands:

- `add <text>` - Add a document to the knowledge base
- `query <question>` - Ask a question (uses RAG to find relevant context)
- `list` - Show the number of documents in the knowledge base
- `clear` - Clear all documents from the knowledge base
- `help` - Show available commands
- `exit` - Exit the application

### Example Session

```
TestRAG> add The capital of France is Paris. It is known for the Eiffel Tower.
Document added successfully!

TestRAG> add The capital of Japan is Tokyo. It is known for its technology and culture.
Document added successfully!

TestRAG> query What is the capital of France?
Searching knowledge base and generating answer...

Answer: Based on the context provided, the capital of France is Paris. It is also known for the Eiffel Tower.

TestRAG> list
Total documents in knowledge base: 2

TestRAG> exit
Goodbye!
```

## Architecture

### Components

- **Main**: Console interface and application entry point
- **OllamaClient**: HTTP client for Ollama API interactions
- **VectorStore**: In-memory storage for document embeddings with similarity search
- **RAGService**: Orchestrates retrieval and generation pipeline
- **Document**: Data model for documents with embeddings

### RAG Pipeline

1. **Document Indexing**: Text is converted to embeddings and stored
2. **Query Processing**: User question is converted to an embedding
3. **Retrieval**: Top-K most similar documents are retrieved using cosine similarity
4. **Generation**: LLM generates an answer using the retrieved context

## Configuration

Edit the constants in `Main.java` to customize:

```java
private static final String OLLAMA_URL = "http://localhost:11434";
private static final String DEFAULT_MODEL = "llama2";
```

To use a different model:
1. Pull the model: `ollama pull <model-name>`
2. Update `DEFAULT_MODEL` in the code

## Dependencies

- **OkHttp**: HTTP client for API calls
- **Gson**: JSON serialization/deserialization
- **Apache Commons Text**: Text processing utilities
- **SLF4J**: Logging framework

## Building

```bash
# Compile
mvn compile

# Run tests
mvn test

# Package
mvn package

# Clean build
mvn clean install
```

## Troubleshooting

**Cannot connect to Ollama**
- Ensure Ollama is running: `ollama serve`
- Check if the URL is correct (default: http://localhost:11434)

**Model not found**
- Pull the model: `ollama pull llama2`
- Or use a different model you have available

**Out of memory**
- Increase JVM heap size: `java -Xmx4g -jar target/TestRAG-1.0-SNAPSHOT.jar`

## License

MIT License
