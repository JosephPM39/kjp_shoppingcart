package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.ProductCartDTO;
import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import com.kjp.shoppingcart.entities.CartEntity;
import com.kjp.shoppingcart.entities.CartStatusEnum;
import com.kjp.shoppingcart.entities.ProductCartEntity;
import com.kjp.shoppingcart.exceptions.BadProductCartQuantityException;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.mappers.ProductCartMapper;
import com.kjp.shoppingcart.repositories.*;
import com.kjp.shoppingcart.utils.ProductServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService implements ICartService {

    ICartRepository cartRepository;
    IOrderRepository orderRepository;
    IOrderProductRepository orderProductRepository;
    IProductCartRepository productCartRepository;
    IProductRepository productRepository;

    @Autowired
    public CartService(
            ICartRepository cartRepository,
            IOrderRepository orderRepository,
            IOrderProductRepository orderProductRepository,
            IProductCartRepository productCartRepository,
            IProductRepository productRepository
    ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productCartRepository = productCartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addProductsToUserCart(UUID userId, ProductsIdListDTO productsIdListDTO) {
        CartEntity userCart = getOrCreateUserCart(userId);
        Map<UUID, Integer> compactedProductIdQuantities = getCompactedProductsQuantities(productsIdListDTO.productsId());

        ProductServiceUtils.throwIfSomeProductNotFound(compactedProductIdQuantities.keySet().stream().toList(), this.productRepository);

        List<ProductCartEntity> productCartEntities = getProductCartList(compactedProductIdQuantities, userCart.getUserId());
        List<ProductCartEntity> finalProductCartEntities = getProductCartListWithExistingSum(productCartEntities);

        this.productCartRepository.saveAll(finalProductCartEntities);
    }

    @Override
    public void removeAllOfProductFromCart(UUID userId, UUID productId) {
        CartEntity userCart = getUserCart(userId);
        this.productCartRepository.deleteByCartIdAndProductId(userCart.getId(), productId);
    }

    @Override
    public List<ProductCartDTO> getAllCartProducts(UUID userId) {
        CartEntity userCart = getUserCart(userId);
        List<ProductCartEntity> productCartEntities = this.productCartRepository.findAllProductsByCartId(userCart.getId());
        return ProductCartMapper.getProductCartDTOList(productCartEntities);
    }

    @Override
    public void removeProductFromCart(UUID userId, UUID productId, Integer quantity) {
        CartEntity userCart = getUserCart(userId);
        Optional<ProductCartEntity> productCartEntity = this.productCartRepository.findFirstByCartIdAndProductId(userCart.getId(), productId);
        if (productCartEntity.isEmpty()) {
            throw new ResourceNotFoundException(
                    "The actual user don't has the product with the Id: "
                            .concat(productId.toString())
                            .concat(" in his cart")
            );
        }
        ProductCartEntity prod = productCartEntity.get();
        if (quantity > prod.getQuantity()) {
            throw new BadProductCartQuantityException(
                    "The cart only has "
                            .concat(prod.getQuantity().toString())
                            .concat(" products, cannot remove ")
                            .concat(quantity.toString())
                            .concat(" products")
            );
        }

        if (quantity.equals(prod.getQuantity())) {
            this.productCartRepository.deleteByCartIdAndProductId(userCart.getId(), productId);
            return;
        }

        prod.setQuantity(prod.getQuantity() - quantity);
        this.productCartRepository.save(prod);
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

        for (ProductCartEntity productCartEntity : list) {
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
        if (preExistUserCart.isPresent()) {
            CartEntity userCart = preExistUserCart.get();
            userCart.setStatus(CartStatusEnum.PENDING);
            this.cartRepository.save(userCart);
            return userCart;
        }
        CartEntity userCart = new CartEntity();
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

