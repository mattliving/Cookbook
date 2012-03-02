package com.cookbook.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;

import com.cookbook.R;

public class MyRecipesActivity extends TabActivity {
	
	protected  ListView lv; 
	
	@Override
	 
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        //setContentView(R.layout.my_recipes);
	        
			Intent intent = new Intent(this,ShowListActivity.class);
			// trying to send the recipe name to the new activity
			intent.putExtra("name", "My Recipes");
			intent.putExtra("sortval", ""); 

			Intent intent2 = new Intent(this,ShowListActivity.class);
			// trying to send the recipe name to the new activity
			intent2.putExtra("name", "Bookmarks");
			intent2.putExtra("sortval", ""); 
			
			
	        final TabHost tabHost = getTabHost();

	        tabHost.addTab(tabHost.newTabSpec("My Recipes")	        		
	        		.setIndicator("My Recipes",getResources().getDrawable(R.drawable.ic_tab_myrecipes))
	        		.setContent(intent));

	        tabHost.addTab(tabHost.newTabSpec("Bookmarks")
	                .setIndicator("Bookmarks",getResources().getDrawable(R.drawable.ic_tab_bookmarks))
	                .setContent(intent2));

	        // This tab sets the intent flag so that it is recreated each time
	        // the tab is clicked.
	        tabHost.addTab(tabHost.newTabSpec("Add Recipe")
	                .setIndicator("Add Recipe",getResources().getDrawable(R.drawable.ic_tab_addrecipe_grey))
	                .setContent(new Intent(this, AddRecipeActivity.class)));
	    }
	 
	    public void setListView(ListView lb){
	    	this.lv = lb;
	    	lv.setTextFilterEnabled(true);
	    }
}