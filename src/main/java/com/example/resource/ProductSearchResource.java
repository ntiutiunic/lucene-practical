package com.example.resource;

import com.example.model.Product;
import com.example.service.LuceneIndexer;
import com.example.service.ProductSearchService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductSearchResource {

    @Inject
    private ProductSearchService searchService;

    @Inject
    private LuceneIndexer indexer;

    @GET
    @Path("/search")
    public List<Product> search(
            @QueryParam("query") String query,
            @QueryParam("category") String category,
            @QueryParam("minPrice") Integer minPrice,
            @QueryParam("maxPrice") Integer maxPrice,
            @QueryParam("sortBy") String sortBy) throws IOException, org.apache.lucene.queryparser.classic.ParseException {
        return searchService.search(query, category, minPrice, maxPrice, sortBy);
    }

    @POST
    @Path("/index")
    public void indexProducts(@QueryParam("filePath") String filePath) throws IOException {
        indexer.indexProducts(filePath);
    }
} 