package com.cookbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.cookbook.R;
import com.cookbook.adapter.CookbookDBAdapter;

public class SortActivity extends FragmentActivity {
	private static String sortby = "recipeName";
	private static String dbtype = "local";
	private static String name = "Recipes";
	protected Intent reciever;
	protected Intent sender;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort);
        
		/** Get extras from sender Activity if they are present */ 
	    reciever = getIntent();
		if (reciever.hasExtra("name")) sortby = reciever.getExtras().getString("dbtype");
	    if (reciever.hasExtra("dbtype")) name = reciever.getExtras().getString("name");
        
        Spinner sorter = (Spinner) findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sorter.setAdapter(adapter);
        sorter.setOnItemSelectedListener(new sortSelectedListener());
        
        Button submit = (Button) findViewById(R.id.submitsort);

        submit.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		Intent intent = new Intent(view.getContext(), SortedListActivity.class);
        		intent.putExtra("name", "My Recipes");
        		intent.putExtra("sortby", sortby); 
        		startActivity(intent);
        	}
        });
        
	}
	

	public class sortSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	setSortSetting(parent.getItemAtPosition(pos).toString());
	    }

	    public void onNothingSelected(AdapterView parent) {}
	}
	
	
	// Getter and setter methods for measurementSetting
	private void setSortSetting(String setting) { 
		sortby = setting;
		if(sortby.equals("Alphabetically")) sortby = "recipeName";
		else if(sortby.equals("By Category")) sortby = "mealType";
		else if(sortby.equals("By Cooking Duration")) sortby = "duration";
		else if(sortby.equals("By Occasion")) sortby = "timeOfYear";
		else if(sortby.equals("By Region")) sortby = "region";
		else if(sortby.equals("By Rating")) sortby = "rating";

	}
	private String getSortSetting() { return sortby; }
	
	//Important method for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_list_menu, menu);
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
        	case R.id.filter:
                // app icon in action bar clicked; go home
                Intent intent2 = new Intent(this, FilterActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;  
        	case R.id.sorter:
                // app icon in action bar clicked; go home
                Intent intent3 = new Intent(this, CookbookActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                return true;  
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent4 = new Intent(this, CookbookActivity.class);
                intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
