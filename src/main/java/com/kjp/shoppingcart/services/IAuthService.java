package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.CredentialsDTO;

public interface IAuthService {
    public String signIn(CredentialsDTO credentials);
}
