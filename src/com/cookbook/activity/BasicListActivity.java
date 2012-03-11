package com.cookbook.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.*;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.*;
import com.cookbook.adapter.CookbookDBAdapter;
import com.cookbook.adapter.MainImageAdapter;
import com.cookbook.adapter.RecipeListAdapter;
import com.cookbook.adapter.SeparatedListAdapter;
import com.cookbook.facebook.BaseRequestListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
/**
 * OVERVIEW:
 * This class is an activity used to display basic lists of recipes.
 * You can submit paramters to be used in activity initliazitations via extras.
 * Inehrit this classe to create child subclasses
 * @author Giulio
 *
 */

public class BasicListActivity extends FragmentActivity{

	
	 /** Database Adapter */
	protected CookbookDBAdapter mDbHelper;
	/** List of Recipes */
	protected RecipeList list = new RecipeList();
	/** ListView */
	protected  ListView lv;
	/** SeparatedListADapter */
	protected SeparatedListAdapter adapter;
	
	protected Boolean isEmpty = false;
	
	/**
	 * Arguments Received from Sender Activities
	 */
	protected String mealType = null;
	protected int cookingDuration = 0;
	protected String season = null;
	protected String country = null;
	protected float rating = 0;
	protected String friendId = "0";
	
	protected Intent sender;
	
	protected String emptyMessage = "No results";
	protected String name = "Recipes"; // name of the list. used like an header that's it
	protected String sortby = "recipeName"; //sorting mode VALUES: "recipeName" "method" 
	// 	"mealType" "duration" "timeOfYear" "region" "rating"
	
	/** this is relating to the way the list items are displayed */
	public final static String ITEM_TITLE = "title";  
	public final static String ITEM_CAPTION = "caption";  
	public final static String ITEM_RATING = "rating";  
	
	/**
	 * DATA Structure used by the adapter to store informations to be displayed in list items
	 * Every map stores all the info about a recipe to be displayed in a list_complex layout
	 * LinkedList is a list of those maps. Headers stores the headers for separatedListsadapter.
	 */
	LinkedList<Map<String, Object>> recipes = new LinkedList<Map<String,Object>>(); 
	ArrayList<LinkedList<Map<String,?>>> headers = new ArrayList<LinkedList<Map<String,?>>>();
	
	
	 
	 
	
	
	/** ==================================== MEHODS =======================================*/
	
	public Map<String,Object> createItem(String title, String caption,float rating) {  
	     Map<String,Object> item = new HashMap<String,Object>();  
	     item.put(ITEM_TITLE, title);  
	     item.put(ITEM_CAPTION, caption);
	     item.put(ITEM_RATING, rating);
	     return item;  
	 }
	
	
	/**
	 *  Called when the activity is first created. 
	 *  */
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	     
		 super.onCreate(savedInstanceState);
	     
		 setContentView(R.layout.show_list);
	     
	     lv = (ListView) findViewById(R.id.list);
	     
	     
	     
	    /** Get extras from sender Activity if they are present */ 
	     sender = getIntent();
	  
	   if (sender.hasExtra("name"))  name = sender.getExtras().getString("name");
	   if (sender.hasExtra("sortby"))  sortby= sender.getExtras().getString("sortby");
	     
	   if (sender.hasExtra("mealType")) mealType= sender.getExtras().getString("mealType");
	   if (sender.hasExtra("cookingDuration")) cookingDuration= sender.getExtras().getInt("cookingDuration");
	   if (sender.hasExtra("season")) season= sender.getExtras().getString("season");
	   if (sender.hasExtra("country")) country = sender.getExtras().getString("country");
	   if (sender.hasExtra("rating")) rating= sender.getExtras().getFloat("rating");
	   if (sender.hasExtra("to")) friendId = sender.getExtras().getString("to");
	     
	 
	     


		  /** Adds the recipes to the list from the DB */
		  getItems();
		  
		  /** Bind the values to the view layout */
		  bindValuesToView();
		  
		  /** set the click listener function  */
		  setOnItemClick();
		  
		  
		  
		  
	 }
	 
	 
	 
	
	 public void onResume()
	 {
		 
		 super.onResume();
		 
		 setContentView(R.layout.show_list);
	     lv = (ListView) findViewById(R.id.list);
	     list.clearList();	
	     
	     recipes = new LinkedList<Map<String,Object>>(); 
	     headers = new ArrayList<LinkedList<Map<String,?>>>();

		  /** Adds the recipes to the list from the DB */
		  getItems();
		  
		  /** Bind the values to the view layout */
		  bindValuesToView();
		  
		  /** set the click listener function  */
		  setOnItemClick();
		  
		  
		  
		 
	 }
	 
	 
	 /**
	  * This methods gets all the recipes from the DB using the paramters which the class 
	  * got from the sender Intent
	  * Override it in child classes if you need.
	  */
	 public void getItems()
	 {
		 /** Initialise the DB */
	     mDbHelper = new CookbookDBAdapter(this);
	     mDbHelper.open();
		 
		 
		 list.fetchfilterRecipesSorted(mDbHelper, mealType, cookingDuration, season, country, rating,sortby); 
		 
		 mDbHelper.close();
	 }
	 
	 
	 
	 /**
	  * Bind the values to the selected layout view
	  * Override it in child classes if you need.
	  */
	 public void bindValuesToView()
	 {
		 
		 adapter = new SeparatedListAdapter(this);
		 
		 if(list.size() == 0)
		 {
         	  
		        
		     	  	  recipes.add(createItem(emptyMessage, "",0)); 
		     	  	 
		     	  	adapter.addSection(name, new RecipeListAdapter(recipes, this));
		 }
		 else
		 {
		          
		  for (int i =0; i<list.size();i++)
		  {
			  recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType(),list.getRecipe(i).getRating())); 
			  System.out.println(list.getRecipe(i).getName());
		  }
		 isEmpty = false;
		  adapter.addSection(name, new RecipeListAdapter(recipes, this));
		 }
		 
		lv.setAdapter(adapter);
		lv.setTextFilterEnabled(true);
		 
	 }
	 
	 
	 
	 /**
	  * 
	  * Finalize the listview initialization and set the onItemClickListener.
	  * Override it in child classes if you need.
	  */
	 public void setOnItemClick()
	 {
		  
			
			lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				
				if (!isEmpty())
				{
					Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);

					String recname  = ((TextView)view.findViewById(R.id.list_recipe_title)).getText().toString();
					mDbHelper.open();
					RecipeList list2 = new RecipeList();
					list2.fetchByName(mDbHelper, recname);
					Recipe rec = list2.getRecipe(0);
					recIntent.putExtra("recipeID", rec.getIdentifier());
					mDbHelper.close();
					
					startActivity(recIntent);
				}
					    	
				}
			});
		 
	 }
	 
	 /**
	  * check if the list is empty
	  * @return
	  */
	 public boolean isEmpty()
	 {
		 
		  return isEmpty;
	 }
	
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
	                Intent intent3 = new Intent(this, SortActivity.class);
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
