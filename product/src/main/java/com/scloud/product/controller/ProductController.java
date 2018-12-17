package com.scloud.product.controller;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.scloud.product.exception.handler.ExceptionHandler;
import com.scloud.product.model.Product;
import com.scloud.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public ProductController(ProductService productService, ExceptionHandler exceptionHandler) {
        this.productService = productService;
        this.exceptionHandler = exceptionHandler;
    }

    @GetMapping("/{ids}")
    public List<Product> getAllByIds(@PathVariable String ids) {
        try {
            return productService.getAvailableProductsByIds(ids);
        } catch (HystrixRuntimeException e) {
            throw new RuntimeException(e.getCause());
        }
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
            throw exceptionHandler.unwrap(e);
        }
    }
}
