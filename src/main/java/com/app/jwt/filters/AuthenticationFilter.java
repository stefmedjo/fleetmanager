package com.app.jwt.filters;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.json.JsonString;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.app.jwt.annotations.IsAuthenticated;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Provider
@IsAuthenticated
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    JsonWebToken jwt;

    @Inject
	@Claim(standard = Claims.preferred_username)
    Optional<JsonString> username;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //System.out.println(username);
        /**
         * Here you can put the logic
         * Verify if user with this username exists
         * Verify if he is enabled
         * Get his role
         */
    }

    
}