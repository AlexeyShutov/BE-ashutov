package com.scloud.product.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.scloud.product.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ProductCatalogCommand extends HystrixCommand<ResponseEntity<Product[]>> {

    private final RestTemplate restTemplate;
    private final String url;
    private final String searchId;

    public ProductCatalogCommand(RestTemplate restTemplate, String url, String searchId) {
        super(HystrixCommandGroupKey.Factory.asKey("ProductGroup"));
        this.restTemplate = restTemplate;
        this.url = url;
        this.searchId = searchId;
    }

    @Override
    protected ResponseEntity<Product[]> run() {
        log.info("Requesting products from catalog by the [{}] criteria", searchId);
        return restTemplate.getForEntity(url, Product[].class, searchId);
    }
}
