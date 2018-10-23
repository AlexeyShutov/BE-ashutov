package com.scloud.catalog.repository;

import com.scloud.catalog.model.ProductData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDataRepository extends CrudRepository<ProductData, String> {

    Optional<List<ProductData>> findBySku(String sku);

}
