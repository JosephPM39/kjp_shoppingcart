package com.kjp.shoppingcart.services.patterns.search_product_chain;

public enum SearchProductStrategyEnum {
  BY_NAME,
  BY_NAME_CONTAINS,
  BY_CATEGORY_NAME,
  BY_KEYWORD_IN_NAME_OR_DESCRIPTION,
  NONE
}
