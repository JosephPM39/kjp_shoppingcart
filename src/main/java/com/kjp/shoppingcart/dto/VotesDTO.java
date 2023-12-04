package com.kjp.shoppingcart.dto;

import java.util.UUID;

public record VotesDTO(UUID productId, Integer likes, Integer dislikes) {}
