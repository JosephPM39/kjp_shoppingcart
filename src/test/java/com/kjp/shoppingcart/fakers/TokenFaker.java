package com.kjp.shoppingcart.fakers;

import static com.nimbusds.jose.JWSAlgorithm.*;

import com.nimbusds.jose.Header;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class TokenFaker {
  private static final String json =
      "{\"access_token\":\"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2ZXNVR3pVY3FncDBudzVaNFlkdjdRNjB4NU1EMzl6VDJhWFpWY2p2bjh3In0.eyJleHAiOjE3MDE3ODYwMTAsImlhdCI6MTcwMTc4NTcxMCwianRpIjoiODMxMzE0MzItNTJmYi00OGNlLWFjMTEtOWQ0MjAyYTFiMTY3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9zcHJpbmctYm9vdC1kZXYiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzdjMDQ3NzYtZDVjMC00OWQ5LWE5OTctMGU5MTlhY2ZmMjJkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic3ByaW5nLWJvb3Qtc2hvcHBpbmctY2FydC1hcGktcmVzdCIsInNlc3Npb25fc3RhdGUiOiJhMzBhMTliNi1iODBlLTQxYzMtYTE1Yi00ZTY2YjAyZDM1ZDEiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1zcHJpbmctYm9vdC1kZXYiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InNwcmluZy1ib290LXNob3BwaW5nLWNhcnQtYXBpLXJlc3QiOnsicm9sZXMiOlsiYWRtaW5fY2xpZW50Il19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiJhMzBhMTliNi1iODBlLTQxYzMtYTE1Yi00ZTY2YjAyZDM1ZDEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6ImFkbWluIHRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0LmFkbWluIiwiZ2l2ZW5fbmFtZSI6ImFkbWluIiwiZmFtaWx5X25hbWUiOiJ0ZXN0IiwiZW1haWwiOiJhZG1pbkB0ZXN0LmNvbSJ9.kjGL0wBjppAG3hhy4nJYbwHNgn-ZtodyrH7n9-UvbtI0jxRFWsoMVzIksoAaXjEfYO52NtY5rEXqNlR8boXkHP42BE8f2Q8hwyUYngoPC0_EDCa8q2FZaErKh-UZr5CpMnuAhNvGafYH8wNDmWjj6JROagatAhpFFzXYyqzxtnxI9waCZ6Vf02pg6d8eCrcsm0ONkF23sudJ9fpMbSixprYMbzkNzqsQyEkGBxLar375GaPhuCQ9k4JqJAYnBiAuOaM3tWYKBo_i6tk6Xr-3dDt9k1n2XvgeTurnMB-ITvHbsy928SHInj6P_Ko8-V2CgvgKfUcbcDnuq6z87b86dg\",\"expires_in\":300,\"refresh_expires_in\":1800,\"refresh_token\":\"eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIyYzI3ODc3Yi04YzQ0LTRmMzMtYjM5Ni1kYTkwNTZmNjRhYmEifQ.eyJleHAiOjE3MDE3ODc1MTAsImlhdCI6MTcwMTc4NTcxMCwianRpIjoiYjk3ZTU5ODktNjhhNi00ZGY4LTg1ZTUtMjg1OGZjYTk1OWYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9zcHJpbmctYm9vdC1kZXYiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjgwODEvcmVhbG1zL3NwcmluZy1ib290LWRldiIsInN1YiI6ImM3YzA0Nzc2LWQ1YzAtNDlkOS1hOTk3LTBlOTE5YWNmZjIyZCIsInR5cCI6IlJlZnJlc2giLCJhenAiOiJzcHJpbmctYm9vdC1zaG9wcGluZy1jYXJ0LWFwaS1yZXN0Iiwic2Vzc2lvbl9zdGF0ZSI6ImEzMGExOWI2LWI4MGUtNDFjMy1hMTViLTRlNjZiMDJkMzVkMSIsInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6ImEzMGExOWI2LWI4MGUtNDFjMy1hMTViLTRlNjZiMDJkMzVkMSJ9.sETh85JXSUEhju75Jhlg0uzuOCD02yuqqTSLG6GK4YQ\",\"token_type\":\"Bearer\",\"not-before-policy\":0,\"session_state\":\"a30a19b6-b80e-41c3-a15b-4e66b02d35d1\",\"scope\":\"email profile\"}";
  private static final String token =
      "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2ZXNVR3pVY3FncDBudzVaNFlkdjdRNjB4NU1EMzl6VDJhWFpWY2p2bjh3In0.eyJleHAiOjE3MDE3ODYwMTAsImlhdCI6MTcwMTc4NTcxMCwianRpIjoiODMxMzE0MzItNTJmYi00OGNlLWFjMTEtOWQ0MjAyYTFiMTY3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9zcHJpbmctYm9vdC1kZXYiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzdjMDQ3NzYtZDVjMC00OWQ5LWE5OTctMGU5MTlhY2ZmMjJkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic3ByaW5nLWJvb3Qtc2hvcHBpbmctY2FydC1hcGktcmVzdCIsInNlc3Npb25fc3RhdGUiOiJhMzBhMTliNi1iODBlLTQxYzMtYTE1Yi00ZTY2YjAyZDM1ZDEiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1zcHJpbmctYm9vdC1kZXYiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InNwcmluZy1ib290LXNob3BwaW5nLWNhcnQtYXBpLXJlc3QiOnsicm9sZXMiOlsiYWRtaW5fY2xpZW50Il19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiJhMzBhMTliNi1iODBlLTQxYzMtYTE1Yi00ZTY2YjAyZDM1ZDEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6ImFkbWluIHRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0LmFkbWluIiwiZ2l2ZW5fbmFtZSI6ImFkbWluIiwiZmFtaWx5X25hbWUiOiJ0ZXN0IiwiZW1haWwiOiJhZG1pbkB0ZXN0LmNvbSJ9.kjGL0wBjppAG3hhy4nJYbwHNgn-ZtodyrH7n9-UvbtI0jxRFWsoMVzIksoAaXjEfYO52NtY5rEXqNlR8boXkHP42BE8f2Q8hwyUYngoPC0_EDCa8q2FZaErKh-UZr5CpMnuAhNvGafYH8wNDmWjj6JROagatAhpFFzXYyqzxtnxI9waCZ6Vf02pg6d8eCrcsm0ONkF23sudJ9fpMbSixprYMbzkNzqsQyEkGBxLar375GaPhuCQ9k4JqJAYnBiAuOaM3tWYKBo_i6tk6Xr-3dDt9k1n2XvgeTurnMB-ITvHbsy928SHInj6P_Ko8-V2CgvgKfUcbcDnuq6z87b86dg";

  public static String getStringJsonKeyClaokResponse() {
    return json;
  }

  public static JSONObject getJSON() {
    return (JSONObject) JSONValue.parse(json);
  }

  public static Jwt getJwtToken() {
    try {
      JWT jwt = JWTParser.parse(token);

      JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();

      String tokenValue = jwt.getParsedString();
      Instant issuedAt = claimsSet.getIssueTime().toInstant();
      Instant expiresAt = claimsSet.getExpirationTime().toInstant();
      Map<String, Object> claims = claimsSet.getClaims();
      Map<String, Object> headersMap = extractHeaders(jwt.getHeader());

      return new Jwt(tokenValue, issuedAt, expiresAt, headersMap, claims);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Map<String, Object> extractHeaders(Header headers) {
    return headers != null ? headers.toJSONObject() : Collections.emptyMap();
  }

  public static JwtAuthenticationToken getJwtAuthToken() {
    return new JwtAuthenticationToken(getJwtToken());
  }
}
