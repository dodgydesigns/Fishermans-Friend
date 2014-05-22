package com.dodgydesigns.view;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dodgydesigns.R;
import com.dodgydesigns.controller.DatabaseManager;
import com.dodgydesigns.model.WorldWeatherOnline;

/**
 * This class provides the main dialog to record or show the details of a new catch.
 * 
 * The accessors and mutators are setup such that the same methods can be called to fill the
 * dialog from a 'new catch' or database query. For this reason these methods will (generally) be
 * restricted to string inputs.
 * 
 * @author mullsy
 * 
 */
public class NewCatchDialog extends Dialog
{
	private static final int BUTTON_OPACITY = 0;

	private static final String IMAGE_FOLDER_PATH = "file://mnt/sdcard//Fisherman's Friend//";

	// Member Variable
	private Context parent;

	private TextView date;
	private TextView weather;
	private TextView latitude;
	private TextView longitude;
	private EditText species;
	private EditText rig;
	private EditText length;
	private EditText weight;
	private RatingBar rating;
	private ImageView photo;
	private Button okButton;
	private Button cancelButton;

	private DatabaseManager dbManager;

	private Uri imageUri;


	public NewCatchDialog( Context context, DatabaseManager db, boolean showKeyboard )
	{
		super( context );
		setContentView( R.layout.new_catch );

		parent = context;
		dbManager = db; 
		
		date = (TextView)findViewById( R.id.dateLabel );
		weather = (TextView)findViewById( R.id.weatherLabel );
		latitude = (TextView)findViewById( R.id.latitude );
		longitude = (TextView)findViewById( R.id.longitude );
		species = (EditText)findViewById( R.id.speciesTextbox );
		length = (EditText)findViewById( R.id.lengthTextbox );
		weight = (EditText)findViewById( R.id.weightTextbox );
		rig = (EditText)findViewById( R.id.rigDescription );
		rating = (RatingBar)findViewById( R.id.ratingBar );
		photo = (ImageView)findViewById( R.id.picture );
		okButton = (Button)findViewById( R.id.okButton );
		cancelButton = (Button)findViewById( R.id.cancelButton );

		if( !showKeyboard )
		{
			getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
		}

		setCancelButton();
		setOkButton();
		setCancelable( true );
	}


	public void setDateText( String dateTime )
	{
		date.setText( dateTime );
	}


	public String setWeatherText( WorldWeatherOnline worldWeatherOnlineResult )
	{
		String weatherString = "";

		if( worldWeatherOnlineResult != null )
		{
			weatherString =
			    "Temp: " + worldWeatherOnlineResult.getTemp() + " \u00b0C\tWater: " +
			        worldWeatherOnlineResult.getWaterTemp() + " \u00b0C\t" +
			        worldWeatherOnlineResult.getWindSpeed() + "km/h " +
			        worldWeatherOnlineResult.getWindDirection() + "\nSwell: " +
			        worldWeatherOnlineResult.getSwellHeight() + "m\t\tWaves: " +
			        worldWeatherOnlineResult.getWaveHeight();
		}
		else
		{
			weatherString = "Sorry, can't get the weather : (\n";
		}

		weather.setText( weatherString );

		return weatherString;
	}


	public void setWeatherText( String weatherString )
	{
		weather.setText( weatherString );
	}


	public void setLocationText( String longString, String latString )
	{
		latitude.setText( longString );
		longitude.setText( latString );
	}

	public void setSpeciesText( String speciesText )
	{
//		species.setBackgroundResource( 0 );
//		species.setTextColor( Color.GREEN );
		species.setText( speciesText );
	}

	public void setLengthText( String speciesText )
	{
		length.setText( speciesText );
	}

	public void setWeightText( String speciesText )
	{
		weight.setText( speciesText );
	}


	public void setRigText( String rigText )
	{
		rig.setText( rigText );
	}


	public void setRating( float ratingValue )
	{
		rating.setRating( ratingValue );
	}


	/**
	 * @param imageName
	 */
	public void setImageThumbnail( String imageName )
	{
		imageUri = Uri.parse( IMAGE_FOLDER_PATH + imageName );

		ContentResolver cr = this.getContext().getContentResolver();
		Bitmap image = null;
		try
		{
			image = android.provider.MediaStore.Images.Media.getBitmap( cr, imageUri );
		}
		catch( Exception e )
		{
			Toast.makeText( parent, "Failed to load in NewCatchDialog", Toast.LENGTH_SHORT ).show();
			Log.e( "NewCatchDialog:setImageThumbnail ", e.toString() );
		}

		photo.setImageBitmap( image );
	}

	
	/**
	 * 
	 */
	public void setOkButton()
	{
		okButton.setOnClickListener( new View.OnClickListener()
		{
			public void onClick( View v )
			{
				dbManager.addRow(
                                    date.getText().toString(),
                                    weather.getText().toString(),
                                    latitude.getText().toString(),
                                    longitude.getText().toString(),
                                    species.getText().toString(),
                                    length.getText().toString(),
                                    weight.getText().toString(),
                                    rig.getText().toString(),
                                    String.valueOf( rating.getRating() ),
                                    imageUri.toString() );
								
				cancel();
			}
		} );
		okButton.setBackgroundColor( BUTTON_OPACITY );
		okButton.setTextColor( Color.WHITE );
	}

	public void setCancelButton()
	{
		cancelButton.setOnClickListener( new View.OnClickListener()
		{
			public void onClick( View v )
			{
				cancel();
			}
		} );
		cancelButton.setBackgroundColor( BUTTON_OPACITY );
		cancelButton.setTextColor( Color.WHITE );
	}
}
