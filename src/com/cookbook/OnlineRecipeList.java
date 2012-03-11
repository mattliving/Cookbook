package com.cookbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cookbook.adapter.OnlineQueryAdapter;
import com.cookbook.RecipeList;

import android.util.Log;


public class OnlineRecipeList{
	private int row = 0;
	private JSONArray jArray = null;
	private JSONObject record;
	private boolean containsRatings = false;

	
	public OnlineRecipeList() {
	}
	public OnlineRecipeList(String result, boolean withRating) {
		try {
			jArray = new JSONArray(result);
			record = jArray.getJSONObject(0);
			containsRatings = withRating;
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error constructing OnlineRecipeList" + e.toString());
		}
	}

	// Print a single record in this OnlineRecipeList
	public void print() {
		System.out.println(this.getAuthorId());
		System.out.println(this.getRecipeId());
		System.out.println(this.getRecipeName());
		System.out.println(this.getMethod());
		System.out.println(this.getCategory());
		System.out.println(this.getDuration());
		System.out.println(this.getOccasion());
		System.out.println(this.getRegion());
		System.out.println(this.getServes());
		if (containsRatings) {
			System.out.println(this.getAvgRating());
			System.out.println(this.getRatingCount());
		}
	}

	// Print all records in the OnlineRecipeList object
	public void printAll() {
		for (int i = 0; i < this.numberOfRecords(); i++) {
			this.print();
			this.nextRecord();
		}
	}

	public int nextRecord() {
		if ((row+1) < jArray.length()) {
			try {
				record = jArray.getJSONObject(row+1);
				row++;
			}
			catch (JSONException e) {
				Log.e("log_tag", "Error moving to next record" + e.toString());
			}
		}
		return -1;
	}

	public int recordNumber() {
		return row;
	}

	public int numberOfRecords() {
		return jArray.length();
	}
	
	public String getAuthorId() {
		try {
			return record.getString("author_facebook_id");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting author_facebook_id" + e.toString());
		}
		return null;
	}

	public int getRecipeId() {
		try {
			return record.getInt("recipe_id");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting recipeId" + e.toString());
		}
		return -1;
	}

	public String getRecipeName() {
		try {
			return record.getString("recipeName");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting recipeName" + e.toString());
		}
		return null;
	}

	public String getMethod() {
		try {
			return record.getString("method");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting method" + e.toString());
		}
		return null;
	}

	public String getCategory() {
		try {
			return record.getString("category");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting category" + e.toString());
		}
		return null;
	}

	public int getDuration() {
		try {
			if (record.getString("duration").equals("null"))
				return -2;
			else
				return record.getInt("duration");
		}
		catch (JSONException e) {
			Log.e("OnlineRecipeList.getDuration", "Error getting duration: " + e.toString());
		}
		return -1;
	}

	public String getOccasion() {
		try {
			return record.getString("occasion");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting occasion: " + e.toString());
		}
		return null;
	}

	public String getRegion() {
		try {
			return record.getString("region");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting region" + e.toString());
		}
		return null;
	}

	public int getServes() {
		try {
			if (record.getString("serves").equals("null"))
				return -2;
			else
				return record.getInt("serves");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting serves" + e.toString());
		}
		return -1;
	}

	public double getAvgRating() {
		if (containsRatings) {
			try {
				if (record.getString("avgRating").equals("null"))
					return 0;
				else
					return record.getDouble("avgRating");
			}
			catch (JSONException e) {
				Log.e("log_tag", "Error getting avgRating" + e.toString());
			}
			return -1;
		}
		else
			return -2;
	}

	public int getRatingCount() {
		if (containsRatings) {
			try {
				if (record.getString("ratingCount").equals("null"))
					return 0;
				else
					return record.getInt("ratingCount");
			}
			catch (JSONException e) {
				Log.e("log_tag", "Error getting ratingCount" + e.toString());
			}
			return -1;
		}
		else
			return -2;
	}
	
	public JSONArray getJarray() {
		return jArray;		
	}
	
	public RecipeList MakeRecipeListFromFriendOnline(String friendId, OnlineQueryAdapter onlinequery) {

		RecipeList recipelist = new RecipeList();
		RecipeList rl = new RecipeList();
		if (friendId != "NULL") recipelist = rl.fetchAllOnlineRecipesFor(onlinequery, friendId);
		return recipelist;
	}
	
	public RecipeList MakeRecipeListFromAllOnline(OnlineQueryAdapter onlinequery) {

		RecipeList recipelist = new RecipeList();
		recipelist = recipelist.fetchAllOnlineRecipes(onlinequery);
		return recipelist;
	}
}
