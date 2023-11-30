package com.kjp.shoppingcart.services.product_queries;

public abstract class ProductDecoratorQuery implements IProductQuery {

    private final IProductQuery productQuery;

    public ProductDecoratorQuery(IProductQuery productQuery) {
        this.productQuery = productQuery;
    }

}