package com.example.sop_final_63070067_lnw;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{ productName: '?0' }")
    Product findByName(String productName);
}
