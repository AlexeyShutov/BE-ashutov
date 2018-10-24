package com.scloud.product.controller;

import com.scloud.product.model.Product;
import com.scloud.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
