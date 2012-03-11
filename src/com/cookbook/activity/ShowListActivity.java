package com.cookbook.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cookbook.R;
import com.cookbook.RecipeList;
import com.cookbook.readFile;
import com.cookbook.adapter.CookbookDBAdapter;
import com.cookbook.adapter.SeparatedListAdapter;

public class ShowListActivity extends FragmentActivity {
	
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
	
	int[] positionhelpers = new int[30];
	
	/** List of Recipes */
	protected RecipeList list = new RecipeList();
	
	/** ListView */
	protected  ListView lv;
	
	ImageButton filter;
	ImageButton search;
	
	//flag used for bookmarks function
	protected int flag = 0;
	
    protected String listtype;
    protected String sortby;
    
	 protected String param1, param3, param4;
	 protected int param2;
	 protected float param5;
	 
	 Intent sender;
	
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
     setContentView(R.layout.show_list);
     
     lv = (ListView) findViewById(R.id.list);
     
     //Initialise the position starter number
  	positionhelpers[0] = 0; 
     
     //Initialise the DB
     mDbHelper = new CookbookDBAdapter(this);
     mDbHelper.open();
     
     sender = getIntent();
     listtype= sender.getExtras().getString("name");
     sortby= sender.getExtras().getString("sortby");
//     sortby = "Alphabetically";
     
     
	  List<Map<String,?>> recipes = new LinkedList<Map<String,?>>(); 

	  SeparatedListAdapter adapter = new SeparatedListAdapter(this); 
	  
  	  String headnames[] = new String[26];
	  
  	  ArrayList<LinkedList<Map<String,?>>> headers = new ArrayList<LinkedList<Map<String,?>>>();
  	  
