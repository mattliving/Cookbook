package com.cookbook.activity;

import com.cookbook.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.cookbook.adapter.DiscoverImageAdapter;



public class DiscoverActivity extends FragmentActivity {
	
	protected String sortby;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover_recipes);
        
        Intent sender = getIntent();
        sortby= sender.getExtras().getString("sortby");
        
        GridView gridview = (GridView) findViewById(R.id.discover_gridview);
        gridview.setAdapter(new DiscoverImageAdapter(this));
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent intent;
            	switch (position) {
	            	case 0:
	            		intent = new Intent(v.getContext(), AllRecipesActivity.class);
	                	startActivity(intent);
	                	break;
	            	case 1:
	            		intent = new Intent(v.getContext(), SearchIngredientActivity.class);
	                	startActivityForResult(intent, 1);
	                	break;
	            	case 2:
	            		intent = new Intent(v.getContext(), BasicListActivity.class);
	        			intent.putExtra("name", "My Recipes");
	        			intent.putExtra("sortby", sortby); 
	                	startActivityForResult(intent, 2);
	                	break;
	            	default:
	            		break;
            	}
            }
        });
	}
	
    //Important method for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    //Important method for action bar, item selected listenener
    @Override
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
