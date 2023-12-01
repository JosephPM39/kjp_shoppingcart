package com.kjp.shoppingcart.services.patterns.product_search_strategy;

import com.kjp.shoppingcart.dto.PaginateProductDTO;
import com.kjp.shoppingcart.entities.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SearchProductByName implements ISearchStrategy<PaginateProductDTO> {

    private EntityManager entityManager;

    public SearchProductByName(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public PaginateProductDTO search(String value, Pagination pagination, OrderEnum order) {
        String baseQuery ="FROM products p WHERE name = :name ";
        TypedQuery<ProductEntity> query = entityManager.createQuery("SELECT p ".concat(baseQuery), ProductEntity.class);
        query.setParameter("name", value);
        query.setMaxResults(pagination.limit());
        query.setFirstResult(pagination.offset());
        List<ProductEntity> result = query.getResultList();

        TypedQuery<Long> query1 = entityManager.createQuery("SELECT count(p) ".concat(baseQuery), Long.class);
        query1.setParameter("name", value);
        long total = query1.getSingleResult();

        Pagination finalPagination = new Pagination(pagination.limit(), pagination.offset(), total);

        return PaginateProductDTO.builder().products(result).pagination(finalPagination).orderEnum(order).build();
    }

    private String getOrderQuery(OrderEnum order, String table) {
        String orderQuery = "ORDER BY ";
        if (OrderEnum.FIRST_UPDATE == order) {
            return orderQuery.concat("updated_at ASC");
        }
        if (OrderEnum.LAST_UPDATE == order) {
            return orderQuery.concat("updated_at DESC");
        }
        if (OrderEnum.FIRST_CREATE == order) {
            return orderQuery.concat("created_at ASC");
        }
        return orderQuery.concat("created_at DESC");
    }

}
