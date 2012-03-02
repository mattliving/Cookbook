package com.cookbook.activity;

import com.cookbook.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.cookbook.adapter.DiscoverImageAdapter;

public class DiscoverActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover_recipes);
        
        GridView gridview = (GridView) findViewById(R.id.discover_gridview);
        gridview.setAdapter(new DiscoverImageAdapter(this));
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent intent;
            	switch (position) {
	            	case 0:
	            		intent = new Intent(v.getContext(), SearchActivity.class);
	                	startActivityForResult(intent, 0);
	                	break;
	            	case 1:
	            		intent = new Intent(v.getContext(), SuggestionActivity.class);
	                	startActivityForResult(intent, 1);
	                	break;
	            	case 2:
	            		intent = new Intent(v.getContext(), FilterActivity.class);
	                	startActivityForResult(intent, 2);
	                	break;
	            	default:
	            		break;
            	}
            }
        });
	}
}
