package com.kjp.shoppingcart.fakers;

import com.kjp.shoppingcart.entities.BaseEntity;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FakerUtils {
  public static String toUniqueAndLimitSize(String data, Integer length) {
    String newData =
        data.concat(randomNumber().toString())
            .concat(randomNumber().toString())
            .concat(randomNumber().toString())
            .concat(randomNumber().toString());
    return truncateFromStart(newData, length);
  }

  private static Integer randomNumber() {
    Random random = new Random();
    return random.nextInt(9) + 1;
  }

  public static String truncateFromStart(String str, int maxLength) {
    if (str.length() > maxLength) {
      return str.substring(str.length() - maxLength);
    } else {
      return str;
    }
  }

  public static Timestamp getTimestamp() {
    return Timestamp.from(Instant.now());
  }

  public static <T extends BaseEntity> UUID randomIdFromEntityList(List<T> list) {
    if (list != null && !list.isEmpty()) {
      Random random = new Random();
      int randomIndex = random.nextInt(list.size());
      return list.get(randomIndex).getId();
    } else {
      return null;
    }
  }
}
