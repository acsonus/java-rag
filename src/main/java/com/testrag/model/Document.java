package com.testrag.model;

import java.util.List;
import java.util.UUID;

/**
 * Represents a document with its embedding
 */
public class Document {
    private final String id;
    private final String content;
    private final List<Double> embedding;
    private double similarityScore;

    public Document(String content, List<Double> embedding) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.embedding = embedding;
        this.similarityScore = 0.0;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", content='" + content.substring(0, Math.min(50, content.length())) + "...'" +
                ", similarityScore=" + String.format("%.4f", similarityScore) +
                '}';
    }
}
