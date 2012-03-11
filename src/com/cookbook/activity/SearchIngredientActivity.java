
package com.cookbook.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.cookbook.R;
import com.cookbook.RecipeList;
import com.cookbook.Search;
import com.cookbook.adapter.CookbookDBAdapter;
import com.cookbook.adapter.SeparatedListAdapter;

/**
 * Searches recipes by ingredient name.
 * @author Giulio
 *
 */

public class SearchIngredientActivity extends BasicListActivity
{  
	
	
    // UI elements
    Button mStartSearch;
    
    AutoCompleteTextView text;
    EditText mQueryAppData;
    CookbookDBAdapter mDbHelper;
    Search searchLib;
    String spinresult = "By Name";
    
    
    
    
	 /*this is relating to the way the list items are displayed*/
	   public final static String ITEM_TITLE = "title";  
	   public final static String ITEM_CAPTION = "caption";  
    
   
    
 
    /** EXTRAS */
    String searchType = "name";  // VALUES: "name" or "ingredient"
	
    
     
    
    /** =================================  METHODS ==================================*/
    
    /**
     * ON CREATE
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.search);
        
        mDbHelper = new CookbookDBAdapter(this);
        mDbHelper.open();
        
        searchLib = new Search(mDbHelper);
        
     // Get display items for later interaction
        mStartSearch = (Button) findViewById(R.id.buttonsearch);
        text = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        lv = (ListView) findViewById(R.id.listView1);
        
       
        
        /** Get extras from sender Activity if they are present */ 
	     sender = getIntent();
	     
	     if (sender.hasExtra("searchType"))  searchType = sender.getExtras().getString("searchType");
        
       
	     
	     
	     /** Adds the recipes to the list from the DB */
		  getItems();
		  
		  /** Bind the values to the view layout */
		  bindValuesToView();
		  
		  /** set the click listener function  */
		  setOnItemClick();
  
		  Cursor cur = mDbHelper.fetchAllIngredients();
	        /**
	    	   * adding the list to the recipeArray used to display it
	    	   */
	    	  RECIPES = new String[cur.getCount()];
	    	  for (int i =0; i<cur.getCount();i++){
	    		  RECIPES[i] = cur.getString(2);
	    		  cur.moveToNext();
	    	  }
		  
		  ArrayAdapter<String> adapterAutoComplete = new ArrayAdapter<String>(this,
                  android.R.layout.simple_dropdown_item_1line, RECIPES); 
		  
		  text.setAdapter(adapterAutoComplete);
        
        // Attach actions to buttons
        mStartSearch.setOnClickListener(
            new OnClickListener() {
                public void onClick(View v) {
                    onSearchRequested();
                }
            });
        
        
        mDbHelper.close();
    }
    
    
    @Override
    public void onResume()
	 {
		 super.onResume();
	       
		 mDbHelper.open();
		 
		 // Inflate our UI from its XML layout description.
	        setContentView(R.layout.search);
		 
		// Get display items for later interaction
	        mStartSearch = (Button) findViewById(R.id.buttonsearch);
	        text = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
	        lv = (ListView) findViewById(R.id.listView1);
		 
	        list.clearList();	
		     
		     recipes = new LinkedList<Map<String,Object>>(); 
		     headers = new ArrayList<LinkedList<Map<String,?>>>();
	        
		 
		 onSearchRequested();
		 
		  
		  /** set the click listener function  */
		  setOnItemClick();
		 
		 ArrayAdapter<String> adapterAutoComplete = new ArrayAdapter<String>(this,
                 android.R.layout.simple_dropdown_item_1line, RECIPES); 
		  
		  text.setAdapter(adapterAutoComplete);
		  
		// Attach actions to buttons
	        mStartSearch.setOnClickListener(
	            new OnClickListener() {
	                public void onClick(View v) {
	                    onSearchRequested();
	                }
	            });
		  
		  
	 }

    /** Handle the menu item selections */
    
    
    /**
     * This hook is called when the user signals the desire to start a search.
     * 
     * By overriding this hook we can insert local or context-specific data.
     * 
     * @return Returns true if search launched, false if activity blocks it
     */
    public boolean onSearchRequested() {
        
    	mDbHelper.open();
    		
    	if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            isEmpty = true;
        }
        else {
        list.clearList();
        String rName = text.getText().toString().toLowerCase();

        
       
        
            searchLib.searchByIngredient(list, rName);
            
        	
        
            isEmpty = false;
        
        }
        mDbHelper.close();
  	  
        recipes = new LinkedList<Map<String,Object>>(); 
	     headers = new ArrayList<LinkedList<Map<String,?>>>();
        
        bindValuesToView();
        
        // Returning true indicates that we did launch the search, instead of blocking it.
        return true;
    }

    /**
     * Any application that implements search suggestions based on previous actions (such as
     * recent queries, page/items viewed, etc.) should provide a way for the user to clear the
     * history.  This gives the user a measure of privacy, if they do not wish for their recent
     * searches to be replayed by other users of the device (via suggestions).
     * 
     * This example shows how to clear the search history for apps that use 
     * android.provider.SearchRecentSuggestions.  If you have developed a custom suggestions
     * provider, you'll need to provide a similar API for clearing history.
     * 
     * In this sample app we call this method from a "Clear History" menu item.  You could also 
     * implement the UI in your preferences, or any other logical place in your UI.
     */
    /*private void clearSearchHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, 
                SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE);
        suggestions.clearHistory();
    }*/
       
    /** need it*/
	 String[] RECIPES = new String[]{"lol"};
    
}
