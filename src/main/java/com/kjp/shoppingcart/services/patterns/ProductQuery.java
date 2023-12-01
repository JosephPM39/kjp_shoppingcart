package com.kjp.shoppingcart.services.patterns;

public class ProductQuery implements IProductQuery {

    @Override
    public Query getQuery() {
        return new Query.QueryBuilder()
                .table("FROM products ")
                .fields("SELECT * ")
                .build();
    }
}
