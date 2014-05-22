package com.dodgydesigns.controller;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper
{
	private final static String DB_NAME = "fishermans_db"; // the name of our database
	private final static int DB_VERSION = 1; // the version of the database

	// The names for our database columns
	private final String TABLE_NAME = "fishermansfriend_table";
	private final String TABLE_ROW_ID = "id";
	private final String DATE_TIME = "date_time";
	private final String WEATHER = "weather";
	private final String LAT = "lat";
	private final String LON = "lon";
	private final String SPECIES = "species";
	private final String LENGTH = "length";
	private final String WEIGHT = "weight";
	private final String RIG = "rig";
	private final String RATING = "rating";
	private final String IMAGE = "image";
	
	public final static int TABLE_ROW_ID_COL = 0;
	public final static int DATE_TIME_COL = 1;
	public final static int WEATHER_COL = 2;
	public final static int LAT_COL = 3;
	public final static int LON_COL = 4;
	public final static int SPECIES_COL = 5;
	public final static int LENGTH_COL = 6;
	public final static int WEIGHT_COL = 7;
	public final static int RIG_COL = 8;
	public final static int RATING_COL = 9;
	public final static int IMAGE_COL = 10;
	
	private SQLiteDatabase db; // a reference to the database manager class.
	private Context context;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public DatabaseManager( Context context )
	{
		super( context, DB_NAME, null, DB_VERSION );

		// create or open the database
		db = getWritableDatabase();			
	}

	
	/* Only called the first time the database is created.
	 * 
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate( SQLiteDatabase db )
	{
		// The SQLite query string that will create our database table.
		String newTableQueryString =
		    "create table " + 
		    	TABLE_NAME + 
		    	" (" + 
		    	TABLE_ROW_ID +
		        " integer primary key autoincrement not null," + 
		    	DATE_TIME 	+ " text," +
		        WEATHER 	+ " text," + 
		    	"lat" 		+ " text," + 
		        LON 		+ " text," + 
		    	SPECIES 	+ " text," +
		        LENGTH 		+ " text," + 
		    	WEIGHT 		+ " text," + 
		        RIG 		+ " text," + 
		    	RATING 		+ " text," +
		        IMAGE 		+ " text" + 
		    	");";

		// execute the query string to the database.
		db.execSQL( newTableQueryString );
	}
	
	
	/**
	 * @param dateTime
	 * @param weather
	 * @param lat
	 * @param lon
	 * @param species
	 * @param length
	 * @param weight
	 * @param rig
	 * @param rating
	 * @param image
	 */
	public void addRow( String dateTime, String weather, String lat, String lon,
	                    String species, String length, String weight, String rig,
	                    String rating, String image )
	{
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();

		// this is how you add a value to a ContentValues object
		// we are passing in a key string and a value string each time
		values.put( DATE_TIME, dateTime );
		values.put( WEATHER, weather );
		values.put( LAT, lat );
		values.put( LON, lon );
		values.put( SPECIES, species );
		values.put( LENGTH, length );
		values.put( WEIGHT, weight );
		values.put( RIG, rig );
		values.put( RATING, rating );
		values.put( IMAGE, image );

		// ask the database object to insert the new data 
		try
		{
			db.insert( TABLE_NAME, null, values );
		}
		catch( Exception e )
		{
			Log.e( "DB ERROR", e.toString() ); // prints the error message to the log
			e.printStackTrace(); // prints the stack trace to the log
		}
	}


	public void clearDatabase()
	{
		android.database.Cursor cursor;

		// ask the database object to create the cursor.
		cursor =
		    db.query(
		              TABLE_NAME,
		              new String[]{ TABLE_ROW_ID, DATE_TIME, WEATHER, LAT, LON, SPECIES,
		                           LENGTH, WEIGHT, RIG, RATING, IMAGE },
		              null,
		              null,
		              null,
		              null,
		              null );

		// move the cursor's pointer to position zero.
		cursor.moveToFirst();

		// if there is data after the current cursor position, add it
		// to the ArrayList.
		if( !cursor.isAfterLast() )
		{
			do
			{
				deleteRow( cursor.getLong( 0 ));
				Log.e("db", "Clearing row " + cursor.getLong( 0 ));
			}
			// move the cursor's pointer up one position.
			while( cursor.moveToNext() );
		}
		Log.e("db", "Cleared Entire Database");
	}
	
	
	/**
	 * @param rowID
	 */
	public void deleteRow( long rowID )
	{
		// ask the database manager to delete the row of given id
		try
		{
			db.delete( TABLE_NAME, TABLE_ROW_ID + "=" + rowID, null );
		}
		catch( Exception e )
		{
			Log.e( "DB ERROR", e.toString() );
			e.printStackTrace();
		}
	}


	/**
	 * @param rowID
	 * @param dateTime
	 * @param weather
	 * @param lat
	 * @param lon
	 * @param species
	 * @param length
	 * @param weight
	 * @param rig
	 * @param rating
	 * @param image
	 */
	public void updateRow( long rowID, String dateTime, String weather, String lat, String lon,
	                       String species, String length, String weight, String rig,
	                       String rating, String image )
	{
		// this is a key value pair holder used by android's SQLite functions
		//TODO: make this generic and put and update method call in each field that actually
		// can be update.
		ContentValues values = new ContentValues();
		values.put( DATE_TIME, dateTime );
		values.put( WEATHER, weather );
		values.put( LAT, lat );
		values.put( LON, lon );
		values.put( SPECIES, species );
		values.put( LENGTH, length );
		values.put( WEIGHT, weight );
		values.put( RIG, rig );
		values.put( RATING, rating );
		values.put( IMAGE, image );

		// ask the database object to update the database row of given rowID
		try
		{
			db.update( TABLE_NAME, values, TABLE_ROW_ID + "=" + rowID, null );
		}
		catch( Exception e )
		{
			Log.e( "DB Error", e.toString() );
			e.printStackTrace();
		}
	}


	/**
	 * @return
	 */
	public ArrayList<ArrayList<Object>> getAllRowsAsArrays()
	{
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		android.database.Cursor cursor;

		// ask the database object to create the cursor.
		cursor =
		    db.query(
		              TABLE_NAME,
		              new String[]{ TABLE_ROW_ID, DATE_TIME, WEATHER, LAT, LON, SPECIES,
		                           LENGTH, WEIGHT, RIG, RATING, IMAGE },
		              null,
		              null,
		              null,
		              null,
		              null );

		// move the cursor's pointer to position zero.
		cursor.moveToFirst();

		// if there is data after the current cursor position, add it
		// to the ArrayList.
		if( !cursor.isAfterLast() )
		{
			do
			{
				ArrayList<Object> dataList = new ArrayList<Object>();

				dataList.add( cursor.getLong( TABLE_ROW_ID_COL ) );
				dataList.add( cursor.getString( DATE_TIME_COL ) );
				dataList.add( cursor.getString( WEATHER_COL ) );
				dataList.add( cursor.getString( LAT_COL ) );
				dataList.add( cursor.getString( LON_COL ) );
				dataList.add( cursor.getString( SPECIES_COL ) );
				dataList.add( cursor.getString( LENGTH_COL ) );
				dataList.add( cursor.getString( WEATHER_COL ) );
				dataList.add( cursor.getString( RIG_COL ) );
				dataList.add( cursor.getString( RATING_COL ) );
				dataList.add( cursor.getString( IMAGE_COL ) );
				
				Log.e("db", cursor.getString( SPECIES_COL ));

				dataArrays.add( dataList );
			}
			// move the cursor's pointer up one position.
			while( cursor.moveToNext() );
		}

		// return the ArrayList that holds the data collected from
		// the database.
		return dataArrays;
	}


	/**
	 * @param imageName
	 * @return
	 */
	public ArrayList<Object> getRowByImageName(String imageName)
	{
		ArrayList<Object> rowArray = new ArrayList<Object>();
		long rowID = 0;
		
		ArrayList<ArrayList<Object>> wholeDB = getAllRowsAsArrays();
		
		for (ArrayList<Object> row : wholeDB)
		{
			String path = (String)row.get( DatabaseManager.IMAGE_COL );
			
			if (path.contains( imageName ))
			{
				rowID = (Long)row.get( DatabaseManager.TABLE_ROW_ID_COL );
			}
		}

		rowArray = getRowByIdAsArray( rowID );
		
		return rowArray;
	}
	
	
	public ArrayList<Object> getRowByIdAsArray( long rowID )
	{
		// create an array list to store data from the database row.
		// I would recommend creating a JavaBean compliant object 
		// to store this data instead.  That way you can ensure
		// data types are correct.
		ArrayList<Object> rowArray = new ArrayList<Object>();
		android.database.Cursor cursor;

		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		cursor =
		    db.query(
		              TABLE_NAME,
		              new String[]{ TABLE_ROW_ID, DATE_TIME, WEATHER, LAT, LON, SPECIES,
		                           LENGTH, WEIGHT, RIG, RATING, IMAGE },
		              TABLE_ROW_ID + "=" + rowID,
		              null,
		              null,
		              null,
		              null,
		              null );

		// move the pointer to position zero in the cursor.
		cursor.moveToFirst();

		// if there is data available after the cursor's pointer, add
		// it to the ArrayList that will be returned by the method.
		if( !cursor.isAfterLast() )
		{
			do
			{
				rowArray.add( cursor.getLong( 0 ) );
				rowArray.add( cursor.getString( 1 ) );
				rowArray.add( cursor.getString( 2 ) );
				rowArray.add( cursor.getString( 3 ) );
				rowArray.add( cursor.getString( 4 ) );
				rowArray.add( cursor.getString( 5 ) );
				rowArray.add( cursor.getString( 6 ) );
				rowArray.add( cursor.getString( 7 ) );
				rowArray.add( cursor.getString( 8 ) );
				rowArray.add( cursor.getString( 9 ) );
				rowArray.add( cursor.getString( 10 ) );
			}
			while( cursor.moveToNext() );
		}

		// let java know that you are through with the cursor.
		cursor.close();

		// return the ArrayList containing the given row from the database.
		return rowArray;
	}


	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
	{
		// TODO Auto-generated method stub

	}
}
