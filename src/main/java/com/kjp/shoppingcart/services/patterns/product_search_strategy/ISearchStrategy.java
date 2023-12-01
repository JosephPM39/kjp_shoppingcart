package com.kjp.shoppingcart.services.patterns.product_search_strategy;

public interface ISearchStrategy<T> {
    public T search(String value, Pagination pagination, OrderEnum order);

}
