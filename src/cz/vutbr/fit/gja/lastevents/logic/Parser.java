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
	 *
	 * @param apiKey Last.fm api key
	 */
	public Parser(String apiKey)
	{
		this.apiKey = apiKey;
	}


	/**
	 * Get URL of method which get event's data by location.
	 *
	 * @param location specifies a name of location to retrieve events for
	 * @param distance find events within a specified radius
	 * @param limit the number of events
	 * @return URL address to XML data
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
	 *
	 * @param artist the artist name
	 * @param limit the number of events
	 * @return URL address to XML data
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


	/**
	 * Parse Last.fm events by location.
	 *
	 * @param queryUrl url address of Last.fm XML file
	 * @param output save parsed data to Query object
	 * @param type type of query
	 * @return error message
	 */
	public static String parseEvents(String queryUrl, Query output, Query.Types type)
	{
		//http://www.java-tips.org/java-se-tips/javax.xml.parsers/how-to-read-xml-file-in-java.html

		try
		{
			// inicialization of XML parser
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(queryUrl);
			doc.getDocumentElement().normalize();

			// check error
			NodeList lfmList = doc.getElementsByTagName("lfm");
		    Element lfmElement = (Element) lfmList.item(0);
		    String status = lfmElement.getAttribute("status");
		    ////System.out.println("STATUS: " + status);
		    if(status.compareTo("failed") == 0)
		    {
		    	Element errorElement = (Element) lfmElement.getElementsByTagName("error").item(0);
		    	String error = errorElement.getTextContent();
		    	System.out.println("ERROR: " + error);
		    }

			// events node
			NodeList eventsList = doc.getElementsByTagName("events");
		    Element eventsElement = (Element) eventsList.item(0);
		    String keyword;
		    if(type == Query.Types.SEARCH_BY_LOCATION)
		    {
		    	keyword = eventsElement.getAttribute("location");
			    ////System.out.println("LOCATION: " + keyword);
		    }
		    else if(type == Query.Types.SEARCH_BY_ARTIST)
		    {
		    	keyword = eventsElement.getAttribute("artist");
			    ////System.out.println("ARTIST: " + keyword);
		    }
		    else
		    {
		    	return "Unknown query type!";
		    }

		    // create query object
		    output.setQuery(keyword, type);

		    // event nodes
		    NodeList eventList = doc.getElementsByTagName("event");
		    for (int i = 0; i < eventList.getLength(); i++)
		    {
		    	////System.out.println("-----------------------------------------");
		    	Element eventElement = (Element) eventList.item(i);

		    	// id node
		    	Element idElement = (Element) eventElement.getElementsByTagName("id").item(0);
		    	int id = Integer.parseInt( idElement.getTextContent() );
		    	////System.out.println("EVENT #" + i + " ID: " + id);

		    	// title node
		    	Element titleElement = (Element) eventElement.getElementsByTagName("title").item(0);
		    	String title = titleElement.getTextContent();
		    	////System.out.println("EVENT #" + i + " TITLE: " + title);

		    	// url node
		    	Element urlElement = (Element) eventElement.getElementsByTagName("url").item(0);
		    	String url = urlElement.getTextContent();
		    	////System.out.println("EVENT #" + i + " URL: " + url);

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
		    	////System.out.println("EVENT #" + i + " DATE: " + date.toString());

		    	// image node
		    	NodeList imageList = eventElement.getElementsByTagName("image");
		    	Element imageElement = (Element) imageList.item( imageList.getLength()-1 );
		    	String image = imageElement.getTextContent();
		    	////System.out.println("EVENT #" + i + " IMAGE: " + image);

		    	// venue node
		    	Element venueElement = (Element) eventElement.getElementsByTagName("venue").item(0);

		    	// venue name node
		    	Element venueNameElement = (Element) venueElement.getElementsByTagName("name").item(0);
		    	String venueName = venueNameElement.getTextContent();
		    	////System.out.println("EVENT #" + i + " VENUE NAME: " + venueName);

		    	// venue location node
		    	Element locationElement = (Element) venueElement.getElementsByTagName("location").item(0);

		    	// venue location city node
		    	Element locationCityElement = (Element) locationElement.getElementsByTagName("city").item(0);
		    	String locationCity = locationCityElement.getTextContent();
		    	////System.out.println("EVENT #" + i + " LOCATION CITY: " + locationCity);

		    	// venue location country node
		    	Element locationCountryElement = (Element) locationElement.getElementsByTagName("country").item(0);
		    	String locationCountry = locationCountryElement.getTextContent();
		    	////System.out.println("EVENT #" + i + " LOCATION COUNTRY: " + locationCountry);

		    	// venue location node
		    	Element geoElement = (Element) locationElement.getElementsByTagName("geo:point").item(0);

		    	// venue location geo lat node
		    	Element geoLatElement = (Element) geoElement.getElementsByTagName("geo:lat").item(0);
		    	double geoLat = 0;
		    	if( geoLatElement.getTextContent() != "")
		    	{
		    		geoLat = Double.parseDouble( geoLatElement.getTextContent() );
		    	}
		    	////System.out.println("EVENT #" + i + " LOCATION LAT: " + geoLat);

		    	// venue location geo lon node
		    	Element geoLonElement = (Element) geoElement.getElementsByTagName("geo:long").item(0);
		    	double geoLon = 0;
		    	if( geoLonElement.getTextContent() != "")
		    	{
			    	geoLon = Double.parseDouble( geoLonElement.getTextContent() );
			    }
		    	////System.out.println("EVENT #" + i + " LOCATION LON: " + geoLon);

		    	// create event object
		    	Event event = new Event(id, title, url, image, date);
		    	event.setVenue(venueName, locationCity, locationCountry, geoLat, geoLon);

		    	// artist node
		    	Element artistsElement = (Element) eventElement.getElementsByTagName("artists").item(0);
		    	NodeList artistsList = artistsElement.getElementsByTagName("artist");
		    	for (int j = 0; j < artistsList.getLength(); j++)
		    	{
		    		Element artistElement = (Element) artistsList.item(j);
		    		String artist = artistElement.getTextContent();
		    		event.addArtist(artist);
			    	////System.out.println("EVENT #" + i + " ARTIST: " + artist);
		    	}

		    	// tag node
		    	Element tagsElement = (Element) eventElement.getElementsByTagName("tags").item(0);
		    	try
		    	{
			    	NodeList tagsList = tagsElement.getElementsByTagName("tag");
			    	for (int j = 0; j < tagsList.getLength(); j++)
			    	{
			    		Element tagElement = (Element) tagsList.item(j);
			    		String tag = tagElement.getTextContent();
			    		event.addTag(tag);
				    	////System.out.println("EVENT #" + i + " TAG: " + tag);
			    	}
		    	}
		    	catch (Exception e) {}

		    	// add event to query
		    	output.addArtist(event);
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println("EXCEPTION: " + e.toString());
			return e.toString();
		}

		return null;
	}
}
