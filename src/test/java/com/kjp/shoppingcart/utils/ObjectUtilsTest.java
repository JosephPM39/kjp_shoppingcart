package com.kjp.shoppingcart.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.kjp.shoppingcart.dto.TokenDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Builder
record TypesMock(
    boolean primitiveBoolean,
    Boolean Boolean,
    String String,
    Integer Integer,
    int primitiveInteger,
    double primitiveDouble,
    Double Double,
    List<String> list,
    Map<String, Integer> map,
    String[] array) {}

class ObjectUtilsTest {

  TokenDTO incompleteTokenDTO;
  TokenDTO completeTokenDTO;

  TypesMock incompleteTypesMock;
  TypesMock completeTypesMock;

  @BeforeEach
  void setUp() {
    completeTokenDTO =
        TokenDTO.builder()
            .accessToken("access_token")
            .refreshToken("refresh_token")
            .tokenType("token_type")
            .expiresIn(400)
            .refreshExpiresIn(600)
            .notBeforePolicy("not_before_policy")
            .scope("scope")
            .sessionState("session_state")
            .build();
    incompleteTokenDTO = TokenDTO.builder().tokenType("new_token_type").expiresIn(800).build();

    completeTypesMock =
        TypesMock.builder()
            .array(new String[] {"1a", "2b", "3c"})
            .Boolean(true)
            .Double(1.02)
            .Integer(8)
            .list(List.of("3a", "1b", "2c"))
            .map(Map.of("k1", 1, "k2", 2, "k3", 3))
            .primitiveBoolean(false)
            .primitiveDouble(1.52)
            .primitiveInteger(2)
            .String("Hello World")
            .build();
    incompleteTypesMock =
        TypesMock.builder()
            .String("By world")
            .list(List.of("one", "two", "three"))
            .primitiveDouble(2.43)
            .Integer(4)
            .build();
  }

  @AfterEach
  void tearDown() {
    completeTokenDTO = null;
    incompleteTokenDTO = null;
  }

  @Test
  void getNullPropertyNames() {
    Set<String> nullProperties = ObjectUtils.getNullPropertyNames(incompleteTokenDTO);
    assertTrue(nullProperties.contains("accessToken"));
    assertTrue(nullProperties.contains("refreshToken"));
    assertTrue(nullProperties.contains("refreshExpiresIn"));
    assertTrue(nullProperties.contains("notBeforePolicy"));
    assertTrue(nullProperties.contains("scope"));
    assertTrue(nullProperties.contains("sessionState"));
  }

  @Test
  void getInstanceWithNotNullFields() {
    TokenDTO finalToken =
        ObjectUtils.getInstanceWithNotNullFields(
            incompleteTokenDTO, completeTokenDTO, TokenDTO.class);

    assertEquals(finalToken.tokenType(), incompleteTokenDTO.tokenType());
    assertEquals(finalToken.expiresIn(), incompleteTokenDTO.expiresIn());

    assertEquals(finalToken.accessToken(), completeTokenDTO.accessToken());
    assertEquals(finalToken.notBeforePolicy(), completeTokenDTO.notBeforePolicy());
    assertEquals(finalToken.refreshExpiresIn(), completeTokenDTO.refreshExpiresIn());
    assertEquals(finalToken.refreshToken(), completeTokenDTO.refreshToken());
    assertEquals(finalToken.scope(), completeTokenDTO.scope());
    assertEquals(finalToken.sessionState(), completeTokenDTO.sessionState());

    TypesMock finalTypesMock =
        ObjectUtils.getInstanceWithNotNullFields(
            incompleteTypesMock, completeTypesMock, TypesMock.class);

    assertEquals(finalTypesMock.String(), incompleteTypesMock.String());
    assertEquals(finalTypesMock.list(), incompleteTypesMock.list());
    assertEquals(finalTypesMock.primitiveDouble(), incompleteTypesMock.primitiveDouble());
    assertEquals(finalTypesMock.Integer(), incompleteTypesMock.Integer());

    assertEquals(finalTypesMock.array(), completeTypesMock.array());
    assertEquals(finalTypesMock.Boolean(), completeTypesMock.Boolean());
    assertEquals(finalTypesMock.Double(), completeTypesMock.Double());
    assertEquals(finalTypesMock.map(), completeTypesMock.map());
    assertNotEquals(finalTypesMock.primitiveInteger(), completeTypesMock.primitiveInteger());
    assertEquals(finalTypesMock.primitiveBoolean(), completeTypesMock.primitiveBoolean());
  }
}
