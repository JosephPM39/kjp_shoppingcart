package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import com.kjp.shoppingcart.entities.CartEntity;
import com.kjp.shoppingcart.entities.CartStatusEnum;
import com.kjp.shoppingcart.entities.ProductCartEntity;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.repositories.ICartRepository;
import com.kjp.shoppingcart.repositories.IOrderProductRepository;
import com.kjp.shoppingcart.repositories.IOrderRepository;
import com.kjp.shoppingcart.repositories.IProductCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
public class CartService {

    ICartRepository cartRepository;
    IOrderRepository orderRepository;
    IOrderProductRepository orderProductRepository;
    IProductCartRepository productCartRepository;

    @Autowired
    public CartService(
        ICartRepository cartRepository,
        IOrderRepository orderRepository,
        IOrderProductRepository orderProductRepository,
        IProductCartRepository productCartRepository
       ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productCartRepository = productCartRepository;
    }

    public void addProductsToUserCart(UUID userId, ProductsIdListDTO productsIdListDTO) {
        CartEntity userCart = getOrCreateUserCart(userId);
        Map<UUID, Integer> compactedProductIdQuantities = getCompactedProductsQuantities(productsIdListDTO.productsId());
        List<ProductCartEntity> productCartEntities = getProductCartList(compactedProductIdQuantities, userCart.getUserId());
        List<ProductCartEntity> finalProductCartEntities = getProductCartListWithExistingSum(productCartEntities);

        this.productCartRepository.saveAll(finalProductCartEntities);
    }


    public void removeProductFromCart(UUID userId, UUID productId) {
        CartEntity userCart = getUserCart(userId);
        this.productCartRepository.deleteByCartIdAndProductId(userCart.getId(), productId);
    }

    public void getAllCartProducts(UUID userId) {
        CartEntity userCart = getUserCart(userId);

    }

    public void makeOrder() {
    }

    public void abortOrder() {
    }

    public void makeDoneOrder() {
    }

    public void getOrders() {
    }
    private List<ProductCartEntity> getProductCartList(Map<UUID, Integer> compactedProductIdQuantities, UUID userCartId) {
        List<ProductCartEntity> productCartEntities = new ArrayList<>();
        for (Map.Entry<UUID, Integer> entry : compactedProductIdQuantities.entrySet()) {
            ProductCartEntity entity = new ProductCartEntity();
            entity.setQuantity(entry.getValue());
            entity.setProductId(entry.getKey());
            entity.setCartId(userCartId);

            productCartEntities.add(entity);
        }
        return productCartEntities;
    }

    private List<ProductCartEntity> getProductCartListWithExistingSum(List<ProductCartEntity> list) {
        List<ProductCartEntity> finalProductCartEntities = new ArrayList<>();

        for (ProductCartEntity productCartEntity: list) {
            Optional<ProductCartEntity> existingEntity = this.productCartRepository.findFirstByCartIdAndProductId(
                    productCartEntity.getCartId(),
                    productCartEntity.getProductId()
            );
            if (existingEntity.isPresent()) {
                ProductCartEntity entity = existingEntity.get();
                entity.setQuantity(entity.getQuantity() + productCartEntity.getQuantity());
                finalProductCartEntities.add(entity);
                continue;
            }
            finalProductCartEntities.add(productCartEntity);
        }

        return finalProductCartEntities;
    }

    private CartEntity getOrCreateUserCart(UUID userId) {
        Optional<CartEntity> preExistUserCart = this.cartRepository.findFirstByUserId(userId);
        CartEntity userCart;
        userCart = preExistUserCart.orElseGet(CartEntity::new);
        userCart.setStatus(CartStatusEnum.PENDING);
        userCart.setUserId(userId);
        this.cartRepository.save(userCart);
        return userCart;
    }

    private Map<UUID, Integer> getCompactedProductsQuantities(UUID[] productsID) {

        Map<UUID, Integer> productsQuantities = new HashMap<>();

        for (UUID productId : productsID) {
            Integer preExisting = productsQuantities.get(productId);
            if (preExisting != null) {
                Integer newQuantity = preExisting + 1;
                productsQuantities.put(productId, newQuantity);
                continue;
            }
            productsQuantities.put(productId, 1);
        }

        return productsQuantities;
    }

    private CartEntity getUserCart(UUID userId) {
        Optional<CartEntity> userCart = this.cartRepository.findFirstByUserId(userId);
        if (userCart.isEmpty()) {
           throw new ResourceNotFoundException("The actual user don't has a product cart");
        }
        return userCart.get();
    }



}
