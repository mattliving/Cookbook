/**
*@author Giulio Muntoni
*@version 1.0
*/

package com.cookbook;

import java.util.Vector;

import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.cookbook.adapter.*;

/** 
 * Class representing a list of Recipes classes
 * @author Giulio
 *
 */
public class RecipeList {

	/** 
	 * The Vector storing the references to Recipes instances 
	 * */
	protected Vector<Recipe> list;
	
	/**
	 * Constructor
	 */
	public RecipeList(){
		list = new Vector<Recipe>();
	}
	
	public void addRecipe(Recipe recipe){
		
		if (!list.contains(recipe)) list.add(recipe);
	}
	
	/**
	 * Removes the instance of Recipe from the list
	 * @deprecated We should overload this function with other kind of input as recipe name,index etc
	 * @param recipe
	 */
	public void removeRecipe(Recipe recipe){
		list.remove(recipe);
	}
	
	public void removeRecipe(String name){
		for (int i =0;i< list.size();i++)
		{
			if (list.get(i).getName().equals(name)) {
				list.remove(i);
				return;
			}
		}
	}
	/**
	 * Removes all elements putting the size of the vector at 0
	 */
	public void clearList() {
		list.removeAllElements();
	}
	
	/**
	 * TO-DO
	 */
	public void mergeList(){
	
	}
	
	public Recipe getRecipe(int index) {
		return list.get(index);
	}
	
	/**
	 * Find if the list contains the same instance of recipes, it doesn't check
	 * if the two classes have the same values
	 * @param recipe
	 * @return
	 */
	public boolean contains(Recipe recipe) {
		return list.contains(recipe);
	}
	
	public boolean contains(String recipeName) {
		for (int i = 0; i< list.size(); i++) {
			if (list.get(i).getName().equals(recipeName)) return true;
		}
		return false;
	}
	
	public boolean contains(long recipeID) {
		for (int i = 0; i< list.size(); i++) {
			if (list.get(i).getIdentifier() == recipeID) return true;
		}
		return false;
	}
	
	public Recipe get(String recipeName) {
		for (int i = 0; i< list.size(); i++) {
			if (list.get(i).getName().equals(recipeName)) return list.get(i);
		}
		return null;
	}
	
	public int size(){
		return list.size();
	}
	
	
	/**
	 * Parse one cursor row into a Recipe and add it to the list
	 * @param cursor
	 */
	public void addRecipe(Cursor cursor) {
		
		int identifier = cursor.getInt(0);
		String mName = cursor.getString(1);
		String mPreparation = cursor.getString(2);
		String type= cursor.getString(3);
		int cookingTime = cursor.getInt(4);
		String season = cursor.getString(5);
		String mRegion = cursor.getString(6);
		float mRating = cursor.getFloat(7);
		/*
		 * Commented until these fields will be part of the database
		 */
		//String mIngredients = cursor.getString(7);
		//float mRating = cursor.getFloat(8);
		/*
		 *  NOT FINAL 
		 */
		addRecipe(new Recipe(mName," ",mPreparation,identifier,
				type,cookingTime,season,mRegion,mRating));
		
	}
	
	/**
	 * Build a list with the whole database. to be used only in ALPHA-BETA
	 * @param adapt the cookbook adapter
	 */
	public void fetchAllRecipes(CookbookDBAdapter adpt) {
		
		Cursor cursor = adpt.fetchAllRecipes();
		
		if (cursor == null) return;
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	}
	
	public void fetchAllRecipesByName(CookbookDBAdapter adpt) {
		
		Cursor cursor = adpt.fetchAllRecipesByName();
		
		if (cursor == null) return;
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	}

	public void fetchAllRecipesByDuration(CookbookDBAdapter adpt) {
		
		Cursor cursor = adpt.fetchAllRecipesByDuration();
		
		if (cursor == null) return;
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	}
	public void fetchAllRecipesByOccasion(CookbookDBAdapter adpt) {
		
		Cursor cursor = adpt.fetchAllRecipesByOccasion();
		
		if (cursor == null) return;
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	}
	public void fetchAllRecipesByRegion(CookbookDBAdapter adpt) {
		
		Cursor cursor = adpt.fetchAllRecipesByRegion();
		
		if (cursor == null) return;
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	}
	public void fetchAllRecipesByRating(CookbookDBAdapter adpt) {
		
		Cursor cursor = adpt.fetchAllRecipesByRating();
		
		if (cursor == null) return;
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	}
	
	
	
	
	public void fetchfilterRecipes(CookbookDBAdapter adpt, String mealType, int cookingDuration, String season, String country, float rating){
		
		Cursor cursor = adpt.fetchRecipeFilter(mealType,cookingDuration,season, country,rating);
		
		if (cursor == null) return;
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	}
	
/**
 * fetch filter recipes sorted by sortby
 * @param adpt
 * @param mealType
 * @param cookingDuration
 * @param season
 * @param country
 * @param rating
 * @param sortby
 */
public void fetchfilterRecipesSorted(CookbookDBAdapter adpt, String mealType, int cookingDuration, String season, String country, float rating,String sortby){
		
		Cursor cursor = adpt.fetchRecipeFilterSorted(mealType,cookingDuration,season, country,rating,sortby);
		
		if (cursor == null) return;
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	}

	
	/**
	 * Add Recipes to the list from a list of database IDs
	 * @param ids
	 * @param adpt
	 */
	
