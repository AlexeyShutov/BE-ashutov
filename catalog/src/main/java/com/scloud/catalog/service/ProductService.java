package com.scloud.catalog.service;

import com.google.common.base.Preconditions;
import com.scloud.catalog.model.Product;
import com.scloud.catalog.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductService(final ProductRepository repository) {
        this.repository = repository;
    }

    public Optional<Product> getById(String id) {
        Preconditions.checkArgument(!StringUtils.isBlank(id), "Empty ID not allowed");
        return repository.findById(id);
    }

    public List<Product> getAllBySku(String sku) {
        return repository.findBySku(sku).orElse(emptyList());
    }

    public void saveAll(Collection<Product> products) {
        repository.saveAll(Optional.ofNullable(products).orElse(emptyList()));
    }

}
