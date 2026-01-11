package com.testrag.service;

import com.testrag.client.OllamaClient;
import com.testrag.model.Document;
import com.testrag.store.VectorStore;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RAG (Retrieval Augmented Generation) Service
 * Combines document retrieval with LLM generation
 */
public class RAGService {
    private final OllamaClient ollamaClient;
    private final VectorStore vectorStore;
    private static final int TOP_K_RESULTS = 3;

    public RAGService(OllamaClient ollamaClient, VectorStore vectorStore) {
        this.ollamaClient = ollamaClient;
        this.vectorStore = vectorStore;
    }

    /**
     * Add a document to the knowledge base
     */
    public void addDocument(String content) throws IOException {
        // Generate embedding for the document
        List<Double> embedding = ollamaClient.generateEmbedding(content);
        
        // Create and store document
        Document document = new Document(content, embedding);
        vectorStore.addDocument(document);
    }

    /**
     * Query the RAG system
     */
    public String query(String question) throws IOException {
        if (vectorStore.size() == 0) {
            return ollamaClient.generate(question);
        }

        // Generate embedding for the query
        List<Double> queryEmbedding = ollamaClient.generateEmbedding(question);

        // Retrieve relevant documents
        List<Document> relevantDocs = vectorStore.search(queryEmbedding, TOP_K_RESULTS);

        if (relevantDocs.isEmpty()) {
            return ollamaClient.generate(question);
        }

        // Build context from retrieved documents
        String context = buildContext(relevantDocs);

        // Generate answer using context
        return ollamaClient.generateWithContext(question, context);
    }

    /**
     * Build context string from retrieved documents
     */
    private String buildContext(List<Document> documents) {
        return documents.stream()
                .map(doc -> String.format("[Similarity: %.4f]\n%s", 
                    doc.getSimilarityScore(), doc.getContent()))
                .collect(Collectors.joining("\n\n---\n\n"));
    }

    /**
     * Get the number of documents in the knowledge base
     */
    public int getDocumentCount() {
        return vectorStore.size();
    }

    /**
     * Clear all documents from the knowledge base
     */
    public void clearDocuments() {
        vectorStore.clear();
    }

    /**
     * Get all documents
     */
    public List<Document> getAllDocuments() {
        return vectorStore.getAllDocuments();
    }
}
