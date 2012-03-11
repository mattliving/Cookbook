package com.cookbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class OnlineIngredientList {
	private int row = 0;
	private JSONArray jArray = null;
	private JSONObject record;

	public OnlineIngredientList(String result) {
		try {
			jArray = new JSONArray(result);
			record = jArray.getJSONObject(0);
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error constructing OnlineRecipeIngredientList: " + e.toString());
		}
	}

	// Print a single record in this OnlineRecipeList
	public void print() {
		System.out.println(this.getAuthorId());
		System.out.println(this.getRecipeId());
		System.out.println(this.getIngredientName());
		System.out.println(this.getQuantity());
		System.out.println(this.getUnit());
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

	public String getIngredientName() {
		try {
			return record.getString("ingredientName");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting ingredientName" + e.toString());
		}
		return null;
	}
	
	public int getQuantity() {
		try {
			if (record.getString("quantity").equals("null"))
				return -2;
			else
				return record.getInt("quantity");
		}
		catch (JSONException e) {
			Log.e("OnlineRecipeList.getQuantity", "Error getting quantity: " + e.toString());
		}
		return -1;
	}

	public String getUnit() {
		try {
			return record.getString("unit");
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error getting unit: " + e.toString());
		}
		return null;
	}
}
