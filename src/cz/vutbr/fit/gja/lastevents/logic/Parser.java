package cz.vutbr.fit.gja.lastevents.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Parser for Last.fm data.
 * Send requests to Last.fm server and get event's data in XML. 
 */
public class Parser 
{
	private String apiKey;
	
	
	/**
	 * Create parser instance with specific Last.fm API key.
	 */
	public Parser(String apiKey)
	{
		this.apiKey = apiKey;
	}
	
	
	/**
	 * Get URL of method which get event's data by location.
	 * @return URL 
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

		return url;
	}
	
	
	/**
	 * Get URL of method which get event's data by artist.
	 * @return URL
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
		
		return url;
	}

	
	//http://www.java-tips.org/java-se-tips/javax.xml.parsers/how-to-read-xml-file-in-java.html
	//http://xmlbeans.apache.org/
	
	/*	 
	 <?xml version="1.0" encoding="utf-8"?>
<lfm status="ok">
<events xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#" location="Brno, Czech Republic" page="1" perPage="1" totalPages="59" total="59">
    <event xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#" >
  <id>1816932</id>
  <title>Caliban</title>
  <artists>
    <artist>Caliban</artist>
    <headliner>Caliban</headliner>
  </artists>
    <venue>
    <id>8881191</id>
    <name>FAVAL music circus (Favál)</name>
    <location>
      <city>Brno</city>
      <country>Czech Republic</country>
      <street>Křížkovského 22</street>
      <postalcode>603 00</postalcode>
      <geo:point>
         <geo:lat>49.184701</geo:lat>
         <geo:long>16.579833</geo:long>
      </geo:point>
    </location>
    <url>http://www.last.fm/venue/8881191+FAVAL+music+circus+%28Fav%C3%A1l%29</url>
    <website>http://www.faval.cz/</website>
    <phonenumber></phonenumber>
    <image size="small">http://userserve-ak.last.fm/serve/34/6164313.gif</image>
	<image size="medium">http://userserve-ak.last.fm/serve/64/6164313.gif</image>
	<image size="large">http://userserve-ak.last.fm/serve/126/6164313.gif</image>
	<image size="extralarge">http://userserve-ak.last.fm/serve/252/6164313.gif</image>
	<image size="mega">http://userserve-ak.last.fm/serve/_/6164313/FAVAL+music+circus+Favl+Faval_logo.gif</image>
  </venue>    <startDate>Sat, 26 Mar 2011 20:00:00</startDate>
  <description></description>
  <image size="small">http://userserve-ak.last.fm/serve/34/2124885.jpg</image>
  <image size="medium">http://userserve-ak.last.fm/serve/64/2124885.jpg</image>
  <image size="large">http://userserve-ak.last.fm/serve/126/2124885.jpg</image>
  <image size="extralarge">http://userserve-ak.last.fm/serve/252/2124885.jpg</image>
  <attendance>12</attendance>
  <reviews>0</reviews>
  <tag>lastfm:event=1816932</tag>    
  <url>http://www.last.fm/event/1816932+Caliban+at+FAVAL+music+circus+%28Fav%C3%A1l%29+on+26+March+2011</url>
  <website></website>
    <tickets>
  </tickets>
    <cancelled>1</cancelled>
    <tags>
      <tag>metalcore</tag>
      <tag>hardcore</tag>
      <tag>metal</tag>
    </tags>
    </event></events></lfm>
	 */
	
	
	/**
	 * Parse Last.fm events by location.
	 */
	public static String parseEventsByLocation(String queryUrl, Query output)
	{
		String errorMessage = "";

		try 
		{
			// inicialization of XML parser
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(queryUrl);			
			doc.getDocumentElement().normalize();
			
			// XML root node
			//Element root = doc.getDocumentElement();			
			
			// events node 
			NodeList eventsList = doc.getElementsByTagName("events");
		    Element eventsElement = (Element) eventsList.item(0);
		    String location = eventsElement.getAttribute("location");
		    System.out.println("LOCATION: " + location);
		    
		    // event nodes
		    NodeList eventList = doc.getElementsByTagName("event");
		    for (int i = 0; i < eventList.getLength(); i++)
		    {		    	
		    	System.out.println("-----------------------------------------");
		    	Element eventElement = (Element) eventList.item(i);
		    	
		    	// id node
		    	Element idElement = (Element) eventElement.getElementsByTagName("id").item(0);
		    	int id = Integer.parseInt( idElement.getTextContent() );
		    	System.out.println("EVENT #" + i + " ID: " + id);
		    	
		    	// title node
		    	Element titleElement = (Element) eventElement.getElementsByTagName("title").item(0);
		    	String title = titleElement.getTextContent();
		    	System.out.println("EVENT #" + i + " TITLE: " + title);
		    	
		    	// url node
		    	Element urlElement = (Element) eventElement.getElementsByTagName("url").item(0);
		    	String url = urlElement.getTextContent();
		    	System.out.println("EVENT #" + i + " URL: " + url);
		    	
		    	// date node
		    	Element startDateElement = (Element) eventElement.getElementsByTagName("startDate").item(0);
		    	String startDate = startDateElement.getTextContent();
		    	Date date;
		    	try
		    	{
		    		DateFormat myDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);
		    		date = myDateFormat.parse(startDate);
		    	}
		    	catch (Exception e) 
				{
					date = new Date(0);
				}		    	
		    	System.out.println("EVENT #" + i + " DATE: " + date.toString());
		    	
		    	// image node
		    	NodeList imageList = eventElement.getElementsByTagName("image");
		    	Element imageElement = (Element) imageList.item( imageList.getLength()-1 );
		    	String image = imageElement.getTextContent();
		    	System.out.println("EVENT #" + i + " IMAGE: " + image);
		    	
		    	// venue node
		    	Element venueElement = (Element) eventElement.getElementsByTagName("venue").item(0);
		    	
		    	// venue name node
		    	Element venueNameElement = (Element) venueElement.getElementsByTagName("name").item(0);
		    	String venueName = venueNameElement.getTextContent();
		    	System.out.println("EVENT #" + i + " VENUE NAME: " + venueName);
		    	
		    	// venue location node
		    	Element locationElement = (Element) venueElement.getElementsByTagName("location").item(0);
		    	
		    	// venue location city node
		    	Element locationCityElement = (Element) locationElement.getElementsByTagName("city").item(0);
		    	String locationCity = locationCityElement.getTextContent();
		    	System.out.println("EVENT #" + i + " LOCATION CITY: " + locationCity);
		    	
		    	// venue location country node
		    	Element locationCountryElement = (Element) locationElement.getElementsByTagName("country").item(0);
		    	String locationCountry = locationCountryElement.getTextContent();
		    	System.out.println("EVENT #" + i + " LOCATION COUNTRY: " + locationCountry);
		    	
		    	// venue location node
		    	Element geoElement = (Element) locationElement.getElementsByTagName("geo:point").item(0);
		    	
		    	// venue location geo lat node
		    	Element geoLatElement = (Element) geoElement.getElementsByTagName("geo:lat").item(0);
		    	float geoLat = Float.parseFloat( geoLatElement.getTextContent() );
		    	System.out.println("EVENT #" + i + " LOCATION LAT: " + geoLat);
		    	
		    	// venue location geo lon node
		    	Element geoLonElement = (Element) geoElement.getElementsByTagName("geo:long").item(0);
		    	float geoLon = Float.parseFloat( geoLonElement.getTextContent() );
		    	System.out.println("EVENT #" + i + " LOCATION LON: " + geoLon);
		    	
		    	// artists node
		    	Element artistsElement = (Element) eventElement.getElementsByTagName("artists").item(0);
		    	
		    	// artist node
		    	NodeList artistsList = artistsElement.getElementsByTagName("artist");
		    	for (int j = 0; j < artistsList.getLength(); j++)
		    	{
		    		Element artistElement = (Element) artistsList.item(j);
		    		String artist = artistElement.getTextContent();
			    	System.out.println("EVENT #" + i + " ARTIST: " + artist);
		    	}
		    	
		    	// tags node
		    	Element tagsElement = (Element) eventElement.getElementsByTagName("tags").item(0);
		    	
		    	// tag node	
		    	try
		    	{
			    	NodeList tagsList = tagsElement.getElementsByTagName("tag");
			    	for (int j = 0; j < tagsList.getLength(); j++)
			    	{
			    		Element tagElement = (Element) tagsList.item(j);
			    		String tag = tagElement.getTextContent();
				    	System.out.println("EVENT #" + i + " TAG: " + tag);
			    	}
		    	}
		    	catch (Exception e) {}
		    }		    
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return e.toString();
		}

		return errorMessage;
	}
	
	
	/**
	 * Parse Last.fm events by artist.
	 */
	public static String parseEventsByArtist(String queryUrl, Query output)
	{
		String errorMessage = "";
		
		return errorMessage;
	}

}
