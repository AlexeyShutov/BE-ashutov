package com.scloud.inventory.controller;

import com.scloud.inventory.model.ProductAvailability;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductAvailabilityControllerIT {

    private static final String IDS =
            "b6c0b6bea69c722939585baeac73c13d,93e5272c51d8cce02597e3ce67b7ad0a,013e320f2f2ec0cf5b3ff5418d688528";

    private URL url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws MalformedURLException {
        url = new URL("http://localhost:" + port + "/inventory/availability/{ids}");
    }

    @Test
    public void getByIdTest() {
        var productAvailabilities = asList(restTemplate.getForObject(url.toString(), ProductAvailability[].class, IDS));
        assertEquals(IDS.split(",").length, productAvailabilities.size());
    }

    @Test
    public void getByNonExistingIdTest() {
        var responseEntity = restTemplate.getForEntity(url.toString(), ProductAvailability.class, "0");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
