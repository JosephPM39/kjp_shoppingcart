package com.kjp.shoppingcart.services.patterns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Query {
   private String table = "";
   private String fields = "";
   private String condition = "";
   private String join = "";
   private String orderBy = "";

    @Override
    public String toString() {
        return fields.concat(table).concat(join).concat(condition).concat(orderBy);
    }
}
