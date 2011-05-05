package cz.vutbr.fit.gja.lastevents.storage;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cz.vutbr.fit.gja.lastevents.logic.Event;
import cz.vutbr.fit.gja.lastevents.logic.QueryEvent;

/**
 * Data storage.
 * @author Petr Nohejl <xnohej00@stud.fit.vutbr.cz>
 */
public class Storage 
{
	public static final long TIMEOUT_INTERVAL = 24 * 3600000; // in millisecond: x * hours

	
	/**
	 * Store object into the database.
	 */
	public static void storeData(QueryEvent data)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try 
        {
            pm.makePersistent(data);

            ////debugInfo(data, "STORE");
        }
        finally 
		{
        	pm.close();
		}    
	}
	
	
	/**
	 * Delete object from the database.
	 */
	@SuppressWarnings("unchecked")
	public static void deleteData(String keyword, int distance, int limit)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<QueryEvent> resultList = null;
		QueryEvent result = null;
		Query query = pm.newQuery(QueryEvent.class);
        try
        {           
        	query.setFilter("keyword == keywordParam && " +
	        		"distance == distanceParam && " +
	        		"limit == limitParam");	
	        query.declareParameters("String keywordParam, int distanceParam, int limitParam");
	        query.setRange(0, 1);

	        resultList = (List<QueryEvent>) query.execute(keyword, distance, limit);
	        if(resultList.size()==1) 
        	{
        		result = (QueryEvent) resultList.get(0);

        		////debugInfo(result, "DELETE");
        		
        		pm.deletePersistent(result);
        	}
		}
        finally 
		{
        	query.closeAll();
        	pm.close();
		}
	}
	

	/**
	 * Load object from database.
	 */
	@SuppressWarnings("unchecked")
	public static QueryEvent loadData(String keyword, int distance, int limit)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<QueryEvent> resultList = null;
		QueryEvent result = null;
		QueryEvent resultReturn = null;
		Query query = pm.newQuery(QueryEvent.class);
        try
        {           
	        query.setFilter("keyword == keywordParam && " +
	        		"distance == distanceParam && " +
	        		"limit == limitParam");	
	        query.declareParameters("String keywordParam, int distanceParam, int limitParam");
	        query.setRange(0, 1);
	        //query.setOrdering("date desc");

	        resultList = (List<QueryEvent>) query.execute(keyword, distance, limit);
	        if(resultList.size()==1) 
        	{
        		result = (QueryEvent) resultList.get(0);

        		// copy query object
        		resultReturn = new QueryEvent(result.getKeyword(), 
        				result.getDistance(), 
        				result.getLimit(), 
        				result.getType());
        		resultReturn.setKey(result.getKey());
        		resultReturn.setDate(result.getDate());
        		for(int i = 0;i < result.getEvents().size();i++)
				{
        			Event e = new Event(
        					result.getEvents().get(i).getId(),
        					result.getEvents().get(i).getTitle(),
        					result.getEvents().get(i).getUrl(),
        					result.getEvents().get(i).getImage(),
        					result.getEvents().get(i).getDate()
        			);
        			e.setVenue(
        					result.getEvents().get(i).getVenueName(),
        					result.getEvents().get(i).getVenueCity(),
        					result.getEvents().get(i).getVenueCountry(),
        					result.getEvents().get(i).getVenueLat(),
        					result.getEvents().get(i).getVenueLon()
        			);
        			for(int j = 0;j < result.getEvents().get(i).getArtists().size(); j++)
        					e.addArtist(result.getEvents().get(i).getArtists().get(j));
        			for(int j = 0;j < result.getEvents().get(i).getTags().size(); j++)
    					e.addTag(result.getEvents().get(i).getTags().get(j));

        			resultReturn.addEvent(e);					
				}
        	}
		}
        finally 
		{
        	query.closeAll();
        	pm.close();
		}
        
        return resultReturn;
	}
		

	/**
	 * Check if query is timeouted and needs to be refreshed.
	 */
	public static boolean checkTimeout(Date queryDate)
	{
		Date currentDate = new Date();		
		long diff = currentDate.getTime() - queryDate.getTime();		
		return (diff > TIMEOUT_INTERVAL);
	}
	
	
	/**
	 * Show debug info.
	 */
	public static void debugInfo(QueryEvent query, String label)
	{
		System.out.println(label + ": " + query.getKeyword() + "|" + query.getDistance() + "|" + query.getLimit() + "|" + query.getType() + "|" + query.getDate() + "|" + query.getEvents().size());
	}
}
