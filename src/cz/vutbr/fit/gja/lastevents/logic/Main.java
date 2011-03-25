package cz.vutbr.fit.gja.lastevents.logic;

public class Main 
{
	public static void main (String arg[])
	{
		System.out.println("Start app");
		
		// API Key is c8e71fc5e7255264940483b4228c010f
		// GEO.GET_EVENTS: http://www.last.fm/api/show?service=270
		// ARTIST.GET_EVENTS: http://www.last.fm/api/show?service=117
		
		
		LastApi lastApi = new LastApi("c8e71fc5e7255264940483b4228c010f");
		
		// TODO - osetrit errory
		// TODO - osetrit diakritiku
		
		String res1 = lastApi.getEventsByLocation("Brno", 10, 1);
		String res2 = lastApi.getEventsByArtist("Wohnout", 1);
		
		// TODO - parse xml
		// TODO - data ulozit do objektu
		
		System.out.println(res1);
		//System.out.println(res2);		
	}
}
