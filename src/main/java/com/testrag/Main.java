package com.testrag;

import com.testrag.service.RAGService;
import com.testrag.client.OllamaClient;
import com.testrag.store.VectorStore;

import java.util.Scanner;

/**
 * Main application class with console interface for RAG system
 */
public class Main {
    private static final String OLLAMA_URL = "http://localhost:11434";
    private static final String DEFAULT_MODEL = "llama2";

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("  TestRAG - RAG with Ollama");
        System.out.println("=================================\n");

        // Initialize components
        OllamaClient ollamaClient = new OllamaClient(OLLAMA_URL, DEFAULT_MODEL);
        VectorStore vectorStore = new VectorStore();
        RAGService ragService = new RAGService(ollamaClient, vectorStore);

        // Check Ollama connection
        if (!ollamaClient.isAvailable()) {
            System.err.println("Error: Cannot connect to Ollama at " + OLLAMA_URL);
            System.err.println("Please ensure Ollama is running: ollama serve");
            System.exit(1);
        }

        System.out.println("Connected to Ollama successfully!");
        System.out.println("Using model: " + DEFAULT_MODEL);
        System.out.println();

        // Start console interface
        runConsoleInterface(ragService);
    }

    private static void runConsoleInterface(RAGService ragService) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        printHelp();

        while (running) {
            System.out.print("\nTestRAG> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split("\\s+", 2);
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "add":
                        if (parts.length < 2) {
                            System.out.println("Usage: add <text>");
                            break;
                        }
                        ragService.addDocument(parts[1]);
                        System.out.println("Document added successfully!");
                        break;

                    case "query":
                    case "q":
                        if (parts.length < 2) {
                            System.out.println("Usage: query <question>");
                            break;
                        }
                        System.out.println("\nSearching knowledge base and generating answer...\n");
                        String answer = ragService.query(parts[1]);
                        System.out.println("Answer: " + answer);
                        break;

                    case "list":
                        int count = ragService.getDocumentCount();
                        System.out.println("Total documents in knowledge base: " + count);
                        break;

                    case "clear":
                        ragService.clearDocuments();
                        System.out.println("Knowledge base cleared!");
                        break;

                    case "help":
                    case "h":
                        printHelp();
                        break;

                    case "exit":
                    case "quit":
                    case "q":
                        System.out.println("Goodbye!");
                        running = false;
                        break;

                    default:
                        System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void printHelp() {
        System.out.println("\nAvailable Commands:");
        System.out.println("  add <text>      - Add a document to the knowledge base");
        System.out.println("  query <question> - Ask a question (RAG-powered answer)");
        System.out.println("  list            - Show number of documents in knowledge base");
        System.out.println("  clear           - Clear all documents from knowledge base");
        System.out.println("  help            - Show this help message");
        System.out.println("  exit            - Exit the application");
    }
}
