package com.example.service;

import com.example.config.LuceneConfig;
import com.example.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import jakarta.inject.Inject;

@ApplicationScoped
public class LuceneIndexer {
    private static final Logger LOG = Logger.getLogger(LuceneIndexer.class);
    private final ObjectMapper objectMapper;
    private final LuceneConfig luceneConfig;

    @Inject
    public LuceneIndexer(LuceneConfig luceneConfig) {
        this.luceneConfig = luceneConfig;
        this.objectMapper = new ObjectMapper();
    }

    public void indexProducts(String jsonFilePath) throws IOException {
        LOG.info("Starting indexing process for file: " + jsonFilePath);
        
        File file = new File(jsonFilePath);
        if (!file.exists()) {
            LOG.error("File does not exist: " + jsonFilePath);
            throw new IOException("File not found: " + jsonFilePath);
        }
        
        LOG.info("File exists, size: " + file.length() + " bytes");
        
        List<Product> products = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                
                try {
                    Product product = objectMapper.readValue(line, Product.class);
                    products.add(product);
                } catch (Exception e) {
                    LOG.error("Error parsing line " + lineNumber + ": " + e.getMessage());
                    throw new IOException("Failed to parse JSON at line " + lineNumber + ": " + e.getMessage(), e);
                }
            }
        }
        
        LOG.info("Successfully read " + products.size() + " products from JSON");
        
        if (products.isEmpty()) {
            LOG.warn("No products found in the JSON file");
            return;
        }
        
        // Log first product as sample
        Product sample = products.get(0);
        LOG.info("Sample product: id=" + sample.getId() + ", title=" + sample.getTitle());
        
        String indexPath = luceneConfig.getIndexPath();
        LOG.info("Creating index directory at: " + indexPath);
        
        // Create index directory if it doesn't exist
        FSDirectory directory = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        
        try (IndexWriter writer = new IndexWriter(directory, config)) {
            LOG.info("Clearing existing index");
            writer.deleteAll();

            LOG.info("Indexing products");
            for (Product product : products) {
                // Получаем категорию: если есть поле category, используем его, иначе берем первую из categories
                String category = product.getCategory();
                if ((category == null || category.isEmpty()) && product.getCategories() != null && !product.getCategories().isEmpty()) {
                    category = product.getCategories().get(0);
                }
                // Check for required fields
                if (product.getId() == null || product.getTitle() == null || product.getDescription() == null || category == null) {
                    LOG.warn("Skipping product with missing required fields: id=" + product.getId() + ", title=" + product.getTitle());
                    continue;
                }
                Document doc = new Document();
                doc.add(new StringField("id", product.getId(), Field.Store.YES));
                doc.add(new TextField("title", product.getTitle(), Field.Store.YES));
                doc.add(new TextField("description", product.getDescription(), Field.Store.YES));
                doc.add(new StringField("category", category, Field.Store.YES));
                doc.add(new IntPoint("price", product.getPrice()));
                doc.add(new StoredField("price", product.getPrice()));
                doc.add(new NumericDocValuesField("price", product.getPrice()));
                writer.addDocument(doc);
            }

            LOG.info("Committing changes to index");
            writer.commit();
            LOG.info("Indexing completed successfully");
        } catch (Exception e) {
            LOG.error("Error during indexing: " + e.getMessage(), e);
            throw e;
        }
    }
} 