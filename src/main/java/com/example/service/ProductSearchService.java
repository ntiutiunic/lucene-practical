package com.example.service;

import com.example.config.TestConfig;
import com.example.config.LuceneConfig;
import com.example.model.Product;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.search.MatchAllDocsQuery;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductSearchService {
    private static final String[] SEARCH_FIELDS = {"title", "description"};

    private final LuceneConfig luceneConfig;

    @Inject
    public ProductSearchService(LuceneConfig luceneConfig) {
        this.luceneConfig = luceneConfig;
    }

    public List<Product> search(String queryString, String category, Integer minPrice, Integer maxPrice, String sortBy) 
            throws IOException, ParseException {
        try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(luceneConfig.getIndexPath())))) {
            IndexSearcher searcher = new IndexSearcher(reader);
            
            // Create the main query
            Query query;
            if (queryString == null || queryString.trim().isEmpty()) {
                query = new MatchAllDocsQuery();
            } else {
                MultiFieldQueryParser queryParser = new MultiFieldQueryParser(SEARCH_FIELDS, new StandardAnalyzer());
                query = queryParser.parse(queryString);
            }
            
            // Create boolean query for filters
            BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
            booleanQuery.add(query, BooleanClause.Occur.MUST);
            
            // Add category filter if specified
            if (category != null && !category.isEmpty()) {
                Query categoryQuery = new TermQuery(new org.apache.lucene.index.Term("category", category));
                booleanQuery.add(categoryQuery, BooleanClause.Occur.MUST);
            }
            
            // Add price range filter if specified
            if (minPrice != null || maxPrice != null) {
                int min = minPrice != null ? minPrice : Integer.MIN_VALUE;
                int max = maxPrice != null ? maxPrice : Integer.MAX_VALUE;
                Query priceQuery = IntPoint.newRangeQuery("price", min, max);
                booleanQuery.add(priceQuery, BooleanClause.Occur.MUST);
            }
            
            // Create sort if specified
            Sort sort = Sort.RELEVANCE;
            if ("price".equals(sortBy)) {
                sort = new Sort(new SortField("price", SortField.Type.INT));
            }
            
            // Execute search
            TopDocs results = searcher.search(booleanQuery.build(), 100, sort);
            
            // Convert results to products
            List<Product> products = new ArrayList<>();
            for (ScoreDoc hit : results.scoreDocs) {
                Document doc = searcher.doc(hit.doc);
                Product product = new Product(
                    doc.get("id"),
                    doc.get("title"),
                    doc.get("description"),
                    doc.get("category"),
                    Integer.parseInt(doc.get("price"))
                );
                products.add(product);
            }
            
            return products;
        }
    }
} 