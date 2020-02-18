package com.app.jwt.resources;

import com.app.jwt.dto.UserDTO;
import com.app.jwt.models.JwtTokens;
import com.app.jwt.models.User;
import com.app.jwt.service.TokenService;


import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mindrot.jbcrypt.BCrypt;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/auth")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

	@Inject
	TokenService tokenService;

	@Inject
	JsonWebToken jwt;


	@POST
	@Transactional
	@PermitAll
	@Path("register")
	public Response register(UserDTO user) {
		if(user.username == null || user.password == null || user.username.isEmpty() || user.password.isEmpty())
		{
			return Response.status(Status.BAD_REQUEST).encoding(user.toString()).build();
		}else{
			User newUser = new User();
			newUser.username = user.username;
			newUser.isEnabled = true;
			newUser.salt = BCrypt.gensalt(12);
			newUser.password = BCrypt.hashpw(user.password, newUser.salt);
			newUser.persist();
			JwtTokens tokens = tokenService.generate(newUser.username, newUser.username,1,1440);
			return Response.ok(tokens).build();
		}
	}

	@POST
	@PermitAll
	@Path("login")
	public Response login(User user){
		if(user.username == null || user.password == null || user.username.isEmpty() || user.password.isEmpty())
		{
			return Response.status(Status.BAD_REQUEST).build();
		}else{
			User _user = User.findByUsername(user.username);
			if(_user == null){
				return Response.status(Status.NOT_FOUND).build();
			}else{
				if(BCrypt.checkpw(user.password, _user.password)){
					JwtTokens tokens = tokenService.generate(user.username, user.username,1,1440);
					return Response.ok(tokens).build();
				}else{
					return Response.status(Status.FORBIDDEN).build();
				}
			}
		}
	}

	@POST
	@PermitAll
	@Path("refresh")
	public Response refresh() {
		if(jwt.containsClaim("preferred_username")){
			String username = jwt.getName();
			User user = User.findByUsername(username);
			if(user == null){
				return Response.status(Status.NOT_FOUND).build();
			}else{
				JwtTokens tokens = tokenService.generate(user.username, user.username,1,1440);
				return Response.ok(tokens).build();
			}
		}else{
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
}
