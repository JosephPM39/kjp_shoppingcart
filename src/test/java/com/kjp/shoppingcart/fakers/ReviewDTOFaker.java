package com.kjp.shoppingcart.fakers;

import com.github.javafaker.Faker;
import com.kjp.shoppingcart.dto.ReviewDTO;

import java.util.UUID;

public class ReviewDTOFaker {
    public static ReviewDTO getFake() {
        return ReviewDTO.builder()
                .title(Faker.instance().name().title())
                .description(Faker.instance().commerce().department())
                .productId(UUID.randomUUID())
                .build();
    }
}
