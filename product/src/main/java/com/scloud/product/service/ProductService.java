package com.scloud.product.service;

import com.scloud.product.exception.ProductNotFoundException;
import com.scloud.product.model.Product;
import com.scloud.product.model.ProductAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final RestTemplate restTemplate;
    private static final String CATALOG_ID_URL = "http://localhost:8080/catalog/product/{ids}";
    private static final String CATALOG_SKU_URL = "http://localhost:8080/catalog/product/sku/{sku}";
    private static final String INVENTORY_URL = "http://localhost:8081/inventory/availability/{ids}";

    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Product> getAvailableProductsByIds(String ids) {
        ResponseEntity<ProductAvailability[]> availabilityResponse;
        try {
            availabilityResponse = restTemplate.getForEntity(INVENTORY_URL, ProductAvailability[].class, ids);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode())
                throw new ProductNotFoundException("Product id not found: " + ids, e);

            throw new IllegalStateException(e);
        }

        String availableIds = getAvailableProductIds(availabilityResponse);
        if (availableIds.isEmpty())
            return List.of();

        var productResponse = restTemplate.getForEntity(CATALOG_ID_URL, Product[].class, availableIds);
        return responseArrayToList(productResponse);
    }

    public List<Product> getAvailableProductsBySku(String sku) {
        ResponseEntity<Product[]> productsResponse;

        try {
            productsResponse = restTemplate.getForEntity(CATALOG_SKU_URL, Product[].class, sku);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode())
                throw new ProductNotFoundException("Product sku not found: " + sku, e);

            throw new IllegalStateException(e);
        }

        List<Product> products = responseArrayToList(productsResponse);

        String productIds = products.stream()
                .map(Product::getUniqId)
                .collect(Collectors.joining(","));

        var availabilityResponse = restTemplate.getForEntity(INVENTORY_URL, ProductAvailability[].class, productIds);
        String availableProductIds = getAvailableProductIds(availabilityResponse);

       return products.stream()
                .filter(product -> availableProductIds.contains(product.getUniqId()))
                .collect(Collectors.toList());
    }

    private <T> List<T> responseArrayToList(ResponseEntity<T[]> responseArray) {
        return Optional.ofNullable(responseArray)
                .map(ResponseEntity::getBody)
                .map(List::of)
                .orElse(List.of());
    }

    private String getAvailableProductIds(ResponseEntity<ProductAvailability[]> availabilityResponse) {
        return responseArrayToList(availabilityResponse).stream()
                .filter(ProductAvailability::isAvailable)
                .map(ProductAvailability::getUniqId)
                .collect(Collectors.joining(","));
    }
}
