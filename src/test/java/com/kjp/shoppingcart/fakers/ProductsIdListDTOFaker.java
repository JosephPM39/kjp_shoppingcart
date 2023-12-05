package com.kjp.shoppingcart.fakers;

import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import java.util.UUID;

public class ProductsIdListDTOFaker {
  public static ProductsIdListDTO getFake(Integer length, boolean duplicates) {
    UUID[] uuids = new UUID[length];
    UUID uuid = UUID.randomUUID();
    for (int i = 0; i < length; i++) {
      if (i == 0 || (i + 1) == length && duplicates) {
        uuids[i] = uuid;
        continue;
      }
      uuids[i] = UUID.randomUUID();
    }
    return new ProductsIdListDTO(uuids);
  }
}
