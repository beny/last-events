package cz.vutbr.fit.gja.lastevents.logic;

import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONArray;

/**
 * Data class represents one search query.
 * @author Petr Nohejl <xnohej00@stud.fit.vutbr.cz>
 */
@PersistenceCapable
public class QueryEvent
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String keyword;
	@Persistent
	private int distance;
	@Persistent
	private int limit;
	@Persistent
	private QueryEvent.Types type;
	@Persistent
	private Date date;
	@Persistent
	private ArrayList<Event> events;

	public enum Types { SEARCH_BY_ARTIST, SEARCH_BY_LOCATION };


	/**
	 * Create query.
	 */
	public QueryEvent()
	{
		this.date = new Date();
		this.events = new ArrayList<Event>();
	}


	/**
	 * Create query.
	 */
	public QueryEvent(String keyword, int distance, int limit, QueryEvent.Types type)
	{
		this.keyword = keyword;
		this.distance = distance;
		this.limit = limit;
		this.type = type;

		this.date = new Date();
		this.events = new ArrayList<Event>();
	}


	/**
	 * Set query.
	 */
	public void setQuery(String keyword, int distance, int limit, QueryEvent.Types type)
	{
		this.keyword = keyword;
		this.distance = distance;
		this.limit = limit;
		this.type = type;
	}


	/**
	 * Add event to query.
	 */
	public void addEvent(Event event)
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
		System.out.println("DIST: " + distance);
		System.out.println("LIMIT: " + limit);
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

	public Key getKey() { return this.key; }
	public String getKeyword() { return this.keyword; }
	public int getDistance() { return this.distance; }
	public int getLimit() { return this.limit; }
	public QueryEvent.Types getType() { return this.type; }
	public Date getDate() { return this.date; }
	public ArrayList<Event> getEvents() { return this.events; }
	
	public void setKey(Key key) { this.key = key; }
	public void setDate(Date date) { this.date = date; }
	public void setEvents(ArrayList<Event> events) { this.events = events; }
}
