package com.stackdev.springnative.repositories;

import com.stackdev.springnative.models.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
