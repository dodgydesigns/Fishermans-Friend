package com.dodgydesigns.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import util.Utils;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dodgydesigns.R;
import com.dodgydesigns.controller.DatabaseManager;
import com.dodgydesigns.controller.ImageAdapter;
import com.dodgydesigns.model.WorldWeatherOnline;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * This is the main entry point for the application.
 * 
 * It begins by displaying a map, zoomed in to show the current location, with a button to add a
 * new catch and a gallery of all the existing entries.
 * 
 * @author mullsy
 * 
 */
public class FishermansFriend extends MapActivity implements Runnable
{
	// Class Constants
	private final String APP_ID = "FishermansFriend";

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 20 * 60 * 1000; // in Minutes
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 69;

	private static final int BUTTON_OPACITY = 0;

	private static final float BASE_SCREEN_WIDTH = 986;

	// Member Variables
	// Geo-location
	private MapView mapView;
	private LocationManager locationManager;
	private double latitude;
	private double longitude;

	// Imaging
	private Gallery gallery;
	private String imageName;
	private File appHomeDirectory;

	// GUI
	private ImageButton newCatchButton;
	private ImageButton prefsButton;
	private ProgressDialog locationProgressDialog;

	// Weather
	WorldWeatherOnline worldWeatherOnlineResult;
	private Thread thread;

	// Database
	private DatabaseManager dbManager;

	private BufferedWriter prefsFile;


	@Override
	public void onCreate( Bundle bundle )
	{
		super.onCreate( bundle );
		setContentView( R.layout.main_layout );

		// Make a root directory to store images etc. in
		appHomeDirectory =
		    new File( Environment.getExternalStorageDirectory() + "/Fisherman's Friend/" );
		appHomeDirectory.mkdirs();

		try
		{
			prefsFile =
			    new BufferedWriter( new FileWriter( appHomeDirectory.getAbsolutePath() + ".prefs" ) );
		}
		catch( IOException e )
		{
			// TODO Auto-generated catch block
			Log.e( APP_ID, "prefsFile" );
		}

		// We need to get the current GPS position.  This could take some time so let's show an
		// indefinite scrolly thing until we're done.  There is no point even continuing if this 
		// is cancelled.
		locationProgressDialog =
		    ProgressDialog.show(
		                         this,
		                         "Working..",
		                         "Getting your location\n and weather...",
		                         false,
		                         true );

		locationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
		locationManager.requestLocationUpdates(
		                                        LocationManager.GPS_PROVIDER,
		                                        MINIMUM_TIME_BETWEEN_UPDATES,
		                                        MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
		                                        new MyLocationListener() );

		// Create or connect to the database
		setupDatabase();

		// Get the map sorted out
		setupMap();

		// Check the image folder and create a gallery of anything in there.
		setupGallery();

		// Set up a listener for the buttons
		enableButtons();
	}


	private void setupDatabase()
	{
		dbManager = new DatabaseManager( this );
//		dbManager.clearDatabase();
//		dbManager.getAllRowsAsArrays();
	}


	private void setupMap()
	{
		mapView = (MapView)findViewById( R.id.mapview );
		mapView.setBuiltInZoomControls( true );

		MyLocationOverlay myLocationOverlay = new MyLocationOverlay( this, mapView );
		myLocationOverlay.enableMyLocation();
		mapView.getOverlays().add( myLocationOverlay );
	}


	private void setupGallery()
	{
		final ImageAdapter adapter = new ImageAdapter( this, appHomeDirectory );

		gallery = (Gallery)findViewById( R.id.gallery );
		gallery.setAdapter( adapter );

		gallery.setSelection( Math.round( adapter.getCount() / 2 ) );

		gallery.setOnItemClickListener( new OnItemClickListener()
		{
			public void onItemClick( AdapterView<?> parent, View v, int position, long id )
			{
				displayCatchDialog( adapter, position );
			}
		} );
	}


	private void enableButtons()
	{
		// Find out what the screen res is so we can scale the buttons to fit
		// Graphics for the buttons are based on 1280dpi width
		Display display = ((WindowManager)getSystemService( WINDOW_SERVICE )).getDefaultDisplay();
		int screenWidth = display.getWidth();
//		int screenHeight = display.getHeight();
		float scale = screenWidth / BASE_SCREEN_WIDTH;
//		Log.e("FF", "width " + screenWidth + " " + BASE_SCREEN_WIDTH + " " + scale);

		// Set up each button
		setNewCatchButton( scale );
		setPreferencesButton( scale );
	}


	private void setNewCatchButton( float scale )
	{
		newCatchButton = (ImageButton)findViewById( R.id.new_Button );
		newCatchButton.setBackgroundColor( BUTTON_OPACITY );
		newCatchButton.setScaleX( scale );
		newCatchButton.setScaleY( scale );
		newCatchButton.setOnClickListener( new View.OnClickListener()
		{
			public void onClick( View view )
			{
				takePhoto( view );
			}
		} );
	}


