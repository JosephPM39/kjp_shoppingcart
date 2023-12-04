package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.ProductCartDTO;
import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import java.util.List;
import java.util.UUID;

public interface ICartService {

  public void addProductsToUserCart(UUID userId, ProductsIdListDTO productsIdListDTO);

  public void removeAllOfProductFromCart(UUID userId, UUID productId);

  public void removeProductFromCart(UUID userId, UUID product, Integer quantity);

  public List<ProductCartDTO> getAllCartProducts(UUID userId);
}
