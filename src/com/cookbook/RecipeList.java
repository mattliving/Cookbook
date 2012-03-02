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
		/*
		 * Commented until these fields will be part of the database
		 */
		//String mIngredients = cursor.getString(7);
		//float mRating = cursor.getFloat(8);
		/*
		 *  NOT FINAL 
		 */
		addRecipe(new Recipe(mName," ",mPreparation,identifier,
				type,cookingTime,season,mRegion,1f));
		
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
	
	public void fetchfilterRecipes(CookbookDBAdapter adpt, String param1, int param2, String param3, String param4, float param5){
		
		Cursor cursor = adpt.fetchRecipeFilter(param1,param2,param3, param4,param5);
		
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
}
