package com.scloud.catalog.controller;

import com.scloud.catalog.model.Product;
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
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerIT {

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
        Product product = restTemplate.getForObject(url.toString() + ID_1, Product.class);
        assertNotNull(product);
        assertEquals(ID_1, product.getUniqId());
        assertEquals(SKU, product.getSku());
        assertEquals("alfred dunner", product.getCategory());
    }

    @Test
    public void getBySkuTest() {
        @SuppressWarnings("unchecked")
        List<Product> products = Arrays.asList(restTemplate.getForObject(url.toString() + "sku/" + SKU, Product[].class));
        assertFalse(products.isEmpty());
        for (Product product : products) {
            assertEquals(SKU, product.getSku());
        }
    }

    @Test
    public void getByNonExistingIdTest() {
        ResponseEntity<Product> entity = restTemplate.getForEntity(url.toString() + "0", Product.class);
        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }

    @Test
    public void getByNonExistingSkuTest() {
        ResponseEntity<Product[]> entity = restTemplate.getForEntity(url.toString() + "sku/0", Product[].class);
        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }

}
