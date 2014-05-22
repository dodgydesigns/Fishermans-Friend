package com.dodgydesigns.view;

import java.io.BufferedWriter;
import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dodgydesigns.R;

public class Preferences extends Dialog
{
	Context parent;
	
	Button imageLocationButton;
	
	public Preferences( Context context, BufferedWriter prefsFile )
    {
	    super( context );
	    
	    setContentView( R.layout.preferences );
	    
	    parent = context;
	    
	    try
        {
	        prefsFile.write( "some prefs" );
        }
        catch( IOException e )
        {
        	Log.e( "Preferences", "error writing" );
        }
	    
	    imageLocationButton = (Button)findViewById( R.id.imageLocationButton );
    }

	public void setImageLocationButton()
	{
		imageLocationButton.setOnClickListener( new View.OnClickListener()
		{
			public void onClick( View v )
			{
				FileBrowserDialog fileBrowser = new FileBrowserDialog( parent );
				fileBrowser.show();
			}
		} );
	}
}
