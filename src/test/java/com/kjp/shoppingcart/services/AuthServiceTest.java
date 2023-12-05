package com.kjp.shoppingcart.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kjp.shoppingcart.config.ApiEnvConfig;
import com.kjp.shoppingcart.dto.SignInCredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;
import java.net.URI;
import java.net.URISyntaxException;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class AuthServiceTest {

  private final RestTemplate restTemplateMock = mock(RestTemplate.class);
  private final ApiEnvConfig apiEnvConfigMock = mock(ApiEnvConfig.class);

  private final AuthService authService = new AuthService(restTemplateMock, apiEnvConfigMock);

  private final String json =
      "{\"access_token\":\"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2ZXNVR3pVY3FncDBudzVaNFlkdjdRNjB4NU1EMzl6VDJhWFpWY2p2bjh3In0.eyJleHAiOjE3MDE3ODYwMTAsImlhdCI6MTcwMTc4NTcxMCwianRpIjoiODMxMzE0MzItNTJmYi00OGNlLWFjMTEtOWQ0MjAyYTFiMTY3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9zcHJpbmctYm9vdC1kZXYiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzdjMDQ3NzYtZDVjMC00OWQ5LWE5OTctMGU5MTlhY2ZmMjJkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic3ByaW5nLWJvb3Qtc2hvcHBpbmctY2FydC1hcGktcmVzdCIsInNlc3Npb25fc3RhdGUiOiJhMzBhMTliNi1iODBlLTQxYzMtYTE1Yi00ZTY2YjAyZDM1ZDEiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1zcHJpbmctYm9vdC1kZXYiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InNwcmluZy1ib290LXNob3BwaW5nLWNhcnQtYXBpLXJlc3QiOnsicm9sZXMiOlsiYWRtaW5fY2xpZW50Il19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiJhMzBhMTliNi1iODBlLTQxYzMtYTE1Yi00ZTY2YjAyZDM1ZDEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6ImFkbWluIHRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0LmFkbWluIiwiZ2l2ZW5fbmFtZSI6ImFkbWluIiwiZmFtaWx5X25hbWUiOiJ0ZXN0IiwiZW1haWwiOiJhZG1pbkB0ZXN0LmNvbSJ9.kjGL0wBjppAG3hhy4nJYbwHNgn-ZtodyrH7n9-UvbtI0jxRFWsoMVzIksoAaXjEfYO52NtY5rEXqNlR8boXkHP42BE8f2Q8hwyUYngoPC0_EDCa8q2FZaErKh-UZr5CpMnuAhNvGafYH8wNDmWjj6JROagatAhpFFzXYyqzxtnxI9waCZ6Vf02pg6d8eCrcsm0ONkF23sudJ9fpMbSixprYMbzkNzqsQyEkGBxLar375GaPhuCQ9k4JqJAYnBiAuOaM3tWYKBo_i6tk6Xr-3dDt9k1n2XvgeTurnMB-ITvHbsy928SHInj6P_Ko8-V2CgvgKfUcbcDnuq6z87b86dg\",\"expires_in\":300,\"refresh_expires_in\":1800,\"refresh_token\":\"eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIyYzI3ODc3Yi04YzQ0LTRmMzMtYjM5Ni1kYTkwNTZmNjRhYmEifQ.eyJleHAiOjE3MDE3ODc1MTAsImlhdCI6MTcwMTc4NTcxMCwianRpIjoiYjk3ZTU5ODktNjhhNi00ZGY4LTg1ZTUtMjg1OGZjYTk1OWYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9zcHJpbmctYm9vdC1kZXYiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjgwODEvcmVhbG1zL3NwcmluZy1ib290LWRldiIsInN1YiI6ImM3YzA0Nzc2LWQ1YzAtNDlkOS1hOTk3LTBlOTE5YWNmZjIyZCIsInR5cCI6IlJlZnJlc2giLCJhenAiOiJzcHJpbmctYm9vdC1zaG9wcGluZy1jYXJ0LWFwaS1yZXN0Iiwic2Vzc2lvbl9zdGF0ZSI6ImEzMGExOWI2LWI4MGUtNDFjMy1hMTViLTRlNjZiMDJkMzVkMSIsInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6ImEzMGExOWI2LWI4MGUtNDFjMy1hMTViLTRlNjZiMDJkMzVkMSJ9.sETh85JXSUEhju75Jhlg0uzuOCD02yuqqTSLG6GK4YQ\",\"token_type\":\"Bearer\",\"not-before-policy\":0,\"session_state\":\"a30a19b6-b80e-41c3-a15b-4e66b02d35d1\",\"scope\":\"email profile\"}";

  @Test
  void signIn() throws URISyntaxException {

    JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
    ResponseEntity<JSONObject> response = ResponseEntity.ok(jsonObject);

    when(restTemplateMock.postForEntity(any(URI.class), any(Object.class), eq(JSONObject.class)))
        .thenReturn(response);
    when(apiEnvConfigMock.getKEYCLOAK_SERVER_GENERATE_TOKEN_URL())
        .thenReturn("http://localhost:8081/realm");

    TokenDTO token = authService.signIn(new SignInCredentialsDTO("test.admin", "testtest"));
    assertEquals("Bearer", token.tokenType());
    assertEquals(300, token.expiresIn());
    assertEquals(1800, token.refreshExpiresIn());
    verify(restTemplateMock).postForEntity(any(URI.class), any(Object.class), eq(JSONObject.class));
    verify(apiEnvConfigMock).getKEYCLOAK_SERVER_GENERATE_TOKEN_URL();
  }
}
