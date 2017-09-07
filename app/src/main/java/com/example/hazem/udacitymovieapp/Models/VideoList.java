package com.example.hazem.udacitymovieapp.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VideoList{

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private ArrayList<Video> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(ArrayList<Video> results){
		this.results = results;
	}

	public ArrayList<Video> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"VideoList{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}