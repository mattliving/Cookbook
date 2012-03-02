package com.cookbook.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cookbook.Advisor;
import com.cookbook.R;
import com.cookbook.RecipeList;
import com.cookbook.readFile;
import com.cookbook.adapter.CookbookDBAdapter;

public class SuggestionActivity extends ListActivity {

	
	Resources myResources;
	readFile rd;
	CookbookDBAdapter mDbHelper;
	Advisor advisor;
	
	/** List of Recipes */
	protected RecipeList list = new RecipeList();
	/** Array Adapter, needed to update the listview */
	protected ArrayAdapter<String> arrayadp;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        //Initialise the DB
        mDbHelper = new CookbookDBAdapter(this);
        mDbHelper.open();
        
        myResources = getResources();
        advisor = new Advisor(mDbHelper);
        
        
        /*
         * Add the database entries to the list
         */
        FileInputStream fos;
        try {
			fos = openFileInput("bookmarks");
			rd = new readFile();
	        list.fetchFromIDs(rd.readIDs(fos),mDbHelper);
	        try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	/**
	 * calculates recipes suggested based on bookmarks
	 */
      list = advisor.suggestFromBookmarks(list);
        
        /**
  	   * adding the list to the recipeArray used to display it
  	   */
        
      if (list.size()>0)
      {  
  	  RECIPES = new String[list.size()];
  	  for (int i =0; i<list.size();i++){
  		  RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
  		  System.out.println(list.getRecipe(i).getName());
  	  }
      }
      else
      {
    	  RECIPES = new String[1];
    	  RECIPES[0] = "No Suggestions yet";
      }
        
     // list_item is in /res/layout/ should be created
      arrayadp = new ArrayAdapter<String>(this, R.layout.list_item, RECIPES);
  	  setListAdapter(arrayadp);



  	  final ListView lv = getListView();
  	  lv.setTextFilterEnabled(true);

        
        

  	/**
	   * Onclik show the info about the Recipe on a popup message
	   */
	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	    	Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
	    	// trying to send the recipe name to the new activity
	    	recIntent.putExtra("recipeName",list.getRecipe(position).getName());
	    	startActivity(recIntent);
	      }
        });
    }
	
	/**
	 * Updates the content when tab is selected
	 */
	public void onResume(){
		
		super.onResume();
		
		FileInputStream fos;
		list.clearList();
        try {
			fos = openFileInput("bookmarks");
			rd = new readFile();
	        list.fetchFromIDs(rd.readIDs(fos),mDbHelper);
	        try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		/**
		 * calculates recipes suggested based on bookmarks
		 */
	      list = advisor.suggestFromBookmarks(list);
        
        /**
  	   * adding the list to the recipeArray used to display it
  	   */
        
      if (list.size()>0)
      {  
  	  RECIPES = new String[list.size()];
  	  for (int i =0; i<list.size();i++){
  		  RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
  		  System.out.println(list.getRecipe(i).getName());
  	  }
      }
      else
      {
    	  RECIPES = new String[1];
    	  RECIPES[0] = "No Suggestions yet";
      }
        
     // list_item is in /res/layout/ should be created
      arrayadp = new ArrayAdapter<String>(this, R.layout.list_item, RECIPES);
  	  setListAdapter(arrayadp);



  	  final ListView lv = getListView();
  	  lv.setTextFilterEnabled(true);

	}
	
	
	/** need it*/
	 String[] RECIPES = new String[]{"lol"};
	
}
