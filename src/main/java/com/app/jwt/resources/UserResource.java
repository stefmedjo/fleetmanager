package com.app.jwt.resources;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonString;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.app.jwt.models.User;
import com.app.jwt.utils.TokenUtils;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

@Path("/api/v1/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserResource {

    @Inject
	@Claim(standard = Claims.preferred_username)
    Optional<JsonString> username;
    
    @GET
    @RolesAllowed({ TokenUtils.ROLE_ADMIN })
    public Response list(){
        return Response.ok(User.findAll().list()).build();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({ TokenUtils.ROLE_USER })
    public Response read(@PathParam("id") Long id){
        return Response.ok(User.findById(id)).build();
    }
    
}