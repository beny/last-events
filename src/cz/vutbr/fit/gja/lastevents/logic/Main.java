package cz.vutbr.fit.gja.lastevents.logic;

public class Main 
{
	public static void main (String arg[])
	{
		System.out.println("Start app");
		
		// API Key is c8e71fc5e7255264940483b4228c010f
		// GEO.GET_EVENTS: http://www.last.fm/api/show?service=270
		// ARTIST.GET_EVENTS: http://www.last.fm/api/show?service=117
		
		
		Parser lastApi = new Parser("c8e71fc5e7255264940483b4228c010f");
		
		// TODO - osetrit errory
		// TODO - osetrit diakritiku
		
		String res1 = lastApi.getEventsByLocation("Brno", 10, 5);
		String res2 = lastApi.getEventsByArtist("Wohnout", 5);
		
		// TODO - parse xml
		
		Query query1 = null;
		Query query2 = null;
		
		String msg1 = Parser.parseEventsByLocation(res1, query1);
		//String msg2 = Parser.parseEventsByArtist(res2, query2);
		
		
		
		// TODO - data ulozit do objektu
		
		//System.out.println(res1);
		//System.out.println(res2);		
		
		// TODO - vygenerovat XML pro JavaScript
		// 3 typy: events (mapa), artists (naseptavac), location (naseptavac)
	}
}
