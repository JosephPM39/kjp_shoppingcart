package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.CredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;

public interface IAuthService {
    public TokenDTO signIn(CredentialsDTO credentials);
}