	public void fetchFromIDs(Vector<Long> ids, CookbookDBAdapter  adpt) {
		if (ids.size() ==0) return;	
		for (int i=0;i<ids.size();i++){ 
			
			Cursor cur = null;
			try {
				cur = adpt.fetchRecipe(ids.get(i));
			} catch (SQLException e) {
				
			}
			if (cur !=null) addRecipe(cur);
		}
	}
	
	/**
	 * Build a list of recipe with a specific name to be used only in ALPHA-BETA
	 * @param adpt the cookbook adapter
	 */
	public void fetchByName(CookbookDBAdapter  adpt,String name){
		
		Cursor cursor = adpt.fetchRecipe(name);
		if (cursor ==null) return;
		//list.clear();
		
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	}
	
	/**
	 * Build a list of recipe with a specific name to be used only in ALPHA-BETA
	 * @param adpt the cookbook adapter
	 */
	public void fetchByPatternName(CookbookDBAdapter  adpt,String name){
		
		Cursor cursor = adpt.fetchRecipeLike(name);
		if (cursor ==null) return;
		//list.clear();
		
		/**
		 * WHY 84 ROWS if the recipes inserted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}	
	}
	
	public void fetchByIngredientName(CookbookDBAdapter  adpt,String name){
		
		Cursor cursor = adpt.fetchRecipeByIngredient(name);
		if (cursor ==null) return;
		
		Vector<Long> ids = new Vector<Long>();
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			ids.add(cursor.getLong(1));
			
			
		cursor.moveToNext();
		}	
		
		if (ids.size()>0) fetchFromIDs(ids,adpt);
		
		
	}
	
	public RecipeList fetchAllOnlineRecipesFor(OnlineQueryAdapter query, String fid) {

		RecipeList result = new RecipeList();
		String name = "", ingredients = "", preparation = "", type = "", season = "", region = "";
		long id;
		int cookingTime;
		float rating;
		OnlineRecipeList onlinelist = query.fetchRecipesSpecificAuthors(fid);
		OnlineIngredientList ingredientlist; 

		for (int i = 0; i < onlinelist.numberOfRecords(); i++) {

			id = onlinelist.getRecipeId();
			name = onlinelist.getRecipeName();
			ingredientlist = query.fetchRecipeIngredientsOneRecipe(fid,id);
			
			if (ingredientlist != null)
			{
				for (int j = 0; j<ingredientlist.numberOfRecords(); j++){
					ingredients = ingredients + ingredientlist.getIngredientName() + " ,";
					ingredientlist.nextRecord();
				}	
			}
			preparation = onlinelist.getMethod();
			type = onlinelist.getCategory();
			season = onlinelist.getOccasion();
			region = onlinelist.getRegion();
			cookingTime = onlinelist.getDuration();
			rating = onlinelist.getRatingCount();

			Recipe recipe = new Recipe(name, ingredients, preparation, id, type, cookingTime, season, region, rating);
			result.addRecipe(recipe);
			
			onlinelist.nextRecord();
			
			System.out.printf("index = %d\n",i);
			
		}
		return result;

	}
	
	
	public RecipeList fetchAllOnlineRecipes(OnlineQueryAdapter query){

		RecipeList result = new RecipeList();
		String name = "", ingredients = "", preparation = "", type = "", season = "", region = "";
		long id;
		int cookingTime;
		float rating;
		OnlineRecipeList onlinelist = query.fetchAllRecipes();
		OnlineIngredientList ingredientlist; 

		for(int i = 0; i<onlinelist.numberOfRecords(); i++){

			id = onlinelist.getRecipeId();
			name = onlinelist.getRecipeName();
			ingredientlist = query.fetchRecipeIngredientsOneRecipe("", id);
			if (ingredientlist !=null){
			for(int j = 0; j<ingredientlist.numberOfRecords(); j++){
				ingredients = ingredients + ingredientlist.getIngredientName() + " ,";
				ingredientlist.nextRecord();
			}	
			}
			preparation = onlinelist.getMethod();
			type = onlinelist.getCategory();
			season = onlinelist.getOccasion();
			region = onlinelist.getRegion();
			cookingTime = onlinelist.getDuration();
			rating = onlinelist.getRatingCount();

			Recipe recipe = new Recipe(name, ingredients, preparation, id, type, cookingTime, season, region, rating);
			result.addRecipe(recipe);
			
			onlinelist.nextRecord();
			
			System.out.printf("index = %d\n",i);
			
		}
		return result;

	}
	
}
