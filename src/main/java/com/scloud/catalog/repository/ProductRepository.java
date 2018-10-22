package com.scloud.catalog.repository;

import com.scloud.catalog.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {

    Optional<List<Product>> findBySku(String sku);
}
