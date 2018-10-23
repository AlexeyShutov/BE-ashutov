package com.scloud.catalog.controller;

import com.scloud.catalog.model.ProductData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CatalogControllerIT {

    private static final String ID_1 = "b6c0b6bea69c722939585baeac73c13d";
    private static final String SKU = "pp5006380337";

    private URL url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws MalformedURLException {
        url = new URL("http://localhost:" + port + "/product/");
    }

    @Test
    public void getByIdTest() {
        ProductData productData = restTemplate.getForObject(url.toString() + ID_1, ProductData.class);
        assertNotNull(productData);
        assertEquals(ID_1, productData.getUniqId());
        assertEquals(SKU, productData.getSku());
        assertEquals("alfred dunner", productData.getCategory());
    }

    @Test
    public void getBySkuTest() {
        var products = Arrays.asList(restTemplate.getForObject(url.toString() + "sku/" + SKU, ProductData[].class));
        assertFalse(products.isEmpty());
        for (ProductData productData : products) {
            assertEquals(SKU, productData.getSku());
        }
    }

    @Test
    public void getByNonExistingIdTest() {
        ResponseEntity<ProductData> entity = restTemplate.getForEntity(url.toString() + "0", ProductData.class);
        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }

    @Test
    public void getByNonExistingSkuTest() {
        ResponseEntity<ProductData[]> entity = restTemplate.getForEntity(url.toString() + "sku/0", ProductData[].class);
        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }

}
