package com.stackdev.springnative.services;

import com.stackdev.springnative.models.Customer;
import com.stackdev.springnative.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    //Initialise Customer FirstNames to populate later
    @Bean
    ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener (){
        return new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(ApplicationReadyEvent event) {
                Flux.just("Daniel","Peter","Mary","Terryn")
                        .map(firstName -> new Customer(null,firstName,null,null))
                        .flatMap(customerRepository::save)
                        .subscribe(System.out::println);
            }
        };
    }


    public Flux<Customer> fetchAllCustomers(){
        return customerRepository.findAll();
    }
}
