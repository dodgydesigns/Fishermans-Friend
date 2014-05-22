package util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils
{
	/**
	 * Take the double value for the longitude and add the appropriate cardinal value to make a
	 * descriptive string.
	 * 
	 * @param longitude The longitude to be used for the string.
	 * 
	 * @return The string representing a cardinal longitudinal location.
	 */
	public static String formatLongitude( double longitude )
	{
		String lonString = "";
		DecimalFormat formatter = new DecimalFormat( "0.000" );

		if( longitude >= 0 && longitude <= 180 )
		{
			lonString = formatter.format( longitude ) + " \u00b0E";
		}
		else
		{
			lonString = formatter.format( longitude ) + " \u00b0W";
		}

		return lonString;
	}


	/**
	 * Take the double value for the latitude and add the appropriate cardinal value to make a
	 * descriptive string.
	 * 
	 * @param longitude The latitude to be used for the string.
	 * 
	 * @return The string representing a cardinal latitudinal location.
	 */
	public static String formatLatitude( double latitude )
	{
		String latString = "";
		DecimalFormat formatter = new DecimalFormat( "0.000" );

		if( latitude < 0 )
		{
			latString = formatter.format( latitude ) + " \u00b0S";
		}
		else
		{
			latString = formatter.format( latitude ) + " \u00b0N";
		}

		return latString;
	}


	/**
	 * Get the date and format it nicely.
	 * 
	 * @param withSeconds Boolean to determine whether seconds are required in the date/time.
	 * 
	 * @return The date in a nicely formatted string.
	 */
	public static String getDateTime(boolean withSeconds)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy HH:mm" );
		
		if (withSeconds)
		{
			dateFormat = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
		}
		
		Date date = new Date();
		return dateFormat.format( date );
	}
}
