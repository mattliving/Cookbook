package com.cookbook;

import com.cookbook.adapter.CookbookDBAdapter;

public class Search {
	CookbookDBAdapter mDbHelper;
	
	public Search (CookbookDBAdapter mDbHelper){
		
		 this.mDbHelper = mDbHelper;
	    
	}
	
	public void searchByName(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
        
        //list.clearList();
        
        
        list.fetchByName(mDbHelper, text);
	}
		
	public void searchByPatternName(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }

		
		/*list.fetchByPatternName(mDbHelper, text +"%"+
		"' AND recipeName<>"+"'"+text);*/
        list.fetchByPatternName(mDbHelper, "%"+text+"%"+
		"' AND recipeName<>"+"'"+text);
	}
		
	public void searchByName_TypingError(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        } 
        
        for (int i=1;i<text.length()-1;i++)
        {
        	// TO-DO not working
        	String nName = new String(text.substring(0, i-1));
        	nName.concat("_");
        	text.concat(text.substring(i+1, text.length()-1));
        	list.fetchByPatternName(mDbHelper, nName+
        			"' AND recipeName<>"+"'"+text);
        }
		
	}
	
	public void searchByName_SubString(RecipeList list,String rName){
		if (rName.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
		for (int i=rName.length()/2;i<rName.length()-1;i++)
		{
    	String nName = new String(rName.substring(i,rName.length()-1));
    	list.fetchByPatternName(mDbHelper, nName +
    			"' AND recipeName<>"+"'"+rName);
		}
	}
	
	public void searchByDuration(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
        
        
        
        
        list.fetchByName(mDbHelper, text );
	}
		
	public void searchByPatternDuration(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }

		
		/*list.fetchByPatternName(mDbHelper, text +"%"+
		"' AND recipeName<>"+"'"+text);*/
        list.fetchByPatternName(mDbHelper, "%"+text +"%"+
		"' AND recipeName<>"+"'"+text);
	}
		
	public void searchByDuration_TypingError(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        } 
        
        for (int i=1;i<text.length()-1;i++)
        {
        	// TO-DO not working
        	String nName = new String(text.substring(0, i-1));
        	nName.concat("_");
        	text.concat(text.substring(i+1, text.length()-1));
        	list.fetchByPatternName(mDbHelper, nName +
        			"' AND recipeName<>"+"'"+text);
        }
		
	}
	
	public void searchByDuration_SubString(RecipeList list,String rName){
		if (rName.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
		for (int i=rName.length()/2;i<rName.length()-1;i++)
		{
    	String nName = new String(rName.substring(i,rName.length()-1));
    	list.fetchByPatternName(mDbHelper, nName +
    			"' AND recipeName<>"+"'"+rName);
		}
	}
	
	/**
	 * Actually used
	 * @param list
	 * @param text
	 */
	public void searchByOccasion(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }

        
		list.fetchfilterRecipes(mDbHelper,null , 0, text+"%",null , 0);
	}
		
	public void searchByPatternOccasion(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }

		
		/*list.fetchByPatternName(mDbHelper, text +"%"+
		"' AND recipeName<>"+"'"+text);*/
        list.fetchByPatternName(mDbHelper, "%"+text +"%"+
		"' AND recipeName<>"+"'"+text);
	}
		
	public void searchByOccasion_TypingError(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        } 
        
        for (int i=1;i<text.length()-1;i++)
        {
        	// TO-DO not working
        	String nName = new String(text.substring(0, i-1));
        	nName.concat("_");
        	text.concat(text.substring(i+1, text.length()-1));
        	list.fetchByPatternName(mDbHelper, nName +
        			"' AND recipeName<>"+"'"+text);
        }
		
	}
	
	public void searchByOccasion_SubString(RecipeList list,String rName){
		if (rName.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
		for (int i=rName.length()/2;i<rName.length()-1;i++)
		{
    	String nName = new String(rName.substring(i,rName.length()-1));
    	list.fetchByPatternName(mDbHelper, nName +
    			"' AND recipeName<>"+"'"+rName);
		}
	}
	
	
	/**
	 * ACtually used
	 * @param list
	 * @param text
	 */
	public void searchByRegion(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
        
        
        

		list.fetchfilterRecipes(mDbHelper,null , 0, null, text+"%", 0);
	}
		
	public void searchByPatternRegion(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }

		
		/*list.fetchByPatternName(mDbHelper, text +"%"+
		"' AND recipeName<>"+"'"+text);*/
        list.fetchByPatternName(mDbHelper, "%"+text +"%"+
		"' AND recipeName<>"+"'"+text);
	}
		
	public void searchByRegion_TypingError(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        } 
        
        for (int i=1;i<text.length()-1;i++)
        {
        	// TO-DO not working
        	String nName = new String(text.substring(0, i-1));
        	nName.concat("_");
        	text.concat(text.substring(i+1, text.length()-1));
        	list.fetchByPatternName(mDbHelper, nName +
        			"' AND recipeName<>"+"'"+text);
        }
		
	}
	
	public void searchByRegion_SubString(RecipeList list,String rName){
		if (rName.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
		for (int i=rName.length()/2;i<rName.length()-1;i++)
		{
    	String nName = new String(rName.substring(i,rName.length()-1));
    	list.fetchByPatternName(mDbHelper, nName +
    			"' AND recipeName<>"+"'"+rName);
		}
	
		
	}
	/**
	 * Actually used
	 * @param list
	 * @param text
	 */
	public void searchByCategory(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
        
        
        
        
        list.fetchfilterRecipes(mDbHelper, text+"%", 0, null, null, 0);
	}
		
	public void searchByPatternCategory(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }

		
		/*list.fetchByPatternName(mDbHelper, text +"%"+
		"' AND recipeName<>"+"'"+text);*/
        list.fetchByPatternName(mDbHelper, "%"+text +"%"+
		"' AND recipeName<>"+"'"+text);
	}
		
	public void searchByCategory_TypingError(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        } 
        
        for (int i=1;i<text.length()-1;i++)
        {
        	// TO-DO not working
        	String nName = new String(text.substring(0, i-1));
        	nName.concat("_");
        	text.concat(text.substring(i+1, text.length()-1));
        	list.fetchByPatternName(mDbHelper, nName +
        			"' AND recipeName<>"+"'"+text);
        }
		
	}
	
	public void searchByCategory_SubString(RecipeList list,String rName){
		if (rName.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
		for (int i=rName.length()/2;i<rName.length()-1;i++)
		{
    	String nName = new String(rName.substring(i,rName.length()-1));
    	list.fetchByPatternName(mDbHelper, nName +
    			"' AND recipeName<>"+"'"+rName);
		}
	}
	

	/**
	 * Search by Ingredient Name
	 * @param list
	 * @param rName
	 */
	public void searchByIngredient(RecipeList list,String rName){
		if (rName.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
		
		list.fetchByIngredientName(mDbHelper, rName);
		
	}
	
}
