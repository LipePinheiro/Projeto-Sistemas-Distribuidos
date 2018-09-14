package com.exercicios.data;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import static com.mongodb.client.model.Filters.*;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.exercicios.model.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.Block;

public class UserData {

	static UserData userData = null;
	static MongoCollection<User> colUsers;


	public static UserData getInstance() {
		if (userData == null) {
			userData = new UserData();

			CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
					fromProviders(PojoCodecProvider.builder().automatic(true).build()));
			MongoClient mongoClient = new MongoClient("localhost",
					MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
			MongoDatabase dbUser = mongoClient.getDatabase("db");
			colUsers = dbUser.getCollection("user", User.class);
		}
		return userData;
	}

	// GET ALL USERS
	public List<User> getUsers() {
		final List<User> users = new ArrayList<User>();
		Block<User> printBlock = new Block<User>() {
			public void apply(final User user) {
				users.add(user);
			}
		};
		colUsers.find().forEach(printBlock);
		return users;
	}
	
	// GET A SPECIFIC USER
		public List<User> getUser(String username) {
			final List<User> users = new ArrayList<User>();
			Block<User> printBlock = new Block<User>() {
				public void apply(final User user) {
					users.add(user);
				}
			};
			colUsers.find(eq("username", username)).forEach(printBlock);

			return users;
		}
	
	// FUNCTION CHECK CREDENTIALS LOGIN
		public boolean checkCredentials(String username, String password) {
		System.out.println("username: " + username);
		System.out.println("password: " + password);
			if (colUsers.find(and(eq("username", username), eq("password", password))).first() == null) {
				return false;
			} else {
				return true;
			}
		}
		
		public List<User> getData(String username) {		
			final List<User> users = new ArrayList<User>();
			Block<User> printBlock = new Block<User>() {
			    public void apply(final User user) {
			    	users.add(user);
			    }
			};
			colUsers.find(eq("username",username)).forEach(printBlock);;		
			return users;
		}

		

		// POST NEW USER
		public Response createUser(User user) {
			if (colUsers.find(eq("userID", user.getUsername())).first() == null) {
				colUsers.insertOne(user);
			}
			return Response.serverError().status(200).type("text/plain").entity("User Already Exists").build();

		}

		public Response updateU(String username, String novaPassword, String novoEmail) {
			Document setPassword = new Document();
			setPassword.append("password", novaPassword);
			Document update = new Document();
			update.append("$set", setPassword);

			Document setEmail = new Document();
			setEmail.append("email", novoEmail);
			Document updateE = new Document();
			updateE.append("$set", setEmail);
		
			
			
			colUsers.updateOne(eq("username", username), update);
			colUsers.updateOne(eq("username", username), updateE);
			return null;
		}
		
		
	
}
