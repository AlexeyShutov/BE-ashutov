package com.scloud.inventory.service;

import com.scloud.inventory.model.ProductAvailability;
import com.scloud.inventory.repository.ProductAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class ProductAvailabilityService {

    private final ProductAvailabilityRepository repository;

    @Autowired
    public ProductAvailabilityService(ProductAvailabilityRepository repository) {
        this.repository = repository;
    }

    public List<ProductAvailability> getAllById(Collection<String> ids) {
        return repository.getAllById(ids);
    }

    public void saveAll(Collection<ProductAvailability> productAvailabilities) {
        repository.saveAll(Optional.ofNullable(productAvailabilities).orElse(emptyList()));
    }

}
