package com.dodgydesigns.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class takes a location (lat/long) and uses it to get information from <a
 * href="http://www.worldweatheronline.com">World Weather Online</a> in the form of a JSON object.
 * It then makes available weather details that are relevant to fishing.
 * 
 * @author mullsy
 * 
 */
public class WorldWeatherOnline
{
	// Member Variables
	// The objects that holds the weather information
	private JSONObject data;
	private JSONObject hourlyWeatherObj;


	/**
	 * Constructor
	 * 
	 * The latitude and longitude of a location are passed in as a string and used to construct a
	 * URL that will retrieve the weather information for that location.
	 * 
	 * The weather information comes from <a href="http://www.worldweatheronline.com">World
	 * Weather Online</a>
	 * 
	 * @param lat The latitude of the location the weather is required for.
	 * @param lon The longitude of the location the weather is required for.
	 */
	public WorldWeatherOnline( String lat, String lon )
	{
		// The URL to retrieve the weather info from World Weather Online
		String weatherURL =
		    "http://free.worldweatheronline.com/feed/marine.ashx?q=" + lat + "," + lon +
		        "&format=json&key=dfefba8861123048111209";

		try
		{
			getWWOnlineResponse( weatherURL );
		}
		catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch( JSONException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * This is the guts of the class and takes the response from World Weather Online and creates
	 * a useful JSON object out of it.
	 * 
	 * @param weatherURL The URL created by the constructor to retrieve the weather information.
	 * 
	 * @return The JSON object that contains the weather data required.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	public JSONObject getWWOnlineResponse( String weatherURL ) throws IOException, JSONException
	{
		StringBuilder returnedString = new StringBuilder();
		String weatherResponse = null;
		URL url = null;
		BufferedReader input = null;
		
		url = new URL( weatherURL );

		InputStreamReader inputStreamReader = new InputStreamReader( url.openStream() );
		input = new BufferedReader( inputStreamReader );

		String htmlLine;
		while( (htmlLine = input.readLine()) != null )
		{
			returnedString.append( htmlLine );
		}

		input.close();

		weatherResponse = returnedString.toString();
		// Make JSON object out of response from server
		JSONObject weatherResponseObj = new JSONObject( weatherResponse );
		// Get highest JSON object
		data = weatherResponseObj.getJSONObject( "data" );

		// Get 'weather' array from object
		JSONArray weatherArray = data.getJSONArray( "weather" );
		JSONObject weatherObj = weatherArray.getJSONObject( 0 );

		// Get 'hourly' array from 'weather' object
		JSONArray hourlyArray = weatherObj.getJSONArray( "hourly" );
		hourlyWeatherObj = hourlyArray.getJSONObject( 0 );

		return hourlyWeatherObj;
	}


	public String getLatitude()
	{
		try
		{
			JSONArray locationArray = data.getJSONArray( "nearest_area" );
			JSONObject locationObj = locationArray.getJSONObject( 0 );

			return locationObj.getString( "latitude" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}


	public String getLongitude()
	{
		try
		{
			JSONArray locationArray = data.getJSONArray( "nearest_area" );
			JSONObject locationObj = locationArray.getJSONObject( 0 );

			return locationObj.getString( "longitude" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}


	public String getCloudCover()
	{
		try
		{
			return hourlyWeatherObj.getString( "cloudcover" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}

	public String getPressure()
	{
		try
		{
			return hourlyWeatherObj.getString( "pressure" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}

	public String getTemp()
	{
		try
		{
			return hourlyWeatherObj.getString( "tempC" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}

	public String getWaterTemp()
	{
		try
		{
			return hourlyWeatherObj.getString( "waterTemp_C" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}

	public String getSwellHeight()
	{
		try
		{
			return hourlyWeatherObj.getString( "swellHeight_m" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}

	public String getWaveHeight()
	{
		try
		{
			return hourlyWeatherObj.getString( "sigHeight_m" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}

	public String getWindDirection()
	{
		try
		{
			return hourlyWeatherObj.getString( "winddir16Point" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}

	public String getWindSpeed()
	{
		try
		{
			return hourlyWeatherObj.getString( "windspeedKmph" );
		}
		catch( JSONException e )
		{
			return "Not Found";
		}
	}
}
