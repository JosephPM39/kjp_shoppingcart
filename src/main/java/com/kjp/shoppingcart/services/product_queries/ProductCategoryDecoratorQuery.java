package com.kjp.shoppingcart.services.product_queries;

public class ProductCategoryDecoratorQuery extends ProductDecoratorQuery {

    private Query query;

    public ProductCategoryDecoratorQuery(IProductQuery productQuery) {
        super(productQuery);
        query = productQuery.getQuery();
    }

    @Override
    public Query getQuery() {
        return new Query.QueryBuilder().table(query.getTable())
                .fields(query.getFields())
                .condition(query.getCondition())
                .join(query.getJoin())
                .orderBy(query.getOrderBy())
                .build();
    }
}
