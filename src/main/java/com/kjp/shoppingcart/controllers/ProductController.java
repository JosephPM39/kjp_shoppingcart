package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.services.ProductService;
import com.kjp.shoppingcart.services.patterns.search_product_chain.SearchProductStrategyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.util.List;
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
   public List<ProductEntity> getAll(
         @PathParam("pageSize") int pageSize,
         @PathParam("pageNumber") int pageNumber,
         @PathParam("strategy") SearchProductStrategyEnum strategy,
         @PathParam("strategyValue") String strategyValue
   ) {
      PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
      return ;
   }

   @GetMapping("/:id")
   public ProductEntity getById(@PathVariable UUID id) {
      return new ProductEntity();
   }

   @PostMapping
   public void create(@RequestBody ProductEntity product) {
      productService.create();
   }

   @DeleteMapping("/:id")
   public void remove(@PathVariable UUID id) {

   }

   @PatchMapping("/:id")
   public void update(@PathVariable UUID id, @RequestBody ProductEntity product) {

   }

}
