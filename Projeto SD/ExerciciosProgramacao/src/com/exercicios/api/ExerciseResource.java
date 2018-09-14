package com.exercicios.api;

import java.util.List;

import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.exercicios.impl.ExerciseManager;
import com.exercicios.impl.UserManager;
import com.exercicios.model.Exercise;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;



@Path("/exercises")
public class ExerciseResource {
	
	//GETS all exercise
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Exercise> getExercises(@QueryParam("title") String title) {
		ExerciseManager exerciseManager = ExerciseManager.getInstance();		
		if(title != null) {
		return exerciseManager.getExercises(title);
		} 
			return exerciseManager.getExercises(); 
		
	}

	
		@Path("/search_user")
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public List<Exercise> getExercise(@PathParam("user") String user) {
			ExerciseManager exerciseManager = ExerciseManager.getInstance();		
			if(user != null) {
			return exerciseManager.getExercisesUser(user);
			} 
				return exerciseManager.getExercises(); 

	}
		
	
	// DELETE a specific exercise
	@Path("/delete_title")
	@DELETE	
	public Response removeExercise(@FormParam("title") String title,@FormParam("token") String token,@Context UriInfo uriInfo) {
		
		ExerciseManager exerciseManager = ExerciseManager.getInstance();
		UserManager userManager = UserManager.getInstance();

		try {
			

			Jwts.parser().setSigningKey(userManager.getKey()).parseClaimsJws(token);
			exerciseManager.removeExercise(title);
			

			return Response.ok().entity("Exercise removed!").build();
		}
		catch(SignatureException e){
			System.out.println(e);
			return Response.serverError().status(401).type("text/plain").entity("Invalid token!").build();

		}
			
	}
	
	//Post a new exercise
		@POST
		@Consumes("application/x-www-form-urlencoded")
		public Response insertExercise(
				@FormParam("title") String title,
				@FormParam("explanation") String explanation, 
				@FormParam("tags") String tags,
				@FormParam("code") String code,
				@FormParam("language") String language,
				@FormParam("output") String output,
				@FormParam("token") String token,
				@Context UriInfo uriInfo) {
			
			ExerciseManager exerciseManager = ExerciseManager.getInstance();
			
			UserManager userManager = new UserManager();
			
			try {
				Jwts.parser().setSigningKey(userManager.getKey()).parseClaimsJws(token);
				// Get user data
				String author = (String) Jwts.parser().setSigningKey(userManager.getKey()).parseClaimsJws(token)
				.getBody().get("username");
				exerciseManager.createExercise(title, author, explanation,tags,code,language, output);
								UriBuilder builder = uriInfo.getAbsolutePathBuilder();	
								builder.path(title);
								return Response.created(builder.build()).build();
			}
			catch(SignatureException e){
				return Response.serverError().status(401).type("text/plain").entity("Invalid token!").build();
			}
		}
	}

