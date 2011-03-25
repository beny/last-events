package cz.vutbr.fit.gja.lastevents.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Last.fm API methods.
 * Send requests to Last.fm server and get event's data in XML. 
 */
public class LastApi 
{
	private String apiKey;
	
	
	/**
	 * Create API instance with specific API key.
	 */
	public LastApi(String apiKey)
	{
		this.apiKey = apiKey;
	}
	
	
	/**
	 * Get event's data by location.
	 * @return XML data
	 */
	public String getEventsByLocation(String location, int distance, int limit)
	{
		/*
		 	lat (Optional) : Specifies a latitude value to retrieve events for (service returns nearby events by default)
			location (Optional) : Specifies a location to retrieve events for (service returns nearby events by default)
			long (Optional) : Specifies a longitude value to retrieve events for (service returns nearby events by default)
			distance (Optional) : Find events within a specified radius (in kilometres)
			limit (Optional) : The number of results to fetch per page. Defaults to 10.
			page (Optional) : The page number to fetch. Defaults to first page.
			api_key (Required) : A Last.fm API key.
		*/
		
		String url = "http://ws.audioscrobbler.com/2.0/?method=geo.getevents&location=" + location + 
			"&distance=" + distance + 
			"&limit=" + limit + 
			"&api_key=" + apiKey;
		
		String xml = "";
		try 
		{
			xml = getFile(url);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println(e.toString());
		}
		
		return xml;
	}
	
	
	/**
	 * Get event's data by artist.
	 * @return XML data
	 */
	public String getEventsByArtist(String artist, int limit)
	{
		/*
		 	artist (Required (unless mbid)] : The artist name
			mbid (Optional) : The musicbrainz id for the artist
			autocorrect[0|1] (Optional) : Transform misspelled artist names into correct artist names, returning the correct version instead. The corrected artist name will be returned in the response.
			limit (Optional) : The number of results to fetch per page. Defaults to 50.
			page (Optiona) : The page number to fetch. Defaults to first page.
			api_key (Required) : A Last.fm API key.
		*/

		String url = "http://ws.audioscrobbler.com/2.0/?method=artist.getevents&artist=" + artist + 
			"&autocorrect=1" + 
			"&limit=" + limit + 
			"&api_key=" + apiKey;
		
		
		String xml = "";
		try 
		{
			xml = getFile(url);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println(e.toString());
		}
		
		return xml;
	}
	
	
	/**
	 * Send HTTP request and download text data.
	 */
	private String getFile(String urlString) throws Exception 
	{
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        InputStreamReader stream = new InputStreamReader(connection.getInputStream());        
        BufferedReader in = new BufferedReader(stream);
        
        String inputLine;
        StringBuilder result = new StringBuilder("");
        String newline = System.getProperty("line.separator");

        while ((inputLine = in.readLine()) != null)
        {
        	result.append(inputLine);
        	result.append(newline);
        }

        in.close();       
		return result.toString();
    }

}
