package com.scloud.inventory.repository;

import com.scloud.inventory.model.ProductAvailability;

import java.util.Collection;
import java.util.List;

public interface ProductAvailabilityRepository {

    List<ProductAvailability> getAllById(Collection<String> ids);

    void saveAll(Collection<ProductAvailability> productAvailabilities);

}
