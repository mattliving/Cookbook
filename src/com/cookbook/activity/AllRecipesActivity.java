package com.cookbook.activity;

import java.util.HashMap;
import java.util.Map;

import com.cookbook.R;
import com.cookbook.RecipeList;
import com.cookbook.adapter.CookbookDBAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class AllRecipesActivity extends FragmentActivity{

	
	String FIRST_LEVEL[] = { "All","Type","Season", "Region"};
	
	String TYPE[] = { "All" ,"Breakfast","Lunch","Dinner","Dessert","Snack"};
	//Not final
	String SEASON[] = {"All", "Spring","Summer","Autumn","Winter","Christmas","Halloween"};
	
	String REGION[] = {"China","England","France","Germany","Italy","Ireland","Japan","Scotland","Spain","United States","Wales"};
	
	
	boolean isFirstLevel = true;
	int secondLevel = 0; // 0:All 1:Type 2:Season 3:Region
	
	/*this is relating to the way the list items are displayed*/
	   public final static String ITEM_TITLE = "title";  
	   public final static String ITEM_CAPTION = "caption";  
 RecipeList list;
 ListView listvw;
 
 Intent sender;
 
 
 
	
 //This combines the title and caption to make an element which can be used with the separated list adapter
 public Map<String,?> createItem(String title, String caption) {  
     Map<String,String> item = new HashMap<String,String>();  
     item.put(ITEM_TITLE, title);  
     item.put(ITEM_CAPTION, caption);  
     return item; }
	


public void onCreate(Bundle savedInstanceState) {
  	
    super.onCreate(savedInstanceState);
  	
    setContentView(R.layout.show_list);
    sender = getIntent();
    	
    listvw = (ListView) findViewById(R.id.list);
    
    if (sender.getExtras() !=null)
    {
    String listtype= sender.getExtras().getString("type");
  	
    isFirstLevel = false;
    
    if (listtype.equalsIgnoreCase("TYPE"))
  	{
  		listvw.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, TYPE));
  		secondLevel = 1;
  	}
  	else if (listtype.equalsIgnoreCase("SEASON"))
  	{
  		listvw.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, SEASON));
  		secondLevel = 2;
  	}
  	else if (listtype.equalsIgnoreCase("REGION"))
  	{
  		listvw.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,REGION));
  		secondLevel = 3;
  	}
  	
    }
    else 
    {
  		listvw.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, FIRST_LEVEL));
  		isFirstLevel = true;
  	}
  	
   
  	
    
  
  	
    
  	


  	

 
listvw.setTextFilterEnabled(true);
  	

  	
listvw.setOnItemClickListener(new OnItemClickListener() {
  	
public void onItemClick(AdapterView<?> parent, View view,
  	
  int position, long id) {
  	
	if(isFirstLevel){
	if(position>0)
		{
		Intent recIntent = new Intent(view.getContext(),AllRecipesActivity.class);
		recIntent.putExtra("type",FIRST_LEVEL[position]);
		startActivity(recIntent);
		}
	else
		{
		Intent recIntent = new Intent(view.getContext(),OnlineListActivity.class);
		recIntent.putExtra("name","All Recipes");
		recIntent.putExtra("sortby", "duration");
		//recIntent.putExtra("mealType", "null");
		startActivity(recIntent);
		
		}
	
	}
	else
	{
		if (secondLevel ==1)
		{
			Intent recIntent = new Intent(view.getContext(),BasicListActivity.class);
			recIntent.putExtra("name","All Recipes");
			recIntent.putExtra("sortby", "mealType");
			recIntent.putExtra("mealType", TYPE[position]);
			startActivity(recIntent);
		}
		if (secondLevel ==2)
		{
			Intent recIntent = new Intent(view.getContext(),BasicListActivity.class);
			recIntent.putExtra("name","All Recipes");
			recIntent.putExtra("sortby", "mealType");
			recIntent.putExtra("season", SEASON[position]);
			startActivity(recIntent);
		}
		if (secondLevel ==3)
		{
			Intent recIntent = new Intent(view.getContext(),BasicListActivity.class);
			recIntent.putExtra("name","All Recipes");
			recIntent.putExtra("sortby", "mealType");
			recIntent.putExtra("country", REGION[position]);
			startActivity(recIntent);
		}
		
	}
	
	
          
  	
  }
  	
});


}

};
