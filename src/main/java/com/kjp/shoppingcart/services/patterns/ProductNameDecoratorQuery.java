package com.kjp.shoppingcart.services.patterns;

public class ProductNameDecoratorQuery extends ProductDecoratorQuery {

    Query query;

    public ProductNameDecoratorQuery(IProductQuery productQuery) {
        super(productQuery);
        query = productQuery.getQuery();
    }

    @Override
    public Query getQuery() {
        return new Query.QueryBuilder().table(query.getTable())
                .fields(query.getFields())
                .condition(" WHERE name = :name ")
                .join(query.getJoin())
                .orderBy(query.getOrderBy())
                .build();
    }

}
