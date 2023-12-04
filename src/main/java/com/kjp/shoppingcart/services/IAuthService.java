package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.SignInCredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;

public interface IAuthService {
  public TokenDTO signIn(SignInCredentialsDTO credentials);
}
