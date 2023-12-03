package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.exceptions.BadStrategySearchParams;
import com.kjp.shoppingcart.services.ProductService;
import com.kjp.shoppingcart.services.patterns.search_product_chain.SearchProductStrategyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

   ProductService productService;

   @Autowired
   public ProductController(ProductService productService){
      this.productService = productService;
   }

   @GetMapping
   public Page<ProductEntity> getAll(
         @PathParam("pageSize") Optional<Integer> pageSize,
         @PathParam("pageNumber") Optional<Integer> pageNumber,
         @PathParam("strategy") Optional<SearchProductStrategyEnum> strategy,
         @PathParam("strategyValue") Optional<String> strategyValue
   ) {

      if (isOnlyOneOfBothPresent(strategy, strategyValue)) {
         String error = "To use search by value, we need two parameters: "
                 .concat("\"strategy\" and \"searchValue\". ")
                 .concat("Example: strategy=BY_NAME&searchValue=product1.");
         throw new BadStrategySearchParams(error);
      }

      PageRequest pageRequest = PageRequest.of(pageNumber.orElse(0), pageSize.orElse(25));
      return productService.getAll(pageRequest, strategy.orElse(SearchProductStrategyEnum.NONE), strategyValue.orElse(null));
   }

   private boolean isOnlyOneOfBothPresent(Optional<SearchProductStrategyEnum> strategy, Optional<String> strategyValue) {
      return strategy.isPresent() && strategyValue.isEmpty() || (strategy.isEmpty() && strategyValue.isPresent());
   }

   @GetMapping("/{id}")
   public ProductEntity getById(@PathVariable UUID id) {
      return productService.getById(id);
   }

   @PostMapping
   public void create(@RequestBody ProductEntity product) {
      productService.create(product);
   }

   @DeleteMapping("/{id}")
   public void remove(@PathVariable UUID id) {
      productService.remove(id);
   }

   @PatchMapping("/{id}")
   public void update(@PathVariable UUID id, @RequestBody ProductEntity product) {
      productService.update(id, product);
   }

   @PostMapping("/{id}/disable")
   public void disable(@PathVariable UUID id) {
      productService.disable(id);
   }

   @PostMapping("/{id}/enable")
   public void enable(@PathVariable UUID id) {
      productService.enable(id);
   }

}
