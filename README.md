# Lucene Product Search

A high-performance product search engine built with Quarkus and Apache Lucene. This application demonstrates how to implement efficient full-text search capabilities with filtering and sorting options.

## Features

- Full-text search across product titles and descriptions
- Category-based filtering
- Price range filtering
- Support for both single and multiple categories per product
- Efficient indexing of large product catalogs
- RESTful API endpoints for search and indexing operations

## Technology Stack

- Java 17
- Quarkus 3.8.1
- Apache Lucene
- Maven
- RESTEasy Reactive

## Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.8.x or later

### Running the Application

1. Clone the repository:
```bash
git clone https://github.com/YOUR_USERNAME/lucene-product-search.git
cd lucene-product-search
```

2. Start the application in development mode:
```bash
mvn quarkus:dev
```

The application will be available at `http://localhost:8080`

### API Endpoints

#### Search Products
```
GET /api/products/search
```

Query Parameters:
- `query` - Search term (optional)
- `category` - Filter by category (optional)
- `minPrice` - Minimum price filter (optional)
- `maxPrice` - Maximum price filter (optional)
- `sortBy` - Sort field (optional)

Example:
```
GET /api/products/search?query=waffle&category=Kitchen%20Appliances&minPrice=50&maxPrice=100
```

#### Index Products
```
POST /api/products/index?filePath=path/to/products.json
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           ├── config/         # Configuration classes
│   │           ├── model/          # Data models
│   │           ├── resource/       # REST endpoints
│   │           └── service/        # Business logic
│   └── resources/
│       └── application.properties  # Application configuration
└── test/                          # Test classes
```

## Configuration

The application can be configured through `application.properties`:

```properties
# Lucene index path
lucene.index.path=target/lucene-index

# HTTP port
quarkus.http.port=8080
```
