package com.scloud.product;

import com.scloud.product.model.Product;
import com.scloud.product.model.ProductAvailability;
import com.scloud.product.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    private static final String IDS = "b6c0b6bea69c722939585baeac73c13d,93e5272c51d8cce02597e3ce67b7ad0a," +
            "013e320f2f2ec0cf5b3ff5418d688528,505e6633d81f2cb7400c0cfa0394c427,d969a8542122e1331e304b09f81a83f6";
    private static final String AVAILABLE_IDS = "b6c0b6bea69c722939585baeac73c13d,93e5272c51d8cce02597e3ce67b7ad0a";
    private static final String SKU = "pp5006380337";
    private static final String INVENTORY_SERVICE = "inventory-service";
    private static final String CATALOG_SERVICE = "catalog-service";

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ProductService service;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(service, "inventoryUrl", INVENTORY_SERVICE);
        ReflectionTestUtils.setField(service, "catalogIdUrl", CATALOG_SERVICE);
        ReflectionTestUtils.setField(service, "catalogSkuUrl", CATALOG_SERVICE);
        ReflectionTestUtils.setField(service, "heavyUrl", CATALOG_SERVICE);
    }

    @Test
    public void getAvailableProductsByIdsTest() {
        when(restTemplate.getForEntity(INVENTORY_SERVICE, ProductAvailability[].class, IDS))
                .thenReturn(buildAvailableProductsResponse(AVAILABLE_IDS));
        when(restTemplate.getForEntity(CATALOG_SERVICE, Product[].class, AVAILABLE_IDS))
                .thenReturn(buildProductsResponse(AVAILABLE_IDS));

        var products = service.getAvailableProductsByIds(IDS);

        List<String> availableIds = List.of(AVAILABLE_IDS.split(","));
        assertFalse(products.isEmpty());
        assertEquals(availableIds.size(), products.size());

        List<@NotNull String> productIds = products.stream()
                .map(Product::getUniqId)
                .collect(Collectors.toList());

        assertTrue(productIds.containsAll(availableIds));
        assertTrue(availableIds.containsAll(productIds));
    }

    @Test
    public void getAvailableProductsBySkuTest() {
        when(restTemplate.getForEntity(CATALOG_SERVICE, Product[].class, SKU))
                .thenReturn(buildProductsResponse(IDS));
        when(restTemplate.getForEntity(INVENTORY_SERVICE, ProductAvailability[].class, IDS))
                .thenReturn(buildAvailableProductsResponse(AVAILABLE_IDS));

        var products = service.getAvailableProductsBySku(SKU);

        List<@NotNull String> productIds = products.stream()
                .map(Product::getUniqId)
                .collect(Collectors.toList());

        List<String> availableIds = List.of(AVAILABLE_IDS.split(","));

        assertTrue(productIds.containsAll(availableIds));
        assertTrue(availableIds.containsAll(productIds));
    }

    private ResponseEntity<ProductAvailability[]> buildAvailableProductsResponse(String ids) {
        ProductAvailability[] productAvailabilities = {};
        return ResponseEntity.ok().body(
                Arrays.stream(ids.split(","))
                        .map(id -> new ProductAvailability(id, true))
                        .collect(Collectors.toList()).toArray(productAvailabilities)
        );
    }

    private ResponseEntity<Product[]> buildProductsResponse(String ids) {
        Product[] products = {};
        return ResponseEntity.ok().body(Arrays.stream(ids.split(","))
                .map(id -> Product.builder().uniqId(id).build())
                .collect(Collectors.toList()).toArray(products));
    }
}
