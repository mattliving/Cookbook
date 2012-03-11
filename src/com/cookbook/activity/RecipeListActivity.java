package com.cookbook.activity;
	  	
import android.app.ListActivity;	  	
import android.content.Intent;
	  	
import android.os.Bundle;
	  	
import android.util.Log;
	  	
import android.view.View;
	  	
import android.view.View.OnClickListener;
	  	
import android.widget.AdapterView;
	  	
import android.widget.AdapterView.OnItemClickListener;
	  	
import android.widget.ArrayAdapter;
	  	
import android.widget.ImageButton;
	  	
import android.widget.ListView;
	  	

	  	
import com.cookbook.R;
	  	
import com.cookbook.RecipeList;
	  	
import com.cookbook.adapter.CookbookDBAdapter;
	  	

	  	
public class RecipeListActivity extends ListActivity {
	  	
    
	  	
 /**
22	  	
-   * Database Adapter
23	  	
-   */
	  	
protected CookbookDBAdapter mDbHelper;
	  	
  
	  	
  /** List of Recipes */
	  	
protected RecipeList list = new RecipeList();
	  	
  
	  	
  /** ListView */
	  	
  protected  ListView lv;
	  	

	  	
  /** Called when the activity is first created. */
	  	
   @Override
	  	
   public void onCreate(Bundle savedInstanceState) {
	  	
        super.onCreate(savedInstanceState);
	  	
        //setContentView(R.layout.main);
	  	
        
	  	
        //Initialise the DB
	  	
        mDbHelper = new CookbookDBAdapter(this);
	  	
        mDbHelper.open();
	  	
        list.fetchAllRecipes(mDbHelper);
	  	
        
	  	
        Log.d("MyDebug", String.valueOf(list.size()));
	  	
        
	  	
       
	  	
    RECIPES = new String[list.size()];
	  	
    for (int i =0; i<list.size();i++){
	  	
      RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
	  	
      System.out.println(list.getRecipe(i).getName());
	  	
    } 
	  	
        
	  	
    // list_item is in /res/layout/ should be created
	  	
    setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RECIPES));
	  	
    
	  	
    lv = getListView();
	  	
    lv.setTextFilterEnabled(true);
	  	
    
	  	
    lv.setOnItemClickListener(new OnItemClickListener() {
	  	
    public void onItemClick(AdapterView<?> parent, View view,
	  	
      int position, long id) {
	  	
        Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
	  	
        // trying to send the recipe name to the new activity
	  	
        recIntent.putExtra("recipeName",list.getRecipe(position).getName());
	  	
        startActivity(recIntent);
	  	
              
	  	
      }
	  	
    });
	  	
  }
	  	
    
	  	

  	
  public void setListView(ListView lb){
	  	
      this.lv = lb;
	  	
      lv.setTextFilterEnabled(true);
	  	
    }
	  	
    
	  	
    /** need it*/
	  	
   String[] RECIPES = new String[]{"lol"};
	  	
   
	  	
}