package com.cookbook.activity;

import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import android.widget.SimpleAdapter;

import com.cookbook.R;
import com.cookbook.adapter.CookbookDBAdapter;
import com.cookbook.adapter.RecipeListAdapter;
import com.cookbook.adapter.SeparatedListAdapter;

public class SortedListActivity extends BasicListActivity{

	/**
	 * Headers Array
	 */
	protected Vector<String> headnames  = new Vector<String>();
	
	String TYPE[] = {"Breakfast","Lunch","Dinner","Dessert","Snack","Undefined"};
	//Not final
	String SEASON[] = {"Spring","Summer","Autumn","Winter","Christmas","Halloween","Undefined"};
	
	String REGION[] = {"England","Scotland","Wales", "Ireland","United States","Italy","Spain","France", "Germany","Undefined",};
	
	
	
	
	/** ==================================== MEHODS =======================================*/
	
	
	@Override
	public void getItems()
	{
		
		/** Initialise the DB */
	     mDbHelper = new CookbookDBAdapter(this);
	     mDbHelper.open();
		 
	     adapter = new SeparatedListAdapter(this);
	     
	   /**sorting mode VALUES: "recipeName" "method" 
	 	* 	"mealType" "duration" "timeOfYear" "region" "rating" */
		 if (sortby.equalsIgnoreCase("recipeName")) byName();
		 else if (sortby.equalsIgnoreCase("mealType")) byType();
		 else if (sortby.equalsIgnoreCase("timeOfYear")) bySeason();
		 else if (sortby.equalsIgnoreCase("region")) byRegion();
		 else byName();
		 
		 mDbHelper.close();
		
		
	}
	
	
	@Override
	public void bindValuesToView()
	{
		
		 
		 
		 lv.setAdapter(adapter);
		lv.setTextFilterEnabled(true);
		
	}
	
	/**
	 * Sorting By Name
	 */
	public void byName()
	{
		for (char c='A'; c<='Z';c++)
		{
			list.clearList();
			LinkedList<Map<String,Object>> recipes = new LinkedList<Map<String,Object>>(); 
			list.fetchByPatternName(mDbHelper, String.valueOf(c)+"%");
			
			if (list.size()>0)
			{
				headnames.add(String.valueOf(c));
				for (int i =0; i<list.size();i++)
				  {
					  recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType(),list.getRecipe(i).getRating())); 
					  System.out.println(list.getRecipe(i).getName());
				  }
				 
				  adapter.addSection(String.valueOf(c), new RecipeListAdapter(recipes, this));
				 isEmpty = false; 
			}
			
		}
		
		 
	}
	
	/**
	 * Sorting by type
	 */
	public void byType()
	{
		for (int j =0; j<TYPE.length;j++)
		{
			list.clearList();
			LinkedList<Map<String,Object>> recipes = new LinkedList<Map<String,Object>>(); 
			list.fetchfilterRecipesSorted(mDbHelper, TYPE[j], cookingDuration, season, country, rating,sortby); 
			
			if (list.size()>0)
			{
				headnames.add( TYPE[j]);
				for (int i =0; i<list.size();i++)
				  {
					  recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType(),list.getRecipe(i).getRating())); 
					  System.out.println(list.getRecipe(i).getName());
				  }
				 
				  adapter.addSection(TYPE[j], new RecipeListAdapter(recipes, this));
				  
			}
			
		}
		
		
	}
	
	/**
	 * Sorting by Season
	 */
	public void bySeason()
	{
		for (int j =0; j<SEASON.length;j++)
		{
			 list.clearList();
			LinkedList<Map<String,Object>> recipes = new LinkedList<Map<String,Object>>(); 
			list.fetchfilterRecipesSorted(mDbHelper, mealType, cookingDuration, SEASON[j], country, rating,sortby); 
			
			if (list.size()>0)
			{
				headnames.add( SEASON[j]);
				for (int i =0; i<list.size();i++)
				  {
					  recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType(),list.getRecipe(i).getRating())); 
					  System.out.println(list.getRecipe(i).getName());
				  }
				 
				  adapter.addSection(SEASON[j], new RecipeListAdapter(recipes, this));
				 
			}
			
		}
	}
	
	/**
	 * sorting by region
	 */
	public void byRegion()
	{
		for (int j =0; j<REGION.length;j++)
		{
			list.clearList();
			LinkedList<Map<String,Object>> recipes = new LinkedList<Map<String,Object>>(); 
			list.fetchfilterRecipesSorted(mDbHelper, mealType, cookingDuration, season, REGION[j], rating,sortby); 
			
			if (list.size()>0)
			{
				headnames.add( REGION[j]);
				for (int i =0; i<list.size();i++)
				  {
					  recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType(),list.getRecipe(i).getRating())); 
					  System.out.println(list.getRecipe(i).getName());
				  }
				 
				  adapter.addSection(REGION[j], new RecipeListAdapter(recipes, this));
				  
			}
			
		}
	}
	
	
}
