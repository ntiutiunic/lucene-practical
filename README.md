# Lucene Product Search Application

This is a Java application that implements full-text search functionality for a product catalog using Apache Lucene and Quarkus.

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

## Сборка и запуск

Для запуска приложения в режиме разработки используйте:

```
mvn quarkus:dev
```

Для сборки обычного jar-файла:

```
mvn package
```

Готовый jar будет находиться в папке `target/`.

## API Endpoints

### Index Products
POST `/api/products/index?filePath=<path_to_json_file>`
- Indexes products from the specified JSON file
- Example: `curl -X POST "http://localhost:8080/api/products/index?filePath=conversational_demo_demoproducts_flattened%20(1).json"`

### Search Products
GET `/api/products/search`
Parameters:
- `q`: Search query (required)
- `category`: Filter by category (optional)
- `minPrice`: Minimum price filter (optional)
- `maxPrice`: Maximum price filter (optional)
- `sortBy`: Sort field (optional, use "price" to sort by price)

Example queries:
1. Basic search:
```
curl "http://localhost:8080/api/products/search?q=iphone"
```

2. Search with filters:
```
curl "http://localhost:8080/api/products/search?q=phone&category=Electronics&minPrice=100&maxPrice=1000&sortBy=price"
```

## Features

- Full-text search across product titles and descriptions
- Category filtering
- Price range filtering
- Price-based sorting
- RESTful API interface
- JSON response format

## Project Structure

- `src/main/java/com/example/model/Product.java`: Product model class
- `src/main/java/com/example/service/LuceneIndexer.java`: Service for indexing products
- `src/main/java/com/example/service/ProductSearchService.java`: Service for searching products
- `src/main/java/com/example/resource/ProductSearchResource.java`: REST API endpoints 