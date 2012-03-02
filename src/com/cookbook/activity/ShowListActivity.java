package com.cookbook.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cookbook.R;
import com.cookbook.RecipeList;
import com.cookbook.readFile;
import com.cookbook.adapter.CookbookDBAdapter;
import com.cookbook.adapter.SeparatedListAdapter;

public class ShowListActivity extends ListActivity {
	
	 /*this is relating to the way the list items are displayed*/
	   public final static String ITEM_TITLE = "title";  
	   public final static String ITEM_CAPTION = "caption";  
	
		Resources myResources;
		readFile rd;
	   
	   /**
	 * Database Adapter
	 */
	protected CookbookDBAdapter mDbHelper;
	
	String[] RECIPES = new String[]{""};
	
	/** List of Recipes */
	protected RecipeList list = new RecipeList();
	
	/** ListView */
	protected  ListView lv;
	
	//flag used for bookmarks function
	protected int flag = 0;
	
    protected String listtype;
    protected String sortby;
	
 //This combines the title and caption to make an element which can be used with the separated list adapter
 public Map<String,?> createItem(String title, String caption) {  
     Map<String,String> item = new HashMap<String,String>();  
     item.put(ITEM_TITLE, title);  
     item.put(ITEM_CAPTION, caption);  
     return item;  
 }
 

	/** Called when the activity is first created. */
 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     //setContentView(R.layout.main);
     
     //Initialise the DB
     mDbHelper = new CookbookDBAdapter(this);
     mDbHelper.open();
     
     Intent sender = getIntent();
     listtype= sender.getExtras().getString("name");
     sortby= sender.getExtras().getString("sortval");
     
	  List<Map<String,?>> recipes = new LinkedList<Map<String,?>>(); 	
     
     if(listtype.equals("My Recipes")){
    	
         list.fetchAllRecipes(mDbHelper);	
         
     }else if(listtype.equals("Bookmarks")){
         myResources = getResources();
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
         
         if(list.size() == 0){
       	  
        RECIPES = new String[1];
       	  RECIPES[0] = "No Bookmarks added yet";
     	  	  recipes.add(createItem("No Bookmarks Added", "")); 
     	  	  flag = 1;
         }
    	 
     }else if(listtype.equals("Filter Results")){
    	 
    	 String param1, param3, param4;
    	 int param2;
    	 float param5;
    	 
         param1= sender.getExtras().getString("param1");
         param2= sender.getExtras().getInt("param2");
         param3= sender.getExtras().getString("param3");
         param4= sender.getExtras().getString("param4");
         param5= sender.getExtras().getFloat("param5");
         
         list.fetchfilterRecipes(mDbHelper ,param1,param2,param3,param4,param5);
         
         if(list.size() == 0){
          	  
        RECIPES = new String[1];
       	  RECIPES[0] = "No Results";
     	  	  recipes.add(createItem("No Results", "")); 
     	  	  flag = 1;
         }
    	 
     }else if(listtype.equals("Suggested Recipes")){
    	 
     }
     

     Log.d("MyDebug", String.valueOf(list.size()));
     
	
     if (flag == 0)
     {  
    	 RECIPES = new String[list.size()];
    	 for (int i =0; i<list.size();i++){
    		 RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
    		 recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
    		 System.out.println(list.getRecipe(i).getName());
    	 }
     }
     
		  //Create our list and custom adapter  
		  SeparatedListAdapter adapter = new SeparatedListAdapter(this); 
		  adapter.addSection(listtype, new SimpleAdapter(this, recipes, R.layout.list_complex, 
				  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));

		//  setContentView(R.layout.show_list);
		 // lv = (ListView)findViewById(R.id.showlistview);		
		lv = getListView();
	      lv.setAdapter(adapter);
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
				Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
				// trying to send the recipe name to the new activity
				recIntent.putExtra("recipeID", list.getRecipe(position - 1).getIdentifier());
				
				startActivity(recIntent);
				    	
			}
		});
 	}
 
 
 	public void setListView(ListView lb){
 		this.lv = lb;
 		lv.setTextFilterEnabled(true);
 	}
 
	public void onResume(){
		
		super.onResume();
		String[] RECIPES = new String[]{""};
		list.clearList();

		  List<Map<String,?>> recipes2 = new LinkedList<Map<String,?>>(); 	
	     
	     if(listtype.equals("My Recipes")){
	    	
	         list.fetchAllRecipes(mDbHelper);	
	         
	     }else if(listtype.equals("Bookmarks")){
	         myResources = getResources();
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
	         
	         if(list.size() == 0){
	       	  
	        RECIPES = new String[1];
	       	  RECIPES[0] = "No Bookmarks added yet";
	     	  	  recipes2.add(createItem("No Bookmarks Added", "")); 
	     	  	  flag = 1;
	         }
	    	 
	     }

	     Log.d("MyDebug", String.valueOf(list.size()));
	     
		
	     if (flag == 0)
	     {  
	    	 RECIPES = new String[list.size()];
	    	 for (int i =0; i<list.size();i++){
	    		 RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
	    		 recipes2.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
	    		 System.out.println(list.getRecipe(i).getName());
	    	 }
	     }
	     
			  //Create our list and custom adapter  
			  SeparatedListAdapter adapter = new SeparatedListAdapter(this); 
			  adapter.addSection(listtype, new SimpleAdapter(this, recipes2, R.layout.list_complex, 
					  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));

			//  setContentView(R.layout.show_list);
			 // lv = (ListView)findViewById(R.id.showlistview);		
			lv = getListView();
		      lv.setAdapter(adapter);
			lv.setTextFilterEnabled(true);
			
			lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
					Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
					// trying to send the recipe name to the new activity
					recIntent.putExtra("recipeID", list.getRecipe(position - 1).getIdentifier());
					
					startActivity(recIntent);
					    	
				}
			});


	}
 
}