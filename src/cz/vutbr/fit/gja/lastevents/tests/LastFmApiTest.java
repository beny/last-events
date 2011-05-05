package cz.vutbr.fit.gja.lastevents.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import cz.vutbr.fit.gja.lastevents.logic.Parser;
import cz.vutbr.fit.gja.lastevents.logic.QueryEvent;

/**
 * Soubor s testy pro API last FM
 * @author Ondrej Benes <xbenes00@stud.fit.vutbr.cz>
 *
 */
public class LastFmApiTest {

	private Parser lastApi;

	public LastFmApiTest() {
		lastApi = new Parser("c8e71fc5e7255264940483b4228c010f", "lastevents");
	}

	@Test
	public void testArtist(){

		QueryEvent query = new QueryEvent();
		String url = new String();

		url = lastApi.getEventsByArtist("Wohnout", 5);
		String err = Parser.parseEvents(0, 5, url, query, QueryEvent.Types.SEARCH_BY_ARTIST);
		String result = query.getJSONResult();

		query = new QueryEvent();
		url = lastApi.getEventsByArtist("Wohnout", 5);
		Parser.parseEvents(0, 5, url, query, QueryEvent.Types.SEARCH_BY_ARTIST);
		String result2 = query.getJSONResult();

		query = new QueryEvent();
		url = lastApi.getEventsByArtist("Wohnout", 3);
		Parser.parseEvents(0, 5, url, query, QueryEvent.Types.SEARCH_BY_ARTIST);
		String result3 = query.getJSONResult();

		assertNull(err);
		assertTrue(result.length() > 2);
		assertEquals(result, result2);
		assertTrue(result != result3);
	}

	@Test
	public void testLocation(){
		QueryEvent query = new QueryEvent();
		String url = new String();

		url = lastApi.getEventsByLocation("Brno", 10, 5);
		String err = Parser.parseEvents(10, 5, url, query, QueryEvent.Types.SEARCH_BY_LOCATION);
		String result = query.getJSONResult();

		query = new QueryEvent();
		url = lastApi.getEventsByLocation("Brno", 10, 5);
		Parser.parseEvents(10, 5, url, query, QueryEvent.Types.SEARCH_BY_LOCATION);
		String result2 = query.getJSONResult();

		query = new QueryEvent();
		url = lastApi.getEventsByLocation("Brno", 10, 3);
		Parser.parseEvents(10, 5, url, query, QueryEvent.Types.SEARCH_BY_LOCATION);
		String result3 = query.getJSONResult();

		assertNull(err);
		assertTrue(result.length() > 2);
		assertEquals(result, result2);
		assertTrue(result != result3);
	}
}
