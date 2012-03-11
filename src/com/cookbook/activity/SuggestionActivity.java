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

public class SuggestionActivity extends BasicListActivity {

	protected String name = "Suggestion";
	protected String emptyMessage = "No Suggestions yet";
	protected String sourceFile = "bookmarks";
	
	
	
	Resources myResources;
	readFile rd;
	Advisor advisor;
	
	
	
	
	/** =================================  METHODS ==================================*/	
	
	@Override
    public void getItems()
	{
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
        
		
		
		mDbHelper.close();
		
	}
	
	
	
	
}
