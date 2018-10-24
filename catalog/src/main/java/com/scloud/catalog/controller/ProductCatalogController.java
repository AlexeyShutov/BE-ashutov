package com.scloud.catalog.controller;

import com.scloud.catalog.model.ProductData;
import com.scloud.catalog.service.ProductDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalog/product")
public class ProductCatalogController {

    private final ProductDataService service;

    @Autowired
    public ProductCatalogController(ProductDataService service) {
        this.service = service;
    }

    @GetMapping("/{ids}")
    public ResponseEntity<List<ProductData>> getAllByIds(@PathVariable String ids) {
        List<ProductData> products = service.getAllById(ids);
        return products.isEmpty() ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(products);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<List<ProductData>> getBySku(@PathVariable String sku) {
        List<ProductData> products = service.getAllBySku(sku);
        return products.isEmpty() ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(products);
    }

}
