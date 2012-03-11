package com.cookbook.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.TabHost;

import com.cookbook.R;

public class MyRecipesActivity extends TabActivity {
	
	protected  ListView lv; 
	
	protected String sortby;
	
	@Override
	 
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        //setContentView(R.layout.my_recipes);
	        
	        Intent sender = getIntent();
	        sortby= sender.getExtras().getString("sortby");
	        
			Intent intent = new Intent(this,BasicListActivity.class);
			// trying to send the recipe name to the new activity
			intent.putExtra("name", "My Recipes");
			intent.putExtra("sortby", sortby); 

			Intent intent2 = new Intent(this,BookmarksActivity.class);
			// trying to send the recipe name to the new activity
			intent2.putExtra("name", "Bookmarks");
			intent2.putExtra("sortby", sortby); 
			
			
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
	    
	    //Important method for action bar
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.main_menu, menu);
	        return true;
	    }
	    
	    //Important method for action bar, item selected listenener
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        	case R.id.search:
	            // app icon in action bar clicked; go home
	            Intent intent1 = new Intent(this, SearchNameActivity.class);
	            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent1);
	            return true;        
	            case android.R.id.home:
	                // app icon in action bar clicked; go home
	                Intent intent2 = new Intent(this, CookbookActivity.class);
	                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                startActivity(intent2);
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
}