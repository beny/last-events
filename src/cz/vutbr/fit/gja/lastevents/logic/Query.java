package cz.vutbr.fit.gja.lastevents.logic;

import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;

/**
 * Data class represents one search query.
 */
public class Query
{
	private String keyword;
	private Query.Types type;
	private Date date;
	private ArrayList<Event> events;

	public enum Types { SEARCH_BY_ARTIST, SEARCH_BY_LOCATION };


	/**
	 * Create query.
	 */
	public Query()
	{
		this.date = new Date();
		this.events = new ArrayList<Event>();
	}


	/**
	 * Create query.
	 */
	public Query(String keyword, Query.Types type)
	{
		this.keyword = keyword;
		this.type = type;

		this.date = new Date();
		this.events = new ArrayList<Event>();
	}


	/**
	 * Set query.
	 */
	public void setQuery(String keyword, Query.Types type)
	{
		this.keyword = keyword;
		this.type = type;
	}


	/**
	 * Add event to query.
	 */
	public void addArtist(Event event)
	{
		this.events.add(event);
	}


	/**
	 * Print query info to standard output.
	 */
	public void printQuery()
	{
		System.out.println("----------------------------------------");
		System.out.println("KEYWORD: " + keyword);
		System.out.println("TYPE: " + type.toString());
		System.out.println("DATE: " + date.toString());
		for(int i=0;i<events.size();i++)
		{
			System.out.println("----------------------------------------");
			System.out.println("EVENT #" + i + ":");
			events.get(i).printEvent();
		}
	}

	/**
	 * @return JSON representation of query result
	 */
	@SuppressWarnings("unchecked")
	public String getJSONResult(){

		JSONArray eventsJSON = new JSONArray();
		for(int i=0;i<events.size();i++){
			eventsJSON.add(events.get(i).getJSONEvent());
		}

		return eventsJSON.toJSONString();

	}

	public String getKeyword() { return this.keyword; }
	public Query.Types getType() { return this.type; }
	public Date getDate() { return this.date; }
	public ArrayList<Event> getEvents() { return this.events; }
}
