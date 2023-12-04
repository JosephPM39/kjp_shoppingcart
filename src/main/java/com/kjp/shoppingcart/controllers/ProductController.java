package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.exceptions.BadStrategySearchParams;
import com.kjp.shoppingcart.services.IProductService;
import com.kjp.shoppingcart.services.patterns.search_product_chain.SearchProductStrategyEnum;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.kjp.shoppingcart.validations.groups.CreateGroup;
import com.kjp.shoppingcart.validations.groups.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final IProductService productService;

  @Autowired
  public ProductController(IProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  @PreAuthorize(
      "hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
  public Page<ProductEntity> getAll(
      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSizeParam,
      @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumberParam,
      @RequestParam(name = "strategy", defaultValue = "NONE") SearchProductStrategyEnum strategyParam,
      @RequestParam(name = "strategyValue", defaultValue = "") String strategyValueParam) {

    Optional<Integer> pageSize = Optional.ofNullable(pageSizeParam);
    Optional<Integer> pageNumber = Optional.ofNullable(pageNumberParam);
    Optional<SearchProductStrategyEnum> strategy = Optional.ofNullable(strategyParam);
    Optional<String> strategyValue = Optional.ofNullable(strategyValueParam);


    if (isOnlyOneOfBothPresent(strategy, strategyValue)) {
      String error =
          "To use search by value, we need two parameters: "
              .concat("\"strategy\" and \"searchValue\". ")
              .concat("Example: strategy=BY_NAME&searchValue=product1.");
      throw new BadStrategySearchParams(error);
    }

    PageRequest pageRequest = PageRequest.of(pageNumber.orElse(0), pageSize.orElse(25));
    return productService.getAll(
        pageRequest, strategy.orElse(SearchProductStrategyEnum.NONE), strategyValue.orElse(null));
  }

  private boolean isOnlyOneOfBothPresent(
      Optional<SearchProductStrategyEnum> strategy, Optional<String> strategyValue) {
    return strategy.isPresent() && strategyValue.isEmpty()
        || (strategy.isEmpty() && strategyValue.isPresent());
  }

  @GetMapping("/{id}")
  @PreAuthorize(
      "hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
  public ProductEntity getById(@PathVariable UUID id) {
    return productService.getById(id);
  }

  @PostMapping("/")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void create(@Validated(CreateGroup.class) @RequestBody ProductEntity product) {
    productService.create(product);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void update(@PathVariable UUID id, @Validated(UpdateGroup.class) @RequestBody ProductEntity product) {
    productService.update(id, product);
  }

  @PostMapping("/{id}/disable")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void disable(@PathVariable UUID id) {
    productService.disable(id);
  }

  @PostMapping("/{id}/enable")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void enable(@PathVariable UUID id) {
    productService.enable(id);
  }
}
