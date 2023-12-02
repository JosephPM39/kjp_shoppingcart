package com.kjp.shoppingcart.utils;

import com.kjp.shoppingcart.dto.SignInCredentialsDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {

    SignInCredentialsDTO credentialsWithNullProperties;
    SignInCredentialsDTO credentialsWithNotNullProperties;

    @BeforeEach
    void setUp() {
        credentialsWithNotNullProperties = new SignInCredentialsDTO("User", "Password");
        credentialsWithNullProperties = new SignInCredentialsDTO("User2", null);
    }

    @AfterEach
    void tearDown() {
        credentialsWithNullProperties = null;
        credentialsWithNotNullProperties = null;
    }

    @Test
    void getNullPropertyNames() {
        Set<String> nullProperties = ObjectUtils.getNullPropertyNames(credentialsWithNullProperties);
        assertTrue(nullProperties.contains("password"));
    }

    @Test
    void copyNotNullProperties() throws Exception {
        SignInCredentialsDTO result = ObjectUtils.copiarPropiedades(credentialsWithNullProperties, credentialsWithNotNullProperties, credentialsWithNullProperties.getClass());
        assertNotNull(result.password());
        assertEquals(result.userName(), credentialsWithNullProperties.userName());
        assertEquals(result.password(), "Password");
    }

}