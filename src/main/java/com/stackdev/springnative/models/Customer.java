package com.stackdev.springnative.models;

import org.springframework.data.annotation.Id;

public record Customer(@Id Integer id, String firstName, String lastName, String email) {
}
