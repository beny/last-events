package cz.vutbr.fit.gja.lastevents.logic;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.Key;

/**
 * Data class represents one last-event. 
 */
@PersistenceCapable
public class Event 
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private int id;
	@Persistent
	private String title;	
	@Persistent
	private String url;	
	@Persistent
	private String image;
	@Persistent
	private Date date;
		
	@Persistent
	private String venueName;
	@Persistent
	private String venueCity;
	@Persistent
	private String venueCountry;
	@Persistent
	private double venueLat; // optional
	@Persistent
	private double venueLon; // optional
	
	@Persistent
	private ArrayList<String> artists;
	@Persistent
	private ArrayList<String> tags; // optional
	
	
	/**
	 * Create event.
	 * Set the most important data.
	 */
	public Event(int id, String title, String url, String image, Date date)
	{
		this.id = id;
		this.title = title;
		this.url = url;
		this.image = image;
		this.date = date;
		
		this.artists = new ArrayList<String>();
		this.tags = new ArrayList<String>();
	}
	
	
	public Event(Event copy) 
	{    
	    this.id = copy.id;
		this.title = copy.title;	
		this.url = copy.url;	
		this.image = copy.image;
		this.date = copy.date;			
		this.venueName = copy.venueName;
		this.venueCity = copy.venueCity;
		this.venueCountry = copy.venueCountry;
		this.venueLat = copy.venueLat;
		this.venueLon = copy.venueLon;
		this.artists = copy.artists;
		this.tags = copy.tags;
	}
	
	
	/**
	 * Set venue data. 
	 */
	public void setVenue(String venueName, String venueCity, String venueCountry, double venueLat, double venueLon)
	{
		this.venueName = venueName;
		this.venueCity = venueCity;
		this.venueCountry = venueCountry;
		this.venueLat = venueLat;
		this.venueLon = venueLon;
	}
	
	
	/**
	 * Add artist to event. 
	 */
	public void addArtist(String artist)
	{
		this.artists.add(artist);
	}
	
	
	/**
	 * Add event's tag. 
	 */
	public void addTag(String tag)
	{
		this.tags.add(tag);
	}
	
	
	/**
	 * Print event info to standard output. 
	 */
	public void printEvent()
	{
		System.out.println("ID: " + id);
		System.out.println("TITLE: " + title);
		System.out.println("URL: " + url);
		System.out.println("IMAGE: " + image);
		System.out.println("DATE: " + date.toString());
		System.out.println("VENUE NAME: " + venueName);
		System.out.println("VENUE CITY: " + venueCity);
		System.out.println("VENUE COUNTRY: " + venueCountry);
		System.out.println("VENUE LAT: " + venueLat);
		System.out.println("VENUE LON: " + venueLon);
		for(int i=0;i<artists.size();i++)
		{
			System.out.println("ARTIST: " + artists.get(i));
		}
		for(int i=0;i<tags.size();i++)
		{
			System.out.println("TAG: " + tags.get(i));
		}
	}
	
	public String getEvent(){
		
		String result = new String();
		result += "ID: " + id;
		result += "TITLE: " + title;
		result += "URL: " + url;
		result += "IMAGE: " + image;
		result += "DATE: " + date.toString();
		result += "VENUE NAME: " + venueName;
		result += "VENUE CITY: " + venueCity;
		result += "VENUE COUNTRY: " + venueCountry;
		result += "VENUE LAT: " + venueLat;
		result += "VENUE LON: " + venueLon;
		for(int i=0;i<artists.size();i++)
		{
			result += "ARTIST: " + artists.get(i);
		}
		for(int i=0;i<tags.size();i++)
		{
			result += "TAG: " + tags.get(i);
		}
		
		return result;
	}
	
	/**
	 * @return JSON objekt s eventem
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getJSONEvent(){

		JSONObject obj = new JSONObject();
		obj.put("id", new Integer(id));
		obj.put("title", title);
		obj.put("url", url);
		obj.put("image", image);
		obj.put("date", date.toString());
		
		// venue object
		JSONObject venueJSON = new JSONObject();
		venueJSON.put("name", venueName);
		venueJSON.put("city", venueCity);
		venueJSON.put("country", venueCountry);
		venueJSON.put("lat", venueLat);
		venueJSON.put("lon", venueLon);
		
		obj.put("venue", venueJSON);
		
		JSONArray artistsJSON = new JSONArray();
		for(int i=0;i<artists.size();i++){
			artistsJSON.add(artists.get(i));
		}
		obj.put("artists", artistsJSON);
		
		JSONArray tagsJSON = new JSONArray();
		for(int i=0;i<tags.size();i++){
			tagsJSON.add(tags.get(i));
		}
		obj.put("tags", tagsJSON);
		
		return obj;
	}
	
	
	public Key getKey() { return this.key; }
	public int getId() { return this.id; }
	public String getTitle() { return this.title; }	
	public String getUrl() { return this.url; }	
	public String getImage() { return this.image; }
	public Date getDate() { return this.date; }	
	public String getVenueName() { return this.venueName; }
	public String getVenueCity() { return this.venueCity; }
	public String getVenueCountry() { return this.venueCountry; }
	public double getVenueLat() { return this.venueLat; }
	public double getVenueLon() { return this.venueLon; }	
	public ArrayList<String> getArtists() { return this.artists; }
	public ArrayList<String> getTags() { return this.tags; } 
}
