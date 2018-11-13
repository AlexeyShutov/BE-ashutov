package com.scloud.product;

import com.scloud.product.model.Product;
import com.scloud.product.model.ProductAvailability;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

// FIXME product service test only for docker profile
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIT {

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String IDS = "b6c0b6bea69c722939585baeac73c13d,93e5272c51d8cce02597e3ce67b7ad0a," +
            "013e320f2f2ec0cf5b3ff5418d688528,505e6633d81f2cb7400c0cfa0394c427,d969a8542122e1331e304b09f81a83f6";
    private static final String SKU = "pp5006380337";

    private URL inventoryUrl;
    private URL productUrl;
    private URL skuUrl;
    private URL heavyUrl;

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws MalformedURLException {
        inventoryUrl = new URL(LOCAL_HOST + 8082 + "/inventory/availability/{ids}");
        productUrl = new URL(LOCAL_HOST + port + "/product/{ids}");
        skuUrl = new URL(LOCAL_HOST + port + "/product/sku/{sku}");
        heavyUrl = new URL(LOCAL_HOST + port + "/product/heavy/{ids}?delay={sec}");
    }

    @Test
    public void getAvailableProductsByIdsTest() {
        var productAvailabilities = asList(restTemplate.getForObject(inventoryUrl.toString(), ProductAvailability[].class, IDS));
        List<String> availableIds = getAvailableIds(productAvailabilities);

        var products = List.of(restTemplate.getForObject(productUrl.toString(), Product[].class, IDS, 5));
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
        var products = List.of(restTemplate.getForObject(skuUrl.toString(), Product[].class, SKU));

        List<@NotNull String> productIds = products.stream()
                .map(Product::getUniqId)
                .collect(Collectors.toList());

        var productAvailabilities = asList(restTemplate.getForObject(inventoryUrl.toString(), ProductAvailability[].class,
                String.join(",", productIds)));
        List<String> availableIds = getAvailableIds(productAvailabilities);

        assertTrue(productIds.containsAll(availableIds));
        assertTrue(availableIds.containsAll(productIds));
    }

    @Test
    public void shouldBreakCircuitAfter5AttemptsTest() {
        for (int i = 0; i < 5; i++) {
            var responseEntity = restTemplate.getForEntity(heavyUrl.toString(), RuntimeException.class, IDS, 5);
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody());
            assertNotNull("Service timed-out", responseEntity.getBody().getMessage());
        }

        var responseEntity = restTemplate.getForEntity(heavyUrl.toString(), RuntimeException.class, IDS, 5);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull("The error threshold crossed, service is temporary down", responseEntity.getBody().getMessage());
    }

    @Test
    public void shouldNotBreakCircuitIfSummaryTimeExceeds20s() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            var responseEntity = restTemplate.getForEntity(heavyUrl.toString(), RuntimeException.class, IDS, 5);
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody());
            assertNotNull("Service timed-out", responseEntity.getBody().getMessage());
            Thread.sleep(4000);
        }
    }

    private List<String> getAvailableIds(List<ProductAvailability> productAvailabilities) {
        return productAvailabilities.stream()
                .filter(ProductAvailability::isAvailable)
                .map(ProductAvailability::getUniqId)
                .collect(Collectors.toList());
    }
}
