package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.entities.ProductEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

   @GetMapping
   public List<ProductEntity> getAll(
         @PathParam("pageSize") String pageSize,
         @PathParam("pageOffset") String pageOffset,
         @PathParam("keyWords") String keyWords,
         @PathParam("name") String name,
         @PathParam("category") String category
   ) {
       return List.of();
   }

   @GetMapping("/:id")
   public ProductEntity getById(@PathVariable UUID id) {
      return new ProductEntity();
   }

   @PostMapping
   public void create(@RequestBody ProductEntity product) {

   }

   @DeleteMapping("/:id")
   public void remove(@PathVariable UUID id) {

   }

   @PatchMapping("/:id")
   public void update(@PathVariable UUID id, @RequestBody ProductEntity product) {

   }

}
