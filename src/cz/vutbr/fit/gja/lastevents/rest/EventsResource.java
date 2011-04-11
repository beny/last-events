package cz.vutbr.fit.gja.lastevents.rest;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import cz.vutbr.fit.gja.lastevents.logic.Parser;
import cz.vutbr.fit.gja.lastevents.logic.Query;

/**
 * Resource which has only one representation.
 * 
 */
public class EventsResource extends ServerResource {

    @Get
    public String represent() {
    	Parser lastApi = new Parser("c8e71fc5e7255264940483b4228c010f");
    	String url2 = lastApi.getEventsByArtist("Wohnout", 5);
    	Query query2 = new Query();
    	
		String res2 = Parser.parseEvents(url2, query2, Query.Types.SEARCH_BY_ARTIST);
		if(res2 == "") return query2.getJSONQuery();
		else return "";
    }

}