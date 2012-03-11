package com.cookbook.activity;
import com.cookbook.*;
import com.cookbook.adapter.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ViewFlipper;

public class FilterActivity extends FragmentActivity {

    /** Database Adapter */
	private CookbookDBAdapter mDbHelper;
	/** List of Recipes */
	RecipeList list;
	
	/** Array Adapter, needed to update the listview */
	protected ArrayAdapter<String> arrayadp;
	
	
	/*this is relating to the way the list items are displayed*/
	public final static String ITEM_TITLE = "title";  
	public final static String ITEM_CAPTION = "caption";  

	String[] RECIPES = new String[]{" "};
	String Spinval1;
	int cookingval;
	String Spinval2;
	String Spinval3;
	String Spinval4;
	String Spinval5;
	Cursor cursor;
	float ratingval;
	
    //This combines the title and caption to make an element which can be used with the separated list adapter
    public Map<String,?> createItem(String title, String caption) {  
        Map<String,String> item = new HashMap<String,String>();  
        item.put(ITEM_TITLE, title);  
        item.put(ITEM_CAPTION, caption);  
        return item;  
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.filter);
    list = new RecipeList();
    mDbHelper = new CookbookDBAdapter(this);
    mDbHelper.open();

        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.TypeOfRecipe, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.CookingTime, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        
        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.TimeOfYear, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        
        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(
                this, R.array.Country, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        
        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner5 = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(
                this, R.array.Ratings, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter5);
        
        Button submit = (Button) findViewById(R.id.button1);
        final Button cancel = (Button) findViewById(R.id.button2);   
        
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval1 = parent.getItemAtPosition(pos).toString();
                if(Spinval1.equals("All")) Spinval1 = null;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval2 = parent.getItemAtPosition(pos).toString();
                if(Spinval2.equals("All")) cookingval = 0;
                if(Spinval2.equals("Under 10 mins")) cookingval = 10;
                else if(Spinval2.equals("Under 20 mins")) cookingval = 20;
                else if(Spinval2.equals("Under 30 mins")) cookingval = 30;
                else if(Spinval2.equals("Under 1 hour")) cookingval = 60;
                else if(Spinval2.equals("Under 2 hours")) cookingval = 120;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval3 = parent.getItemAtPosition(pos).toString();
                if(Spinval3.equals("All")) Spinval3 = null;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval4 = parent.getItemAtPosition(pos).toString();
                if(Spinval4.equals("All")) Spinval4 = null;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval5 = parent.getItemAtPosition(pos).toString();
                if(Spinval5.equals("All"))ratingval = 0;
                else if(Spinval5.equals("1 Star")) ratingval = 1;
                else if(Spinval5.equals("2 Stars")) ratingval = 2;
                else if(Spinval5.equals("3 Stars")) ratingval = 3;
                else if(Spinval5.equals("4 Stars")) ratingval = 4;
                else if(Spinval5.equals("5 Stars")) ratingval = 5;

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

           finish();

            }
        });
        
        submit.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
            	Intent intent = new Intent(view.getContext(), BasicListActivity.class);
    			intent.putExtra("name", "Filter Results");
    			intent.putExtra("sortby", "recipeName"); 
          		intent.putExtra("mealType", Spinval1);
          		intent.putExtra("cookingDuration", cookingval);
          		intent.putExtra("season", Spinval3);
          		intent.putExtra("country", Spinval4);
          		intent.putExtra("rating", ratingval);
            	startActivityForResult(intent, 0);
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