  	  List<Map<String,?>> heads[] = null;
  	  for(int i = 0; i<27; i++){
  		  headers.add(new LinkedList<Map<String,?>>());
  	  }

     
     if(listtype.equals("My Recipes")){
    	
    	 if(sortby.equals("Alphabetically")) list.fetchAllRecipesByName(mDbHelper);
    	 else if(sortby.equals("By Category")) list.fetchAllRecipes(mDbHelper);    		 
    	 else if(sortby.equals("By Cooking Duration")) list.fetchAllRecipesByDuration(mDbHelper);   		 
    	 else if(sortby.equals("By Occasion")) list.fetchAllRecipesByOccasion(mDbHelper);	 
    	 else if(sortby.equals("By Region")) list.fetchAllRecipesByRegion(mDbHelper);
    	 else if(sortby.equals("By Rating")) list.fetchAllRecipesByRating(mDbHelper);
    		 
    	 
         if(list.size() == 0){
          	  
        RECIPES = new String[1];
       	  RECIPES[0] = "No Recipes added yet";
     	  	  recipes.add(createItem("No Recipes Added", "")); 
     	  	  flag = 1;
     		  adapter.addSection(listtype, new SimpleAdapter(this, recipes, R.layout.list_complex, 
     				  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
         }
         System.out.printf("list length = %d\n", list.size());
         
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
     		  adapter.addSection(listtype, new SimpleAdapter(this, recipes, R.layout.list_complex, 
     				  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
         }else{
             
        	 RECIPES = new String[list.size()];
        	 for (int i =0; i<list.size();i++){
        		 RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
        		 recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
        		 System.out.println(list.getRecipe(i).getName());
        	 }
             }
    	 
     }else if(listtype.equals("Filter Results")){
    	 
    	 
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
     		  adapter.addSection(listtype, new SimpleAdapter(this, recipes, R.layout.list_complex, 
     				  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
         }else{
         
    	 RECIPES = new String[list.size()];
    	 for (int i =0; i<list.size();i++){
    		 RECIPES[i] = list.getRecipe(i).getName();
    		 recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
    		 System.out.println(list.getRecipe(i).getName());
    	 }
         }
    	 
     }else if(listtype.equals("Suggested Recipes")){
    	 
     }
      
     adapter.addSection(listtype, new SimpleAdapter(this, recipes, R.layout.list_complex, 
			  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));

     
     Log.d("MyDebug", String.valueOf(list.size()));
     
     char c = 'A';
     char hold = 'A';
     
     if (flag == 0)
     {  
    	 RECIPES = new String[list.size()];
		 RECIPES[0] = list.getRecipe(0).getName();
		 
		 //Misses one recipe off the end, strange
		 if(sortby.equals("Alphabetically")){
			 c = list.getRecipe(0).getName().charAt(0);
			 hold = c;
			 String empty = "";
			 empty = empty + c;
			 headnames[0] = empty;
			 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType())); 


			 int i=1;
			 int k = 0;
			 for (i=1; i<list.size(); i++){
				 //System.out.printf("%d\n",i);
				 c = list.getRecipe(i-1).getName().charAt(0);
				 hold = list.getRecipe(i).getName().charAt(0);

				 if(c == hold){
					 RECIPES[i] = list.getRecipe(i).getName();
					 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
				 }else{
					 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
							 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
					 k++;
					 positionhelpers[k] = positionhelpers[k-1] + i;
					 empty = "";
					 empty = empty + hold;
					 headnames[k] = empty;
					 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
				 }
			 }
			 positionhelpers[k] = list.size() + k;
			 for(i=0; i<positionhelpers.length; i++){
			 System.out.printf("positionhelpers" + i + " = %d", positionhelpers[i]);
			 }
		 }else if(sortby.equals("By Category")){
			 String type = list.getRecipe(0).getType();
			 String holder = type;
			 headnames[0] = holder;
			 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
			 int i=1;
			 int k = 0;
			 for (i=1; i<list.size(); i++){
				 //System.out.printf("%d\n",i);
				 type = list.getRecipe(i-1).getType();
				 holder = list.getRecipe(i).getType();

				 if(type.equals(holder)){
					 RECIPES[i] = list.getRecipe(i).getName();
					 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
				 }else{
					 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
							 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
					 k++;
					 headnames[k] = holder;
					 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
				 }
			 }			 
		 }else if(sortby.equals("By Cooking Duration")){
			 int type = list.getRecipe(0).getCookingTime();
			 int holder = type;
			 headnames[0] = Integer.toString(holder) + " Minutes";
			 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
			 int i=1;
			 int k = 0;
			 for (i=1; i<list.size(); i++){
				 //System.out.printf("%d\n",i);
				 type = list.getRecipe(i-1).getCookingTime();
				 holder = list.getRecipe(i).getCookingTime();

				 if(type == holder){
					 RECIPES[i] = list.getRecipe(i).getName();
					 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
				 }else{
					 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
							 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
					 k++;
					 headnames[k] = Integer.toString(holder) + " Minutes";
					 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
				 }
			 }			 
		//Not Working Correctly	 
		 }else if(sortby.equals("By Occasion")){
			 String type = list.getRecipe(0).getSeason();
			 if(type.equals("")) type = "Unsorted";
			 String holder = type;
			 headnames[0] = holder;
			 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
			 int i=1;
			 int k = 0;
			 for (i=1; i<list.size(); i++){
				 //System.out.printf("%d\n",i);
				 type = list.getRecipe(i-1).getSeason();
				 holder = list.getRecipe(i).getSeason();

				 if(type.equals(holder)){
					 RECIPES[i] = list.getRecipe(i).getName();
					 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
				 }else{
					 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
							 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
					 k++;
					 if(holder.equals("")) type = "Unsorted";
					 headnames[k] = holder;
					 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
				 }
				 
			 	}
			 }else if(sortby.equals("By Region")){
				 String type = list.getRecipe(0).getRegion();
				 if(type.equals("")) type = "Unsorted";
				 String holder = type;
				 headnames[0] = holder;
				 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
				 int i=1;
				 int k = 0;
				 for (i=1; i<list.size(); i++){
					 //System.out.printf("%d\n",i);
					 type = list.getRecipe(i-1).getRegion();
					 holder = list.getRecipe(i).getRegion();

					 if(type.equals(holder)){
						 RECIPES[i] = list.getRecipe(i).getName();
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
					 }else{
						 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
								 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
						 k++;
						 if(holder.equals("")) type = "Unsorted";
						 headnames[k] = holder;
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
					 }
				 }
			 //needs work, comparisons for floating point aren't working properly
			 }else if(sortby.equals("By Rating")){
				 float type = list.getRecipe(0).getRating();
				 float holder = type;
				 if(Math.abs(type - 0.0) == 0) headnames[0] = "Unrated";
				 headnames[0] = Float.toString(holder) + " Stars";
				 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
				 int i=1;
				 int k = 0;
				 for (i=1; i<list.size(); i++){
					 //System.out.printf("%d\n",i);
					 type = list.getRecipe(i-1).getRating();
					 holder = list.getRecipe(i).getRating();

					 if(Math.abs(type - holder) == 0){
						 RECIPES[i] = list.getRecipe(i).getName();
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
					 }else{
						 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
								 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
						 k++;
						 if(holder == 0.0) headnames[k] = "Unrated";
						 headnames[k] = Float.toString(holder) + " Stars";
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
					 }
				 }	
				 
			 }
    		 

     }
     		  

	      lv.setAdapter(adapter);
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
				Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
				System.out.printf("position = %d\n", position);
				position = position-2;
				for(int g=0; g<(positionhelpers.length-1); g++)
				{
					if(positionhelpers[g]<position && position<=positionhelpers[g+1]) position = position - (g+1);
				}
				// trying to send the recipe name to the new activity
				recIntent.putExtra("recipeID", list.getRecipe(position).getIdentifier());
				recIntent.putExtra("online", false);

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
		
		  List<Map<String,?>> recipes = new LinkedList<Map<String,?>>(); 
		  
	  	  String headnames[] = new String[26];
		
	  	  ArrayList<LinkedList<Map<String,?>>> headers = new ArrayList<LinkedList<Map<String,?>>>();
	  	  
	  	  List<Map<String,?>> heads[] = null;
	  	  for(int i = 0; i<26; i++){
	  		  headers.add(new LinkedList<Map<String,?>>());
	  	  }
	     
		
		  List<Map<String,?>> recipes2 = new LinkedList<Map<String,?>>(); 	

		  SeparatedListAdapter adapter = new SeparatedListAdapter(this); 
		  
	     if(listtype.equals("My Recipes")){
	    	
	    	 if(sortby.equals("Alphabetically")) list.fetchAllRecipesByName(mDbHelper);
	    	 else if(sortby.equals("By Category")) list.fetchAllRecipes(mDbHelper);    		 
	    	 else if(sortby.equals("By Cooking Duration")) list.fetchAllRecipesByDuration(mDbHelper);   		 
	    	 else if(sortby.equals("By Occasion")) list.fetchAllRecipesByOccasion(mDbHelper);	 
	    	 else if(sortby.equals("By Region")) list.fetchAllRecipesByRegion(mDbHelper);
	    	 else if(sortby.equals("By Rating")) list.fetchAllRecipesByRating(mDbHelper);
	    		 	
	         
	         if(list.size() == 0){
	          	  
	             RECIPES = new String[1];
	            	  RECIPES[0] = "No Recipes added yet";
	          	  	  recipes2.add(createItem("No Recipes Added", "")); 
	          	  	  flag = 1;
	          		  adapter.addSection(listtype, new SimpleAdapter(this, recipes2, R.layout.list_complex, 
	          				  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
	              }
	         
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
	         }else{
	         
	    	 RECIPES = new String[list.size()];
	    	 for (int i =0; i<list.size();i++){
	    		 RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
	    		 recipes2.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
	    		 System.out.println(list.getRecipe(i).getName());
	    	 }
	         }
	    	 
	     }else if(listtype.equals("Filter Results")){
	    	 
	         
	         list.fetchfilterRecipes(mDbHelper ,param1,param2,param3,param4,param5);
	         
	         if(list.size() == 0){
	          	  
	        RECIPES = new String[1];
	       	  RECIPES[0] = "No Results";
	     	  	  recipes2.add(createItem("No Results", "")); 
	     	  	  flag = 1;
	     	  	  adapter.addSection(listtype, new SimpleAdapter(this, recipes2, R.layout.list_complex, 
	    				  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
	         }else{
	         
	    	 RECIPES = new String[list.size()];
	    	 for (int i =0; i<list.size();i++){
	    		 RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
	    		 recipes2.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
	    		 System.out.println(list.getRecipe(i).getName());
	    	 }
	         }
	    	 
	     }else if(listtype.equals("Suggested Recipes")){
	    	 
	     }

	     Log.d("MyDebug", String.valueOf(list.size()));
	     
	     adapter.addSection(listtype, new SimpleAdapter(this, recipes, R.layout.list_complex, 
				  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
	     
	     char c = 'A';
	     char hold = 'A';
	     
	     if (flag == 0)
	     {  
	    	 RECIPES = new String[list.size()];
			 RECIPES[0] = list.getRecipe(0).getName();
			 if(sortby.equals("Alphabetically")){
				 c = list.getRecipe(0).getName().charAt(0);
				 hold = c;
				 String empty = "";
				 empty = empty + c;
				 headnames[0] = empty;
				 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType())); 


				 int i=1;
				 int k = 0;
				 for (i=1; i<list.size(); i++){
					 //System.out.printf("%d\n",i);
					 c = list.getRecipe(i-1).getName().charAt(0);
					 hold = list.getRecipe(i).getName().charAt(0);

					 if(c == hold){
						 RECIPES[i] = list.getRecipe(i).getName();
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
					 }else{
						 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
								 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
						 k++;
						 positionhelpers[k] = positionhelpers[k-1] + i;
						 empty = "";
						 empty = empty + hold;
						 headnames[k] = empty;
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
					 }
				 }
				 positionhelpers[k] = list.size() + k;
				 for(i=0; i<positionhelpers.length; i++){
				 System.out.printf("positionhelpers" + i + " = %d", positionhelpers[i]);
				 }		 
	    	 
			 }else if(sortby.equals("By Category")){
				 String type = list.getRecipe(0).getType();
				 String holder = type;
				 headnames[0] = holder;
				 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
				 int i=1;
				 int k = 0;
				 for (i=1; i<list.size(); i++){
					 //System.out.printf("%d\n",i);
					 type = list.getRecipe(i-1).getType();
					 holder = list.getRecipe(i).getType();

					 if(type.equals(holder)){
						 RECIPES[i] = list.getRecipe(i).getName();
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
					 }else{
						 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
								 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
						 k++;
						 headnames[k] = holder;
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
					 }
				 }			 
			 }else if(sortby.equals("By Cooking Duration")){
				 int type = list.getRecipe(0).getCookingTime();
				 int holder = type;
				 headnames[0] = Integer.toString(holder) + " Minutes";
				 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
				 int i=1;
				 int k = 0;
				 for (i=1; i<list.size(); i++){
					 //System.out.printf("%d\n",i);
					 type = list.getRecipe(i-1).getCookingTime();
					 holder = list.getRecipe(i).getCookingTime();

					 if(type == holder){
						 RECIPES[i] = list.getRecipe(i).getName();
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
					 }else{
						 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
								 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
						 k++;
						 headnames[k] = Integer.toString(holder) + " Minutes";
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
					 }
				 }			 
			//Not Working Correctly	 
			 }else if(sortby.equals("By Occasion")){
				 String type = list.getRecipe(0).getSeason();
				 if(type.equals("")) type = "Unsorted";
				 String holder = type;
				 headnames[0] = holder;
				 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
				 int i=1;
				 int k = 0;
				 for (i=1; i<list.size(); i++){
					 //System.out.printf("%d\n",i);
					 type = list.getRecipe(i-1).getSeason();
					 holder = list.getRecipe(i).getSeason();

					 if(type.equals(holder)){
						 RECIPES[i] = list.getRecipe(i).getName();
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
					 }else{
						 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
								 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
						 k++;
						 if(holder.equals("")) type = "Unsorted";
						 headnames[k] = holder;
						 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
					 }
					 
				 	}
				 }else if(sortby.equals("By Region")){
					 String type = list.getRecipe(0).getRegion();
					 if(type.equals("")) type = "Unsorted";
					 String holder = type;
					 headnames[0] = holder;
					 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
					 int i=1;
					 int k = 0;
					 for (i=1; i<list.size(); i++){
						 //System.out.printf("%d\n",i);
						 type = list.getRecipe(i-1).getRegion();
						 holder = list.getRecipe(i).getRegion();

						 if(type.equals(holder)){
							 RECIPES[i] = list.getRecipe(i).getName();
							 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
						 }else{
							 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
									 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
							 k++;
							 if(holder.equals("")) type = "Unsorted";
							 headnames[k] = holder;
							 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
						 }
					 }
				 //needs work, comparisons for floating point aren't working properly
				 }else if(sortby.equals("By Rating")){
					 float type = list.getRecipe(0).getRating();
					 float holder = type;
					 if(Math.abs(type - 0.0) == 0) headnames[0] = "Unrated";
					 headnames[0] = Float.toString(holder) + " Stars";
					 headers.get(0).add(createItem(list.getRecipe(0).getName(), list.getRecipe(0).getType()));
					 int i=1;
					 int k = 0;
					 for (i=1; i<list.size(); i++){
						 //System.out.printf("%d\n",i);
						 type = list.getRecipe(i-1).getRating();
						 holder = list.getRecipe(i).getRating();

						 if(Math.abs(type - holder) == 0){
							 RECIPES[i] = list.getRecipe(i).getName();
							 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 
						 }else{
							 adapter.addSection(headnames[k], new SimpleAdapter(this, headers.get(k), R.layout.list_complex, 
									 new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
							 k++;
							 if(holder == 0.0) headnames[k] = "Unrated";
							 headnames[k] = Float.toString(holder) + " Stars";
							 headers.get(k).add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType()));	 
						 }
					 }	
					 
				 }
			 
			 
	     }
	     

			     setContentView(R.layout.show_list);
			     
			     lv = (ListView) findViewById(R.id.list);	
		      lv.setAdapter(adapter);
			lv.setTextFilterEnabled(true);
			
			lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
					Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
					System.out.printf("position = %d\n", position);
					position = position-2;
					for(int g=0; g<(positionhelpers.length-1); g++){
						if(positionhelpers[g]<position && position<=positionhelpers[g+1]) position = position - (g+1);
					}
					// trying to send the recipe name to the new activity
					recIntent.putExtra("recipeID", list.getRecipe(position).getIdentifier());
					
					startActivity(recIntent);
					    	
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
	
	public RecipeList sortlist(RecipeList list){
		
		
		
		return list;
	}
	
	public class linkedlist{
	    private final LinkedList<Map<String,?>> list = new LinkedList<Map<String,?>>();
	}
 
}