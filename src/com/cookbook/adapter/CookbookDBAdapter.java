package com.cookbook.adapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/* Provides following functionality:
 * -Create/open/close database file
 * -Define table structure
 * -Create/update/delete record methods
 * -Fetch methods
 */
public class CookbookDBAdapter {
	// recipe table fields
    public static final String PKEY_RECIPE_ID = "_id";
	public static final String KEY_RECIPE_NAME = "recipeName";
	public static final String KEY_METHOD = "method	";
    public static final String KEY_MEAL_TYPE = "mealType";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_TIME_OF_YEAR = "timeOfYear";
    public static final String KEY_REGION = "region";
    public static final String KEY_RATING = "rating";
    public static final String KEY_SERVES = "serves";
	
    // recipeIngredients table fields
    public static final String PKEY_RECIPE_INGREDIENT_ID = "_id";
    public static final String FKEY_RECIPE_ID = "recipeId";
    public static final String FKEY_INGREDIENT_NAME = "ingredientName";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_UNIT = "unit";

    // friends table fields
    public static final String PKEY_FACEBOOK_ID = "_id";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_SURNAME = "surname";

    // TAG is used to identify this class in log messages
    private static final String TAG = "CookBookDbAdapter";
    
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
	private final Context mContext;
    
    // sql create table strings
    // recipe table
    private static final String RECIPE_TABLE_CREATE =
       	"create table recipe (" + PKEY_RECIPE_ID + " integer primary key " +
       	"autoincrement, " + KEY_RECIPE_NAME + " text not null, " + KEY_METHOD +
       	" text, " + KEY_MEAL_TYPE + " text, " + KEY_DURATION +
       	" integer, " + KEY_TIME_OF_YEAR + " text, " + KEY_REGION + " text, " +
       	KEY_RATING + " real, " + KEY_SERVES + " integer);";
    
    // recipeIngredients table
    private static final String RECIPE_INGREDIENTS_TABLE_CREATE =
           	"create table recipeIngredients (" + PKEY_RECIPE_INGREDIENT_ID +
           	" integer primary key autoincrement, " + FKEY_RECIPE_ID +
           	" integer not null, " + FKEY_INGREDIENT_NAME + " string not null, " +
           	KEY_QUANTITY + " integer, " + KEY_UNIT + " string);";

    // friends table
    private static final String FRIENDS_TABLE_CREATE =
           	"create table friends (" + PKEY_FACEBOOK_ID +
           	" integer primary key, " + KEY_FIRSTNAME +
           	" text, " + KEY_SURNAME + " text);";

    // database and table name strings
    private static final String DATABASE_NAME = "cookbook";
    private static final String RECIPE_TABLE = "recipe";
    private static final String RECIPE_INGREDIENTS_TABLE = "recipeIngredients";
    private static final String FRIENDS_TABLE = "friends";
    private static final int DATABASE_VERSION = 2;
    
    

    // database creation and upgrade
    private static class DatabaseHelper extends SQLiteOpenHelper {
    	
    	private static String DB_PATH = "/data/data/com.cookbook/databases/";
        private static String DB_NAME = "cookbook";
        private SQLiteDatabase myDataBase; 
        
        private Context mContext;
        

		DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.mContext = context;
            
        }

        /*@Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(RECIPE_TABLE_CREATE);
            db.execSQL(RECIPE_INGREDIENTS_TABLE_CREATE);
            db.execSQL(FRIENDS_TABLE_CREATE);
        }*/
        
        /**
         * Creates a empty database on the system and rewrites it with your own database.
         * */
        public void createDataBase() throws IOException{
     
        	boolean dbExist = checkDataBase();
     
        	if(dbExist){
        		//do nothing - database already exist
        	}else{
     
        		//By calling this method and empty database will be created into the default system path
                   //of your application so we are gonna be able to overwrite that database with our database.
            	this.getReadableDatabase();
     
            	try {
     
        			copyDataBase();
     
        		} catch (IOException e) {
     
            		throw new Error("Error copying database");
     
            	}
        	}
     
        }
     
