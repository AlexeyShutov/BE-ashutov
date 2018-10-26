package com.scloud.catalog.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.scloud.catalog.model.ProductData;
import com.scloud.catalog.repository.ProductDataRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class ProductDataService {

    private final ProductDataRepository repository;

    private Logger log = LoggerFactory.getLogger(ProductDataService.class);

    @Autowired
    public ProductDataService(final ProductDataRepository repository) {
        this.repository = repository;
    }

    public List<ProductData> getAllById(String ids) {
        Preconditions.checkArgument(!StringUtils.isBlank(ids), "Empty ID not allowed");
        return getAllById(List.of(ids.split(",")));
    }

    private List<ProductData> getAllById(Collection<String> ids) {
        log.info("Getting product data by ids: {}", ids);
        Iterable<ProductData> productsData = repository.findAllById(ids);
        return Lists.newArrayList(productsData);
    }

    public List<ProductData> getAllBySku(String sku) {
        log.info("Getting product data by sku: {}", sku);
        return repository.findBySku(sku).orElse(emptyList());
    }

    public void saveAll(Collection<ProductData> productData) {
        repository.saveAll(Optional.ofNullable(productData).orElse(emptyList()));
    }

}
