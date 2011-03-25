package cz.vutbr.fit.gja.lastevents.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * Data class represents one last-event. 
 */
public class Event 
{
	private int id;
	private String title;	
	private String url;	
	private String image;
	private Date date;
	private Date parsedDate;
		
	private String venueName;
	private String venueCity;
	private String venueCountry;
	private int venueLat;
	private int venueLon;
	
	private ArrayList<String> artists;
	private ArrayList<String> tags;
	
	
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
		
		this.parsedDate = new Date();
		this.artists = new ArrayList<String>();
		this.tags = new ArrayList<String>();
	}
	
	
	/**
	 * Set venue data. 
	 */
	public void setVenue(String venueName, String venueCity, String venueCountry, int venueLat, int venueLon)
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
	
	
	public int getId() { return this.id; }
	public String getTitle() { return this.title; }	
	public String getUrl() { return this.url; }	
	public String getImage() { return this.image; }
	public Date getDate() { return this.date; }
	public Date getParsedDate() { return this.parsedDate; }		
	public String getVenueName() { return this.venueName; }
	public String getVenueCity() { return this.venueCity; }
	public String getVenueCountry() { return this.venueCountry; }
	public int getVenueLat() { return this.venueLat; }
	public int getVenueLon() { return this.venueLon; }	
	public ArrayList<String> getArtists() { return this.artists; }
	public ArrayList<String> getTags() { return this.tags; } 
}
