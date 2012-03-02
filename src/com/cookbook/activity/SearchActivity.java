

package com.cookbook.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class SearchActivity extends Activity
{  
	
	
    // UI elements
    Button mStartSearch;
    Spinner mMenuMode;
    Spinner searchby;
    AutoCompleteTextView text;
    EditText mQueryAppData;
    CookbookDBAdapter mDbHelper;
    Search searchLib;
    String spinresult = "By Name";
    
    
	 /*this is relating to the way the list items are displayed*/
	   public final static String ITEM_TITLE = "title";  
	   public final static String ITEM_CAPTION = "caption";  
    RecipeList list;
    ListView listvw;
    
    /** Array Adapter, needed to update the listview */
	protected ArrayAdapter<String> arrayadp;
    
	
    //This combines the title and caption to make an element which can be used with the separated list adapter
    public Map<String,?> createItem(String title, String caption) {  
        Map<String,String> item = new HashMap<String,String>();  
        item.put(ITEM_TITLE, title);  
        item.put(ITEM_CAPTION, caption);  
        return item;  
    }
     
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
        listvw = (ListView) findViewById(R.id.listView1);
        searchby = (Spinner) findViewById(R.id.searchby);
        ArrayAdapter<CharSequence> adaptersearchby = ArrayAdapter.createFromResource(
                this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adaptersearchby.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchby.setAdapter(adaptersearchby);
        
        searchby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                 spinresult = parent.getItemAtPosition(pos).toString();
                 System.out.printf("%s", spinresult);

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        
        list = new RecipeList();
        list.fetchAllRecipes(mDbHelper);
        
    	  List<Map<String,?>> recipes = new LinkedList<Map<String,?>>(); 
        
        /**
    	   * adding the list to the recipeArray used to display it
    	   */
    	  RECIPES = new String[list.size()];
    	  for (int i =0; i<list.size();i++){
    		  RECIPES[i] = list.getRecipe(i).getName();
      	  	  recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
    		  System.out.println(list.getRecipe(i).getName());
    	  }
        
    	  //Create our list and custom adapter  
    	  SeparatedListAdapter adapter = new SeparatedListAdapter(this); 
    	  adapter.addSection("All Recipes", new SimpleAdapter(this, recipes, R.layout.list_complex, 
    			  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
    	  
          ArrayAdapter<String> adapterAutoComplete = new ArrayAdapter<String>(this,
                  android.R.layout.simple_dropdown_item_1line, RECIPES); 
         
         
          text.setAdapter(adapterAutoComplete);

      	  listvw.setAdapter(adapter);

      	  listvw.setTextFilterEnabled(true);
        
    	  /**
    	   * Onclik show the info about the Recipe on a popup message
    	   */
    	  listvw.setOnItemClickListener(new OnItemClickListener() {
    	    public void onItemClick(AdapterView<?> parent, View view,
    	        int position, long id) {
    	      // When clicked, show a toast with the TextView text
    	    	Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
    	    	// trying to send the recipe name to the new activity
    	    	recIntent.putExtra("recipeID",list.getRecipe(position-1).getIdentifier());
    	    	startActivity(recIntent);
    	      }
            }); 
  
        //TODO Populate items
       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                            this, R.array.search_menuModes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMenuMode.setAdapter(adapter);
        */
        
        // Create listener for the menu mode dropdown.  We use this to demonstrate control
        // of the default keys handler in every Activity.  More typically, you will simply set
        // the default key mode in your activity's onCreate() handler.
        /*mMenuMode.setOnItemSelectedListener(
            new OnItemSelectedListener() {
                public void onItemSelected(
                        AdapterView<?> parent, View view, int position, long id) {
                    if (position == MENUMODE_TYPE_TO_SEARCH) {
                        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
                    } else {
                        setDefaultKeyMode(DEFAULT_KEYS_DISABLE);
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    setDefaultKeyMode(DEFAULT_KEYS_DISABLE);
                }
            });
        */
        
        // Attach actions to buttons
        mStartSearch.setOnClickListener(
            new OnClickListener() {
                public void onClick(View v) {
                    onSearchRequested();
                }
            });
    }

    /** Handle the menu item selections */
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 0:
            switch (mMenuMode.getSelectedItemPosition()) {
            case MENUMODE_SEARCH_KEY:
                new AlertDialog.Builder(this)
                    .setMessage("To invoke search, dismiss this dialog and press the search key" +
                                " (F5 on the simulator).")
                    .setPositiveButton("OK", null)
                    .show();
                break;
                
            case MENUMODE_MENU_ITEM:
                onSearchRequested();
                break;
                
            case MENUMODE_TYPE_TO_SEARCH:
                new AlertDialog.Builder(this)
                    .setMessage("To invoke search, dismiss this dialog and start typing.")
                    .setPositiveButton("OK", null)
                    .show();
                break;
                
            case MENUMODE_DISABLED:
                new AlertDialog.Builder(this)
                    .setMessage("You have disabled search.")
                    .setPositiveButton("OK", null)
                    .show();
                break;
            }
            break;
        case 1:
            clearSearchHistory();
            break;
        }
    
         return super.onOptionsItemSelected(item);
    }*/
    
    /**
     * This hook is called when the user signals the desire to start a search.
     * 
     * By overriding this hook we can insert local or context-specific data.
     * 
     * @return Returns true if search launched, false if activity blocks it
     */
    //@Override
    public boolean onSearchRequested() {
        // If your application absolutely must disable search, do it here.
        if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
        }
        else {
        list.clearList();
        String rName = text.getText().toString().toLowerCase();

        
        if(spinresult.equals("By Name")){
        
            searchLib.searchByName(list, rName);
            searchLib.searchByPatternName(list, rName);
            searchLib.searchByName_SubString(list, rName);
        	
        }
        else if(spinresult.equals("By Category")){

            searchLib.searchByCategory(list, rName);
            searchLib.searchByPatternCategory(list, rName);
            searchLib.searchByCategory_SubString(list, rName);
        	
        }else if(spinresult.equals("By Cooking Duration")){
        	
            searchLib.searchByDuration(list, rName);
            searchLib.searchByPatternDuration(list, rName);
            searchLib.searchByDuration_SubString(list, rName);
        	
        }else if(spinresult.equals("By Occasion")){
            
        	searchLib.searchByDuration(list, rName);
            searchLib.searchByPatternDuration(list, rName);
            searchLib.searchByDuration_SubString(list, rName);
        	
        }else if(spinresult.equals("By Region")){
        	
            searchLib.searchByRegion(list, rName);
            searchLib.searchByPatternRegion(list, rName);
            searchLib.searchByRegion_SubString(list, rName);
            
        }else if(spinresult.equals("By Rating")){
        	
            searchLib.searchByRating(list, rName);
            searchLib.searchByPatternRating(list, rName);
            searchLib.searchByRating_SubString(list, rName);
            
        }
        
        }
        
  	  List<Map<String,?>> recipes2 = new LinkedList<Map<String,?>>(); 
        
        if (list.size()>0){
        	RECIPES = new String[list.size()];
      	  for (int i =0; i<list.size();i++){
      		  RECIPES[i] = list.getRecipe(i).getName();
  		      recipes2.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
      		  System.out.println(list.getRecipe(i).getName());
      	  }    	
        }
        else
        {
        	RECIPES = new String[1];
        	recipes2.add(createItem("No Results Found","")); 
        	RECIPES[0] = "No results found";
        }  
      	
  	  //Create our list and custom adapter  
  	  SeparatedListAdapter adapter2 = new SeparatedListAdapter(this); 
  	  adapter2.addSection("Search Results", new SimpleAdapter(this, recipes2, R.layout.list_complex, 
  			  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
  	  
        
      //  arrayadp = new ArrayAdapter<String>(this, R.layout.list_item, RECIPES);
        
    	  listvw.setAdapter(adapter2);
    	//  arrayadp.notifyDataSetChanged();
        
    	  /**
    	   * Onclik show the info about the Recipe on a popup message
    	   */
    	  listvw.setOnItemClickListener(new OnItemClickListener() {
    	    public void onItemClick(AdapterView<?> parent, View view,
    	        int position, long id) {
    	      // When clicked, show a toast with the TextView text
    	    	Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
    	    	// trying to send the recipe name to the new activity
    	    	recIntent.putExtra("recipeID",list.getRecipe(position-1).getIdentifier());
    	    	startActivity(recIntent);
    	      }
            }); 
    	  
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
