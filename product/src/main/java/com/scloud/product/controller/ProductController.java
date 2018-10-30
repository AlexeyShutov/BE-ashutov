package com.scloud.product.controller;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.scloud.exception.ServiceUnavailableException;
import com.scloud.product.model.Product;
import com.scloud.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{ids}")
    public List<Product> getAllByIds(@PathVariable String ids) {
        return productService.getAvailableProductsByIds(ids);
    }

    @GetMapping("/sku/{sku}")
    public List<Product> getAllBySku(@PathVariable String sku) {
        return productService.getAvailableProductsBySku(sku);
    }

    @GetMapping("/heavy/{ids}")
    public List<Product> getAllHeavyById(@PathVariable String ids,
                                         @RequestParam Long delay) {
        try {
            return productService.getHeavyProductsByIds(ids, delay);
        } catch (HystrixRuntimeException e) {
            handleHystrixRuntimeException(e);
            return List.of();
        }
    }

    private void handleHystrixRuntimeException(HystrixRuntimeException e) {
        Throwable cause = e.getFallbackException().getCause();
        if (cause instanceof HystrixTimeoutException)
            throw new ServiceUnavailableException("Service timed-out");
        else if (cause instanceof RuntimeException)
            throw new ServiceUnavailableException("The error threshold crossed, service is temporary down");
        else
            throw new RuntimeException(cause);
    }
}
