package com.exercicios.data;

import javax.ws.rs.core.Response;

import static com.mongodb.client.model.Filters.*;


import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.exercicios.model.Exercise;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

public class ExerciseData {
	
	static ExerciseData exerciseData = null;
	static MongoCollection<Exercise> colExercises;

	public static ExerciseData getInstance() {
		
		if (exerciseData == null) {
			exerciseData = new ExerciseData();

			CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
					fromProviders(PojoCodecProvider.builder().automatic(true).build()));
			MongoClient mongoClient = new MongoClient("localhost",
					MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
			MongoDatabase dbExercise = mongoClient.getDatabase("db");
			colExercises = dbExercise.getCollection("exercises", Exercise.class);
		}
return exerciseData;
	}

	public Response createExercise(Exercise exercise) {
		if (colExercises.find(eq("title", exercise.getTitle())).first() == null) {
			colExercises.insertOne(exercise);
}
		return Response.serverError().status(200).type("text/plain").entity("Exercise Already Exists").build();
	}

	//remover
	public Response removeData(String title) {
		System.out.println("Data = " + title);
		colExercises.deleteOne(eq("title", title));
		return null;
	}
	
	//get exercicios
	public List<Exercise> getData() {
		final List<Exercise> exercises = new ArrayList<Exercise>();
		Block<Exercise> block = new Block<Exercise>() { 
			@Override
			public void apply(Exercise exercise) {
				exercises.add(exercise);	
			}
		};
		colExercises.find().forEach(block);
		return exercises;
	}



	public List<Exercise> getData(String title) {
		final List<Exercise> exercises = new ArrayList<Exercise>();
		Block<Exercise> printBlock = new Block<Exercise>() {
			public void apply(final Exercise exercise) {
				exercises.add(exercise);
			}
		};

		colExercises.find(eq("title", title)).forEach(printBlock);
		return exercises;

	}
	public List<Exercise> getDataUser(String pesquisa) {
		final List<Exercise> exercises = new ArrayList<Exercise>();
		Block<Exercise> printBlock = new Block<Exercise>() {
			public void apply(final Exercise exercise) {
				exercises.add(exercise);
			}
		};

		colExercises.find(eq("user/username", pesquisa)).forEach(printBlock);
		return exercises;

	}

	

}
