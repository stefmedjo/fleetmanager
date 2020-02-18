package com.app.jwt.service;

import com.app.jwt.models.JwtTokens;
import com.app.jwt.utils.TokenUtils;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;

import javax.enterprise.context.RequestScoped;
import java.util.Arrays;

@RequestScoped
public class TokenService {

	public JwtTokens generate(String email, String username, float accessTokenDuration, float refreshTokenDuration) {
		try {
			System.out.println("creating account");

			JwtClaims accessTokenJwtClaims = new JwtClaims();
			accessTokenJwtClaims.setIssuer("https://quarkus.io/using-jwt-rbac");
			accessTokenJwtClaims.setJwtId("a-123");
			accessTokenJwtClaims.setSubject(email);
			accessTokenJwtClaims.setClaim(Claims.upn.name(), email);
			accessTokenJwtClaims.setClaim(Claims.preferred_username.name(), username);
			//jwtClaims.setClaim(Claims.birthdate.name(), birthdate);
			accessTokenJwtClaims.setClaim(Claims.groups.name(), Arrays.asList(TokenUtils.ROLE_USER));
			accessTokenJwtClaims.setAudience("using-jwt");
			accessTokenJwtClaims.setExpirationTimeMinutesInTheFuture(accessTokenDuration);
			
			JwtClaims refreshTokenJwtClaims = new JwtClaims();
			refreshTokenJwtClaims.setIssuer("https://quarkus.io/using-jwt-rbac");
			refreshTokenJwtClaims.setJwtId("a-123");
			refreshTokenJwtClaims.setSubject(email);
			refreshTokenJwtClaims.setClaim(Claims.upn.name(), email);
			refreshTokenJwtClaims.setClaim(Claims.preferred_username.name(), username);
			refreshTokenJwtClaims.setClaim(Claims.groups.name(), Arrays.asList(TokenUtils.ROLE_USER));
			refreshTokenJwtClaims.setAudience("using-jwt");
			refreshTokenJwtClaims.setExpirationTimeMinutesInTheFuture(refreshTokenDuration);


			JwtTokens tokens = new JwtTokens();
			tokens.accessToken = TokenUtils.generateTokenString(accessTokenJwtClaims);
			tokens.refreshToken = TokenUtils.generateRefreshTokenString(refreshTokenJwtClaims);

			return tokens;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Oops!");
		}

	}
}