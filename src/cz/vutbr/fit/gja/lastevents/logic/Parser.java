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

import cz.vutbr.fit.gja.lastevents.storage.Storage;

/**
 * Parser for Last.fm data.
 * Send requests to Last.fm server and get event's data in XML.
 * @author Petr Nohejl <xnohej00@stud.fit.vutbr.cz>
 */
public class Parser
{
	private String apiKey;
	private String geonamesLogin;


	/**
	 * Create parser instance with specific Last.fm API key and Geonames login.
	 *
	 * @param apiKey Last.fm api key
	 * @param geonamesLogin Geonames.org user login
	 */
	public Parser(String apiKey, String geonamesLogin)
	{
		this.apiKey = apiKey;
		this.geonamesLogin = geonamesLogin;
	}


	/**
	 * Get URL of method which get event's data by location.
	 * Using api.last.fm.
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
	 * Using api.last.fm.
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
	 * Get URL of method which search artists by a keyword.
	 * Using api.last.fm.
	 *
	 * @param artist the artist name for match
	 * @param limit the number of artists
	 * @return URL address to XML data
	 */
	public String getArtists(String artist, int limit)
	{
		/*
		 	limit (Optional) : The number of results to fetch per page. Defaults to 50.
			page (Optional) : The page number to fetch. Defaults to first page.
			artist (Required) : The artist name
			api_key (Required) : A Last.fm API key.
		*/

		String url = "http://ws.audioscrobbler.com/2.0/?method=artist.search&artist=" + artist +
			"&limit=" + limit +
			"&api_key=" + apiKey;

		return url;
	}
	
	
	/**
	 * Get URL of method which search locations by a keyword.
	 * Using api.geonames.org.
	 *
	 * @param location the location name for match
	 * @param limit the number of locations
	 * @return URL address to XML data
	 */
	public String getLocations(String location, int limit)
	{
		// http://www.geonames.org/export/geonames-search.html
			
		String url = "http://api.geonames.org/search?q=" + location + 
			"&username=" + geonamesLogin + 
			"&style=short&maxRows=" + limit;

		return url;
	}
	
	
	/**
	 * Get URL of method which search tags by a keyword.
	 * Using api.last.fm.
	 *
	 * @param tag the tag name for match
	 * @param limit the number of tags
	 * @return URL address to XML data
	 */
	public String getTags(String tag, int limit)
	{
		/*
		 	limit (Optional) : The number of results to fetch per page. Defaults to 30.
			page (Optional) : The page number to fetch. Defaults to first page.
			tag (Required) : The tag name
			api_key (Required) : A Last.fm API key.
		*/
		
		String url = "http://ws.audioscrobbler.com/2.0/?method=tag.search&tag=" + tag +
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
	public static String parseEvents(int distance, int limit, String queryUrl, QueryEvent output, QueryEvent.Types type)
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
		    	////System.out.println("ERROR: " + error);
		    	return error;
		    }

			// events node
			NodeList eventsList = doc.getElementsByTagName("events");
		    Element eventsElement = (Element) eventsList.item(0);
		    String keyword;
		    if(type == QueryEvent.Types.SEARCH_BY_LOCATION)
		    {
		    	keyword = eventsElement.getAttribute("location");
		    	keyword = keyword.replaceAll(",.*", "");
			    ////System.out.println("LOCATION: " + keyword);
		    }
		    else if(type == QueryEvent.Types.SEARCH_BY_ARTIST)
		    {
		    	keyword = eventsElement.getAttribute("artist");
			    ////System.out.println("ARTIST: " + keyword);
		    }
		    else
		    {
		    	return "Unknown query type!";
		    }

