package com.testrag.store;

import com.testrag.model.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple in-memory vector store for document embeddings
 */
public class VectorStore {
    private final List<Document> documents;

    public VectorStore() {
        this.documents = new ArrayList<>();
    }

    /**
     * Add a document to the store
     */
    public void addDocument(Document document) {
        documents.add(document);
    }

    /**
     * Search for similar documents using cosine similarity
     */
    public List<Document> search(List<Double> queryEmbedding, int topK) {
        if (documents.isEmpty()) {
            return new ArrayList<>();
        }

        // Calculate similarity scores for all documents
        return documents.stream()
                .map(doc -> {
                    double similarity = cosineSimilarity(queryEmbedding, doc.getEmbedding());
                    doc.setSimilarityScore(similarity);
                    return doc;
                })
                .sorted((d1, d2) -> Double.compare(d2.getSimilarityScore(), d1.getSimilarityScore()))
                .limit(topK)
                .collect(Collectors.toList());
    }

    /**
     * Calculate cosine similarity between two vectors
     */
    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        if (v1.size() != v2.size()) {
            throw new IllegalArgumentException("Vectors must have the same dimension");
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < v1.size(); i++) {
            dotProduct += v1.get(i) * v2.get(i);
            norm1 += v1.get(i) * v1.get(i);
            norm2 += v2.get(i) * v2.get(i);
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * Get all documents
     */
    public List<Document> getAllDocuments() {
        return new ArrayList<>(documents);
    }

    /**
     * Get document count
     */
    public int size() {
        return documents.size();
    }

    /**
     * Clear all documents
     */
    public void clear() {
        documents.clear();
    }
}
