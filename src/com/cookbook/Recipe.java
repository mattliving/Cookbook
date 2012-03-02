/**
*@author Giulio Muntoni
*@version 1.0
*/

package com.cookbook;

/**
 * Class representing a Recipe Entity on the database
 */
public class Recipe {
	
	public enum TypeOfMeal { BREAKFAST, LUNCH, DINNER, SNACK, STARTER, MAINCOURSE, DESSERT };
	public enum Season 	   { SPRING, SUMMER, AUTUMN, WINTER };
	
	protected String mName;
	protected String mIngredients;
	protected String mPreparation;
	protected long mIdentifier;
	protected String mType;
	protected int mCookingTime;
	protected String mSeason;
	protected String mRegion;
	protected float mRating;
	
	/**
	 * Constructor
	 * @param mName
	 * @param mIngredients
	 * @param mPreparation
	 * @param identifier
	 * @param type
	 * @param cookingTime
	 * @param season
	 * @param mRegion
	 * @param mRating
	 */
	public Recipe(String name, String ingredients, String preparation, long identifier,
			 String type, int cookingTime, String season, String region, float rating) {
	
		mName = new String(name);
		mIngredients = new String(ingredients); 
		mPreparation = new String(preparation);
		mIdentifier = identifier;
		mType = type;
		mCookingTime = cookingTime;
		mSeason = season;
		mRegion = new String(region);
		mRating = rating;
	}
	
	//Get Functions to access class protected fields 
	
	public String getName(){
		return mName;
	}
	
	public String getIngredients(){
		return mIngredients;
	}
	
	public String getPreparation(){
		return mPreparation;
	}
	
	public long getIdentifier(){
		return mIdentifier;
	}
	
	public String getType(){
		return mType;
	}
	
	public int getCookingTime(){
		return mCookingTime;
	}
	
	public String getSeason(){
		return mSeason;
	}
	
	public String getRegion(){
		return mRegion;
	}
	
	public float getRating(){
		return mRating;
	}
}
