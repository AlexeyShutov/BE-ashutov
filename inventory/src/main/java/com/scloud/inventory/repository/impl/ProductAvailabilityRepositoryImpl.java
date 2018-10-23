package com.scloud.inventory.repository.impl;

import com.scloud.inventory.model.ProductAvailability;
import com.scloud.inventory.repository.ProductAvailabilityRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ProductAvailabilityRepositoryImpl implements ProductAvailabilityRepository {

    private static final Map<String, Boolean> idToStatusMap = new HashMap<>();

    public List<ProductAvailability> getAllById(Collection<String> ids) {
        return idToStatusMap.entrySet().stream()
                .filter(entry -> ids.contains(entry.getKey()))
                .map(entry -> new ProductAvailability(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public void saveAll(Collection<ProductAvailability> productAvailabilities) {
        productAvailabilities.forEach(productAvailability ->
                idToStatusMap.put(productAvailability.getUniqId(), productAvailability.isAvailable()));
    }

}
