package com.scloud.product.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.scloud.product.exception.ProductNotFoundException;
import com.scloud.product.model.ProductAvailability;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ProductAvailabilityCommand extends HystrixCommand<ResponseEntity<ProductAvailability[]>> {

    private final RestTemplate restTemplate;
    private final String url;
    private final String ids;

    public ProductAvailabilityCommand(RestTemplate restTemplate, String url, String ids) {
        super(HystrixCommandGroupKey.Factory.asKey("ProductGroup"));
        this.restTemplate = restTemplate;
        this.url = url;
        this.ids = ids;
    }

    @Override
    protected ResponseEntity<ProductAvailability[]> run() {
        log.info("Checking availability of the following products: {}", ids);
        ResponseEntity<ProductAvailability[]> response;
        try {
            response = restTemplate.getForEntity(url, ProductAvailability[].class, ids);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode()))
                throw new ProductNotFoundException("Product id not found: " + ids);

            throw e;
        }
        return response;
    }
}
