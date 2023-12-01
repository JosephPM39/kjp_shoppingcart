package com.kjp.shoppingcart.dto;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.services.patterns.product_search_strategy.OrderEnum;
import com.kjp.shoppingcart.services.patterns.product_search_strategy.Pagination;
import lombok.Builder;

import java.util.List;

@Builder
public record PaginateProductDTO(
        Pagination pagination,
        OrderEnum orderEnum,
        List<ProductEntity> products
) {}
