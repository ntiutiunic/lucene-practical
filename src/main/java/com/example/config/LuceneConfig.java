package com.example.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class LuceneConfig {
    @ConfigProperty(name = "lucene.index.path", defaultValue = "./index")
    String indexPath;

    public String getIndexPath() {
        return indexPath;
    }
} 