		    // create query object
		    output.setQuery(keyword, distance, limit, type);

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
		    	output.addEvent(event);
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

	
	/**
	 * Parse Last.fm artists.
	 *
	 * @param queryUrl url address of Last.fm XML file
	 * @param output save parsed data to QueryArtist object
	 * @return error message
	 */
	public static String loadArtists(String queryUrl, QueryArtist output)
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
		    	////System.out.println("ERROR: " + error);
		    	return error;
		    }

		    // results node
			NodeList resultsList = doc.getElementsByTagName("results");
		    Element resultsElement = (Element) resultsList.item(0);
		    String keyword = resultsElement.getAttribute("for");
		    ////System.out.println("KEYWORD: " + keyword);

		    // create query object
		    output.setKeyword(keyword);
		    
		    // artist nodes
		    NodeList artistList = doc.getElementsByTagName("artist");
		    for (int i = 0; i < artistList.getLength(); i++)
		    {
		    	////System.out.println("-----------------------------------------");
		    	Element artistElement = (Element) artistList.item(i);

		    	// name node
		    	Element nameElement = (Element) artistElement.getElementsByTagName("name").item(0);
		    	String name = nameElement.getTextContent();
		    	////System.out.println("ARTIST #" + i + " NAME: " + name);
		    	
		    	output.addName(name);
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
	

	/**
	 * Parse Geonames locations.
	 *
	 * @param keyword queried keyword
	 * @param queryUrl url address of Geonames.org XML file
	 * @param output save parsed data to QueryLocation object
	 * @return error message
	 */
	public static String loadLocations(String keyword, String queryUrl, QueryLocation output)
	{
		//http://www.java-tips.org/java-se-tips/javax.xml.parsers/how-to-read-xml-file-in-java.html

		try
		{
			// inicialization of XML parser
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(queryUrl);
			doc.getDocumentElement().normalize();

		    // create query object
		    output.setKeyword(keyword);

		    // geoname nodes
		    NodeList geonameList = doc.getElementsByTagName("geoname");
		    for (int i = 0; i < geonameList.getLength(); i++)
		    {
		    	////System.out.println("-----------------------------------------");
		    	Element geonameElement = (Element) geonameList.item(i);

		    	// name node
		    	Element nameElement = (Element) geonameElement.getElementsByTagName("name").item(0);
		    	String name = nameElement.getTextContent();
		    	////System.out.println("GEONAME #" + i + " NAME: " + name);
		    	
		    	output.addName(name);
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
	
	
	/**
	 * Parse Last.fm tags.
	 *
	 * @param queryUrl url address of Last.fm XML file
	 * @param output save parsed data to QueryTag object
	 * @return error message
	 */
	public static String loadTags(String queryUrl, QueryTag output)
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
		    	////System.out.println("ERROR: " + error);
		    	return error;
		    }

		    // results node
			NodeList resultsList = doc.getElementsByTagName("results");
		    Element resultsElement = (Element) resultsList.item(0);
		    String keyword = resultsElement.getAttribute("for");
		    ////System.out.println("KEYWORD: " + keyword);

		    // create query object
		    output.setKeyword(keyword);
		    
		    // artist nodes
		    NodeList tagList = doc.getElementsByTagName("tag");
		    for (int i = 0; i < tagList.getLength(); i++)
		    {
		    	////System.out.println("-----------------------------------------");
		    	Element tagElement = (Element) tagList.item(i);

		    	// name node
		    	Element nameElement = (Element) tagElement.getElementsByTagName("name").item(0);
		    	String name = nameElement.getTextContent();
		    	////System.out.println("TAG #" + i + " NAME: " + name);
		    	
		    	output.addName(name);
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
	

	/**
	 * Load Last.fm events from storage or XML.
	 *
	 * @param keyword for check keyword in storage
	 * @param queryUrl url address of Last.fm XML file
	 * @param output save data from storage or XML to Query object
	 * @param type type of query
	 * @return error message
	 */
	public static String loadEvents(String keyword, int distance, int limit, String queryUrl, QueryEvent output, QueryEvent.Types type)
	{	
		// Pseudokod:
		// - zkontroluj storage zda je tam hledany zaznam		
		// - pokud je, zkontroluj timeout
		// -		pokud je v poradku, vrat zaznam ze storage
		// -		jinak nacti data z XML a aktualizuj ve storage
		// - jinak nacti data z XML a uloz do storage					
		
		// create storage object
		QueryEvent storageQuery;
		String err = null;
		
		// try load data from storage
		storageQuery = Storage.loadData(keyword, distance, limit);
		
		// data is in cache
		if(storageQuery!=null)
		{
			////Storage.debugInfo(storageQuery, "LOAD");
			
			// check timeout			
			boolean timeout = Storage.checkTimeout(storageQuery.getDate());
			
			// fresh data is in storage
			if(!timeout) 
			{
				// copy query object
				output.setQuery(storageQuery.getKeyword(), 
						storageQuery.getDistance(), 
						storageQuery.getLimit(), 
						storageQuery.getType());
				output.setDate(storageQuery.getDate());
        		for(int i = 0;i < storageQuery.getEvents().size();i++)
				{
        			Event e = new Event(
        					storageQuery.getEvents().get(i).getId(),
        					storageQuery.getEvents().get(i).getTitle(),
        					storageQuery.getEvents().get(i).getUrl(),
        					storageQuery.getEvents().get(i).getImage(),
        					storageQuery.getEvents().get(i).getDate()
        			);
        			e.setVenue(
        					storageQuery.getEvents().get(i).getVenueName(),
        					storageQuery.getEvents().get(i).getVenueCity(),
        					storageQuery.getEvents().get(i).getVenueCountry(),
        					storageQuery.getEvents().get(i).getVenueLat(),
        					storageQuery.getEvents().get(i).getVenueLon()
        			);
        			for(int j = 0;j < storageQuery.getEvents().get(i).getArtists().size(); j++)
        					e.addArtist(storageQuery.getEvents().get(i).getArtists().get(j));
        			for(int j = 0;j < storageQuery.getEvents().get(i).getTags().size(); j++)
    					e.addTag(storageQuery.getEvents().get(i).getTags().get(j));

        			output.addEvent(e);					
				}
				
				////Storage.debugInfo(output, "FROM CACHE");
				return err;
			}
			// old data is in storage
			else
			{
				err = parseEvents(distance, limit, queryUrl, output, type);
				if(err==null) 
				{
					Storage.deleteData(storageQuery.getKeyword(), storageQuery.getDistance(), storageQuery.getLimit());
					Storage.storeData(output);
				}
				return err;
			}			
		}
		// data is not in cache
		else
		{
			err = parseEvents(distance, limit, queryUrl, output, type);
			if(err==null) Storage.storeData(output);
			return err;
		}
	}
}