	private void setPreferencesButton( float scaleX )
	{
		prefsButton = (ImageButton)findViewById( R.id.settings_Button );
		prefsButton.setBackgroundColor( BUTTON_OPACITY );
		prefsButton.setScaleX( scaleX );
		prefsButton.setScaleY( scaleX );
		prefsButton.setOnClickListener( new View.OnClickListener()
		{
			public void onClick( View view )
			{
				Preferences prefs = new Preferences( FishermansFriend.this, prefsFile );
				prefs.show();
			}
		} );
	}


	private void startAsyncThread()
	{
		thread = new Thread( this );
		thread.start();
	}


	public void takePhoto( View view )
	{
		Intent intent = new Intent( "android.media.action.IMAGE_CAPTURE" );

		imageName = Utils.getDateTime( true ) + ".jpg";

		File photo = new File( appHomeDirectory, imageName );

		intent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( photo ) );

		startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE );
	}


	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult( requestCode, resultCode, data );

		if( resultCode == RESULT_OK )
		{
			Uri selectedImage = Uri.parse( "file://" + appHomeDirectory + "/" + imageName );
			getContentResolver().notifyChange( selectedImage, null );

			ContentResolver cr = getContentResolver();
			Bitmap image;
			try
			{
				image = android.provider.MediaStore.Images.Media.getBitmap( cr, selectedImage );

				createNewCatchDialog();

				setupGallery();
			}
			catch( Exception e )
			{
				Toast.makeText( this, "Failed to load", Toast.LENGTH_SHORT ).show();
				Log.e( "FishermansFriend:onActivityResult ", e.toString() );
			}
		}
	}


	private void createNewCatchDialog()
	{
		NewCatchDialog dialog = new NewCatchDialog( FishermansFriend.this, dbManager, true );

		dialog.setDateText( Utils.getDateTime( false ) );
		dialog.setWeatherText( worldWeatherOnlineResult );
		dialog.setLocationText( Utils.formatLatitude( latitude ), Utils.formatLongitude( longitude ) );
		dialog.setImageThumbnail( imageName.toString() );

		dialog.show();
	}


	private void displayCatchDialog( ImageAdapter adapter, int pos )
	{
		String imageName = adapter.getImageName( pos );

		NewCatchDialog dialog = new NewCatchDialog( FishermansFriend.this, dbManager, false );

		ArrayList<Object> dbData = dbManager.getRowByImageName( imageName );

		dialog.setDateText( dbData.get( DatabaseManager.DATE_TIME_COL ).toString() );
		dialog.setWeatherText( dbData.get( DatabaseManager.WEATHER_COL ).toString() );
		dialog.setLocationText(
		                        dbData.get( DatabaseManager.LAT_COL ).toString(),
		                        dbData.get( DatabaseManager.LON_COL ).toString() );
		dialog.setSpeciesText( dbData.get( DatabaseManager.SPECIES_COL ).toString() );
		dialog.setLengthText( dbData.get( DatabaseManager.LENGTH_COL ).toString() );
		dialog.setWeightText( dbData.get( DatabaseManager.WEIGHT_COL ).toString() );
		dialog.setRigText( dbData.get( DatabaseManager.RIG_COL ).toString() );
		dialog.setRating( Float.valueOf( dbData.get( DatabaseManager.RATING_COL ).toString() ) );
		dialog.setImageThumbnail( imageName );
		dialog.setOkButton();
		dialog.setCancelButton();

		dialog.show();
	}


	private class MyLocationListener implements LocationListener
	{

		public void onLocationChanged( Location location )
		{
			startAsyncThread();

			latitude = location.getLatitude();
			longitude = location.getLongitude();

			if( location != null )
			{
				GeoPoint point =
				    new GeoPoint( (int)(location.getLatitude() * 1E6),
				                  (int)(location.getLongitude() * 1E6) );

				MapController mc = mapView.getController();
				mc.animateTo( point );
				mc.setZoom( 15 );
				mapView.invalidate();
			}
		}


		public void onStatusChanged( String s, int i, Bundle b )
		{
			Toast.makeText( FishermansFriend.this, "Provider status changed", Toast.LENGTH_LONG ).show();
		}


		public void onProviderDisabled( String s )
		{
			Toast.makeText(
			                FishermansFriend.this,
			                "Provider disabled by the user. GPS turned off",
			                Toast.LENGTH_LONG ).show();
		}


		public void onProviderEnabled( String s )
		{
			Toast.makeText(
			                FishermansFriend.this,
			                "Provider enabled by the user. GPS turned on",
			                Toast.LENGTH_LONG ).show();
		}
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			locationProgressDialog.dismiss();
		}
	};


	@Override
	public void run()
	{
		synchronized( this )
		{
			worldWeatherOnlineResult =
			    new WorldWeatherOnline( String.valueOf( latitude ), String.valueOf( longitude ) );

			// So if we don't have anything back-which is almost the same as getting a lat/lon of 0,0
			// wait for a bit and then try again.
			while( worldWeatherOnlineResult == null || latitude == 0.0 || longitude == 0.0 )
			{
				try
				{
					// Give it 2 seconds between tries
					Thread.sleep( 2000 );
				}
				catch( InterruptedException e )
				{
					e.printStackTrace();
				}
			}

			// Good, we got it so cancel the progress dialog and get on with it.
			handler.sendEmptyMessage( 0 );
		}
	}


	private void fillData()
	{

	}


	// Required dummy methods

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();

	}
}
