package com.stackdev.springnative.controllers;

import com.stackdev.springnative.models.Customer;
import com.stackdev.springnative.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping(value = "/customers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> getAllCustomer(){
        return customerService.fetchAllCustomers();
    }
}
