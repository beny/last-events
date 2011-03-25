package cz.vutbr.fit.gja.lastevents.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * Data class represents one search query. 
 */
public class Query 
{
	private String keyword;
	private int type;
	private Date date;
	private ArrayList<Event> events;
	
	enum Types { SEARCH_BY_ARTIST, SEARCH_BY_LOCATION };
	
	
	/**
	 * Create query. 
	 */
	public Query(String keyword, int type)
	{
		this.keyword = keyword;
		this.type = type;
		
		this.date = new Date();
		this.events = new ArrayList<Event>();
	}
	
	
	/**
	 * Add event to query. 
	 */
	public void addArtist(Event event)
	{
		this.events.add(event);
	}
	
	
	public String getKeyword() { return this.keyword; }
	public int getType() { return this.type; }
	public Date getDate() { return this.date; }
	public ArrayList<Event> getEvents() { return this.events; }
}