        /**
         * Check if the database already exist to avoid re-copying the file each time you open the application.
         * @return true if it exists, false if it doesn't
         */
        private boolean checkDataBase(){
     
        	SQLiteDatabase checkDB = null;
     
        	try{
        		String myPath = DB_PATH + DB_NAME;
        		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     
        	}catch(SQLiteException e){
     
        		//database does't exist yet.
     
        	}
     
        	if(checkDB != null){
     
        		checkDB.close();
     
        	}
     
        	return checkDB != null ? true : false;
        }
     
        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transfering bytestream.
         * */
        private void copyDataBase() throws IOException{
     
        	//Open your local db as the input stream
        	InputStream myInput = mContext.getAssets().open(DB_NAME);
     
        	// Path to the just created empty db
        	String outFileName = DB_PATH + DB_NAME;
     
        	//Open the empty db as the output stream
        	OutputStream myOutput = new FileOutputStream(outFileName);
     
        	//transfer bytes from the inputfile to the outputfile
        	byte[] buffer = new byte[1024];
        	int length;
        	while ((length = myInput.read(buffer))>0){
        		myOutput.write(buffer, 0, length);
        	}
     
        	//Close the streams
        	myOutput.flush();
        	myOutput.close();
        	myInput.close();
     
        }
     
        public void openDataBase() throws SQLException{
     
        	//Open the database
            String myPath = DB_PATH + DB_NAME;
        	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     
        }
     
        @Override
    	public synchronized void close() {
     
        	    if(myDataBase != null)
        		    myDataBase.close();
     
        	    super.close();
     
    	}
     
    	@Override
    	public void onCreate(SQLiteDatabase db) {
    		
    	}

		
     
    	
     
            // Add your public helper methods to access and get content from the database.
           // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
           // to you to create adapters for your views.
     
    

