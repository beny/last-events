package cz.vutbr.fit.gja.lastevents.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

import cz.vutbr.fit.gja.lastevents.logic.Event;
import cz.vutbr.fit.gja.lastevents.logic.QueryEvent;

/**
 * Data storage.
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

            debugInfo(data, "STORE");
        }
        finally 
		{
        	pm.close();
		}    
	}
	

	/**
	 * Load object from database.
	 */
	@SuppressWarnings("unchecked")
	public static QueryEvent loadData(String keyword)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<QueryEvent> resultList = null;
		QueryEvent result = null;
		Query query = pm.newQuery(QueryEvent.class);
        try
        {           
	        query.setFilter("keyword == keywordParam");	
	        query.declareParameters("String keywordParam");
	        query.setRange(0, 1);
	        //query.setOrdering("date desc");

	        resultList = (List<QueryEvent>) query.execute(keyword);
	        if(resultList.size()==1) result = (QueryEvent) resultList.get(0);
		}
        finally 
		{
        	query.closeAll();
        	pm.close();
		}
        
        return result;
	}
	
	
	
	/**
	 * Update object in database.
	 */
	public static void updateData(Key updatedObject, ArrayList<Event> newEvents)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			QueryEvent q = pm.getObjectById(QueryEvent.class, updatedObject);
						
			q.setDate(new Date());
			// TODO zkonstrolovat zda toto funguje!!!
			q.setEvents(newEvents);	
			
			debugInfo(q, "UPDATE");
		}
		finally 
		{
        	pm.close();
		}
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
	
	
	public static void debugInfo(QueryEvent query, String label)
	{
		System.out.println(label + ": " + query.getKeyword() + "|" + query.getType() + "|" + query.getDate() + "|" + query.getEvents().size());
	}
}
