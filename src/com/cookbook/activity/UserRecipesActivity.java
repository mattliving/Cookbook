package com.cookbook.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.cookbook.R;
import com.cookbook.RecipeList;
import com.cookbook.readFile;
import com.cookbook.adapter.CookbookDBAdapter;


import android.app.Activity;
import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

//Trying commit and removing cookbook folder

public class UserRecipesActivity extends BasicListActivity {
    
	protected String name = "My Recipes";
	protected String emptyMessage = "No Recipes Added yet";
	protected String sourceFile = "userrecipes";
    
	
	protected Resources myResources;
	protected readFile rd;
	
	
	
	/** =================================  METHODS ==================================*/
	
	
	/**
	 * Get the ids from the "userrecipes" file in res->raw and add their correspondent recipe
	 *  to the list. Any other file could be used if its name is passed via an extra.
	 */
	@Override
	public void getItems()
	{
		myResources = getResources();
		
		if (sender.hasExtra("sourceFile"))	sourceFile = sender.getExtras().getString("sourceFile");
			
		/** Initialise the DB */
	     mDbHelper = new CookbookDBAdapter(this);
	     mDbHelper.open();
		
		FileInputStream fos;
        try {
			fos = openFileInput(sourceFile);
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
		mDbHelper.close();
	}
	
	public void sort(){
		
	
	
	}
	
	
}