        // upgrade database structure after db change
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion);
            // add serves attribute to recipe table
            if (oldVersion == 1) {
            	db.execSQL("ALTER TABLE recipe ADD COLUMN " + KEY_SERVES
            		+ " integer");
        	}
        }
    }
        
    
    
    
    // CookBookDbAdapter constructor takes context in which to open/create
    // the database
    public CookbookDBAdapter(Context context) {
        this.mContext = context;
    }

    // Open cookbook database (create db if not already present)
    public CookbookDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        try {
			mDbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDbHelper.openDataBase();
		
        mDb = mDbHelper.getWritableDatabase();
        
        //Had to be close and repeated due to a bug
      this.close();
      mDbHelper.openDataBase();
		
      mDb = mDbHelper.getWritableDatabase();
        /*mDb.execSQL(RECIPE_TABLE_CREATE);
		mDb.execSQL(RECIPE_INGREDIENTS_TABLE_CREATE);
		mDb.execSQL(FRIENDS_TABLE_CREATE);*/
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

// create a new recipe using values provided in parameters
// (rating initialises to 0)
    public long createRecipe(String recipeName, String method, String mealType,
    	int duration, String timeOfYear, String region) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(KEY_RECIPE_NAME, recipeName);
    	addValues.put(KEY_METHOD, method);
    	addValues.put(KEY_MEAL_TYPE, mealType);
    	addValues.put(KEY_DURATION, duration);
    	addValues.put(KEY_TIME_OF_YEAR, timeOfYear);
    	addValues.put(KEY_REGION, region);
    	addValues.put(KEY_RATING, 0);

        return mDb.insert(RECIPE_TABLE, null, addValues);
    }
	
    // create a new recipe using values provided in parameters
    // (manually initialise rating)
    public long createRecipe(String recipeName, String method, String mealType,
    	int duration, String timeOfYear, String region, float rating, int serves) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(KEY_RECIPE_NAME, recipeName);
    	addValues.put(KEY_METHOD, method);
    	addValues.put(KEY_MEAL_TYPE, mealType);
    	addValues.put(KEY_DURATION, duration);
    	addValues.put(KEY_TIME_OF_YEAR, timeOfYear);
    	addValues.put(KEY_REGION, region);
    	addValues.put(KEY_RATING, rating);
    	addValues.put(KEY_SERVES, serves);

        return mDb.insert(RECIPE_TABLE, null, addValues);
    }

    // delete recipe corresponding to given recipe_ID
    // **this will need to be linked to deleting the corresponding
    // recipeIngredients**
    public boolean deleteRecipe(long recipeId) {

        return mDb.delete(RECIPE_TABLE, PKEY_RECIPE_ID + "=" + recipeId, null)
        	> 0;
    }

    // return cursor over all recipes, sorted by category then name
    public Cursor fetchAllRecipes() {
    	String mealTypeOrder = " WHEN 'Breakfast' THEN 1 WHEN 'Lunch' THEN 2 WHEN 'Dinner' THEN 3 WHEN 'Snack' THEN 4 WHEN 'Dessert' THEN 5 ELSE 99 END";
    	
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING, KEY_SERVES}, null, null, null, null, 
        	"CASE "+ KEY_MEAL_TYPE + mealTypeOrder + "," + KEY_RECIPE_NAME);
    }

    // return cursor over all recipes, sorted by category then name
    public Cursor fetchAllRecipesByName() {
    	
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING, KEY_SERVES}, null, null, null, null, 
        	KEY_RECIPE_NAME);
    }

    // return cursor over all recipes, sorted by duration then name
    public Cursor fetchAllRecipesByDuration() {
    	
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING, KEY_SERVES}, null, null, null, null, 
        	KEY_DURATION + "," + KEY_RECIPE_NAME);
    }

    // return cursor over all recipes, sorted by occasion then name
    public Cursor fetchAllRecipesByOccasion() {
    	
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING, KEY_SERVES}, null, null, null, null, 
        	KEY_TIME_OF_YEAR + "," + KEY_RECIPE_NAME);
    }
    
    // return cursor over all recipes, sorted by region then name
    public Cursor fetchAllRecipesByRegion() {
    	
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING, KEY_SERVES}, null, null, null, null, 
        	KEY_REGION + "," + KEY_RECIPE_NAME);
    }
    
    // return cursor over all recipes, sorted by rating (best to worst) then name
    public Cursor fetchAllRecipesByRating() {
    	
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING, KEY_SERVES}, null, null, null, null, 
        	KEY_REGION + " DESC," + KEY_RECIPE_NAME);
    }
    
    // return cursor with filters applied recipes
    // sorted by category then name
    public Cursor fetchRecipeFilter(String mealType, int cookingDuration,
    		String season, String country, float rating) {
    	
    	// ordering for meal types
    	String mealTypeOrder = " WHEN 'Breakfast' THEN 1 WHEN 'Lunch' THEN 2 WHEN 'Dinner' THEN 3 WHEN 'Snack' THEN 4 WHEN 'Dessert' THEN 5 ELSE 99 END";

    	// construct filter string
    	String filter = "";
    	if (mealType != null) filter += KEY_MEAL_TYPE + " LIKE '" + mealType + "' AND ";
    	if (cookingDuration > 0) filter += KEY_DURATION + " <= " + cookingDuration + " AND ";
    	if (season != null) filter += KEY_TIME_OF_YEAR + " LIKE '" + season + "' AND ";
    	if (country != null) filter += KEY_REGION + " LIKE '" + country + "' AND "; 
    	if (rating > 0) filter += KEY_RATING + " >= " + rating;
    	if (filter.endsWith(" AND ")) filter = filter.substring(0, filter.length()-5);

    	// returned filtered, sorted query
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING, KEY_SERVES}, filter, null, null, null, 
        	"CASE "+ KEY_MEAL_TYPE + mealTypeOrder + "," + KEY_RECIPE_NAME);
    }
    
    // return cursor at recipe with given recipeID 
    public Cursor fetchRecipe(long recipeId) throws SQLException {

        Cursor mCursor = mDb.query(RECIPE_TABLE,
        	new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
        	KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING,
        	KEY_SERVES}, PKEY_RECIPE_ID + "=" + recipeId, null, null, null,
        	KEY_RECIPE_NAME);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // return cursor at recipe with given recipeName 
    public Cursor fetchRecipe(String recipeName) throws SQLException {

        Cursor mCursor = mDb.query(RECIPE_TABLE,
        	new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
        	KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING,
        	KEY_SERVES},
        	KEY_RECIPE_NAME + "=" + "'" + recipeName + "'", null, null, null,
        	KEY_RECIPE_NAME);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
      
    /** 
     * Search the recipes for patterns in the name
     * http://www.w3schools.com/sql/sql_like.asp
     * @param recipeName
     * @return Cursor
     * @throws SQLException
     */
    public Cursor fetchRecipeLike(String recipeName) throws SQLException {

    	Cursor mCursor = mDb.query(RECIPE_TABLE,
    		new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
    		KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING,
    		KEY_SERVES},
    		KEY_RECIPE_NAME + " LIKE " + "'"+recipeName+"'", null, null, null, 
    		KEY_RECIPE_NAME);

    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }

    /**
     * Query used for suggestFromBookmarks
     * @param type
     * @param season
     * @param cookingtime
     * @return
     * @throws SQLException
     */
    public Cursor fetchRecipe(String type,String season,int cookingtime) throws SQLException {
 
        Cursor mCursor = mDb.query(RECIPE_TABLE,
        		new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
                KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING,
                KEY_SERVES}, KEY_MEAL_TYPE + "=" + "'" + type + "'" + " AND " +
                KEY_TIME_OF_YEAR + "=" + "'" + season + "'" + " AND " +
                KEY_DURATION + "<=" + "'" + cookingtime + "'", null, null, null, null);
 
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // Update recipe at given recipe ID with the values passed
    public boolean updateRecipe(long recipeId, String recipeName, String method,
    	String mealType, int duration, String timeOfYear, String region,
    	float rating, int serves) {

        ContentValues newValues = new ContentValues();
    	newValues.put(KEY_RECIPE_NAME, recipeName);
    	newValues.put(KEY_METHOD, method);
    	newValues.put(KEY_MEAL_TYPE, mealType);
    	newValues.put(KEY_DURATION, duration);
    	newValues.put(KEY_TIME_OF_YEAR, timeOfYear);
    	newValues.put(KEY_REGION, region);
    	newValues.put(KEY_RATING, rating);
    	newValues.put(KEY_SERVES, serves);

        return mDb.update(RECIPE_TABLE, newValues,
        	PKEY_RECIPE_ID + "=" + recipeId, null) > 0;
    }

    // update a recipe's rating
    public boolean updateRecipe(long recipeId, float rating) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_RATING, rating);
        
        return mDb.update(RECIPE_TABLE, newValues,
        	PKEY_RECIPE_ID + "=" + recipeId, null) > 0;
    }

    // create a new recipeIngredient using values provided in parameters
    public long createRecipeIngredient(long recipeId, String ingredientName,
    	int quantity, String unit) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(FKEY_RECIPE_ID, recipeId);
    	addValues.put(FKEY_INGREDIENT_NAME, ingredientName);
    	addValues.put(KEY_QUANTITY, quantity);
    	addValues.put(KEY_UNIT, unit);
    	
        return mDb.insert(RECIPE_INGREDIENTS_TABLE, null, addValues);
    }
    
    // return cursor containing all ingredients for recipe with given recipeId
    public Cursor fetchRecipeIngredient(long recipeId) throws SQLException {

        Cursor mCursor = mDb.query(RECIPE_INGREDIENTS_TABLE,
        	new String[] {PKEY_RECIPE_INGREDIENT_ID, FKEY_RECIPE_ID,
        	FKEY_INGREDIENT_NAME, KEY_QUANTITY, KEY_UNIT}, FKEY_RECIPE_ID + "=" +
        	recipeId, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // create friend record using facebook user id as key
    public long createFriend(long facebookId, String firstName, String surname) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(PKEY_FACEBOOK_ID, facebookId);
    	addValues.put(KEY_FIRSTNAME, firstName);
    	addValues.put(KEY_SURNAME, surname);
    	
        return mDb.insert(FRIENDS_TABLE, null, addValues);
    }

    // update first name and surname for given facebookId
    public boolean updateFriend(long facebookId, String firstName, String surname) {
        ContentValues newValues = new ContentValues();
    	newValues.put(KEY_FIRSTNAME, firstName);
    	newValues.put(KEY_SURNAME, surname);
    	
    	return mDb.update(FRIENDS_TABLE, newValues,
    		PKEY_RECIPE_ID + "=" + facebookId, null) > 0;
    }

    // delete friend with given facebookId
    public boolean deleteFriend(long facebookId) {
    	return mDb.delete(FRIENDS_TABLE, PKEY_FACEBOOK_ID + "=" + facebookId, null) > 0;
    }

    public boolean deleteAllFriends() {
    	return mDb.delete(FRIENDS_TABLE, null, null) > 0;
    }

    // fetch all friends, ordered by surname then firstname
    public Cursor fetchAllFriends() {
        return mDb.query(FRIENDS_TABLE, new String[] {PKEY_FACEBOOK_ID,
            KEY_FIRSTNAME, KEY_SURNAME}, null, null, null, null,
            KEY_SURNAME + ", " + KEY_FIRSTNAME);
    }
}
