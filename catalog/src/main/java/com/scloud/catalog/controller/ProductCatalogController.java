package com.scloud.catalog.controller;

import com.scloud.catalog.model.ProductData;
import com.scloud.catalog.service.ProductDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog/product")
public class ProductCatalogController {

    private Logger log = LoggerFactory.getLogger(ProductCatalogController.class);

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

    @GetMapping("/heavy/{ids}")
    public ResponseEntity<List<ProductData>> getAllByIdsWithDelay(@PathVariable String ids, @RequestParam Long delay) {
        try {
            Thread.sleep(delay * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Thread was interrupted", e);
        }

        List<ProductData> products = service.getAllById(ids);
        return products.isEmpty() ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(products);
    }

}
