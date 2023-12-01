package com.kjp.shoppingcart.services.patterns.product_search_strategy;

public record Pagination(
        int limit,
        int offset,
        long total
) {
}
