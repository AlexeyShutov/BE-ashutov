package com.scloud.product.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.scloud.product.exception.ProductNotFoundException;
import com.scloud.exception.ServiceUnavailableException;
import com.scloud.product.model.Product;
import com.scloud.product.model.ProductAvailability;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    @Value("${services.catalog-product-url}")
    private String catalogIdUrl;
    @Value("${services.catalog-product-sku-url}")
    private String catalogSkuUrl;
    @Value("${services.inventory-availability-url}")
    private String inventoryUrl;
    @Value("${services.heavy-product-url}")
    private String heavyUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Product> getAvailableProductsByIds(String ids) {
        log.info("Checking availability of the following products: {}", ids);
        ResponseEntity<ProductAvailability[]> availabilityResponse;
        try {
            availabilityResponse = restTemplate.getForEntity(inventoryUrl, ProductAvailability[].class, ids);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode())
                throw new ProductNotFoundException("Product id not found: " + ids, e);

            throw new IllegalStateException(e);
        }

        String availableIds = getAvailableProductIds(availabilityResponse);
        if (availableIds.isEmpty())
            return List.of();

        log.info("Fetching available products: {}", availableIds);
        var productResponse = restTemplate.getForEntity(catalogIdUrl, Product[].class, availableIds);
        return responseArrayToList(productResponse);
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "20000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "80")
    })
    public List<Product> getHeavyProductsByIds(String ids, Long delay) {
        log.info("Trying to fetch heavy products: {}", ids);
        ResponseEntity<Product[]> productResponse;
        try {
            productResponse = restTemplate.getForEntity(heavyUrl, Product[].class, ids, delay);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode())
                throw new ProductNotFoundException("Product id not found: " + ids, e);

            throw new IllegalStateException(e);
        }

        return responseArrayToList(productResponse);
    }

    public List<Product> getAvailableProductsBySku(String sku) {
        log.info("Getting the products by sku: {}", sku);
        ResponseEntity<Product[]> productsResponse;
        try {
            productsResponse = restTemplate.getForEntity(catalogSkuUrl, Product[].class, sku);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode())
                throw new ProductNotFoundException("Product sku not found: " + sku, e);

            throw new IllegalStateException(e);
        }

        List<Product> products = responseArrayToList(productsResponse);

        String productIds = products.stream()
                .map(Product::getUniqId)
                .collect(Collectors.joining(","));

        var availabilityResponse = restTemplate.getForEntity(inventoryUrl, ProductAvailability[].class, productIds);
        String availableProductIds = getAvailableProductIds(availabilityResponse);
        log.info("The available products are: {}", availableProductIds);

        return products.stream()
                .filter(product -> availableProductIds.contains(product.getUniqId()))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    @HystrixCommand(ignoreExceptions = ServiceUnavailableException.class)
    private List<Product> fallbackServiceTimedOutException(String ids, Long delay) {
        throw new ServiceUnavailableException("Service unavailable");
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
