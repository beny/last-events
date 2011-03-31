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
		
	private String venueName;
	private String venueCity;
	private String venueCountry;
	private double venueLat; // optional
	private double venueLon; // optional
	
	private ArrayList<String> artists;
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
