package cz.vutbr.fit.gja.lastevents.logic;

/**
 * Testing class.
 */
public class Main 
{
	public static void main (String arg[])
	{
		System.out.println("Start app");
		
		// API Key is c8e71fc5e7255264940483b4228c010f
		// GEO.GET_EVENTS: http://www.last.fm/api/show?service=270
		// ARTIST.GET_EVENTS: http://www.last.fm/api/show?service=117
				
		Parser lastApi = new Parser("c8e71fc5e7255264940483b4228c010f", "lastevents");

		String url1 = lastApi.getEventsByLocation("Brno", 10, 5);
		String url2 = lastApi.getEventsByArtist("Wohnout", 5);
		
		System.out.println(url1);
		System.out.println(url2);

		QueryEvent query1 = new QueryEvent();
		QueryEvent query2 = new QueryEvent();
		
		String res1 = Parser.parseEvents(url1, query1, QueryEvent.Types.SEARCH_BY_LOCATION);
		String res2 = Parser.parseEvents(url2, query2, QueryEvent.Types.SEARCH_BY_ARTIST);
		
		if(res1 == "") query1.printQuery();
		if(res2 == "") query2.printQuery();
	}
}
