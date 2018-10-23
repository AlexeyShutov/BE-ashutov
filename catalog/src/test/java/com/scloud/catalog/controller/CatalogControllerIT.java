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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CatalogControllerIT {

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String IDS =
            "b6c0b6bea69c722939585baeac73c13d,93e5272c51d8cce02597e3ce67b7ad0a,013e320f2f2ec0cf5b3ff5418d688528";
    private static final String SKU = "pp5006380337";

    private URL productUrl;
    private URL skuUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws MalformedURLException {
        productUrl = new URL(LOCAL_HOST + port + "/catalog/product/{ids}");
        skuUrl = new URL(LOCAL_HOST + port + "/catalog/sku/{sku}");
    }

    @Test
    public void getByIdTest() {
        var productsData = List.of(restTemplate.getForObject(productUrl.toString(), ProductData[].class, IDS));
        assertEquals(IDS.split(",").length, productsData.size());
        productsData.forEach(prodData -> assertEquals(SKU, prodData.getSku()));
    }

    @Test
    public void getBySkuTest() {
        var products = List.of(restTemplate.getForObject(skuUrl.toString(), ProductData[].class, SKU));
        assertFalse(products.isEmpty());
        for (ProductData productData : products) {
            assertEquals(SKU, productData.getSku());
        }
    }

    @Test
    public void getByNonExistingIdTest() {
        var responseEntity = restTemplate.getForEntity(productUrl.toString(), ProductData.class, "0");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getByNonExistingSkuTest() {
        var responseEntity = restTemplate.getForEntity(skuUrl.toString(), ProductData.class, "1");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
