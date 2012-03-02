package com.cookbook.activity;

import java.io.InputStream;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.R;
import com.cookbook.RecipeList;
import com.cookbook.readFile;


/**
 * An Example of using the ReadFile class to read data from a file. In this case we parse the data
 * as Recipes.
 * @author Giulio
 *
 */
public class ExampleRawFileActivity extends ListActivity {
    

	protected TextView textView;
    readFile rd;
    InputStream fos;
    String FILENAME = "./res/raw/recipes";
    Resources myResources;
    RecipeList list;
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  myResources = getResources();


	  /**
	   * Opening the file in the project folder "res/raw/recipes" and generating the input stream
	   */
		fos = myResources.openRawResource(R.raw.recipes);
		rd = new readFile();
	  list = rd.readRecipeData(fos);


	  /**
	   * adding the list to the recipeArray used to display it
	   */
	  RECIPES = new String[list.size()];
	  for (int i =0; i<list.size();i++){
		  RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
	  }


	  // list_item is in /res/layout/ should be created
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RECIPES));



	  final ListView lv = getListView();
	  lv.setTextFilterEnabled(true);


	  /**
	   * Onclik show the info about the Recipe on a popup message
	   */
	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	      // When clicked, show a toast with the TextView text
	      Toast.makeText(getApplicationContext(), 
	    "Ingredients: "+list.getRecipe(position).getIngredients()+"\nPreparation: "+list.getRecipe(position).getPreparation()
	    +"\nType: "+list.getRecipe(position).getType()+"\nRegion: "+list.getRecipe(position).getRegion(),
	          Toast.LENGTH_SHORT).show();


	    //dididi


	    }
	  });
	}

	/** need it*/
	 String[] RECIPES = new String[]{"lol"};




}