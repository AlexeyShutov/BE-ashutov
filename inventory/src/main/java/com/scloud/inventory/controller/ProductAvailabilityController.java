package com.scloud.inventory.controller;

import com.scloud.inventory.model.ProductAvailability;
import com.scloud.inventory.service.ProductAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/inventory/availability")
public class ProductAvailabilityController {

    private final ProductAvailabilityService service;

    @Autowired
    public ProductAvailabilityController(ProductAvailabilityService service) {
        this.service = service;
    }

    @GetMapping("/{ids}")
    public ResponseEntity<List<ProductAvailability>> getAllById(@PathVariable String ids) {
        List<ProductAvailability> productAvailabilities = service.getAllById(Arrays.asList(ids.split(",")));
        return productAvailabilities.isEmpty() ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(productAvailabilities);
    }

}
