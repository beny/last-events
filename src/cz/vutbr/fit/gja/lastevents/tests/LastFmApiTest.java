package cz.vutbr.fit.gja.lastevents.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import cz.vutbr.fit.gja.lastevents.logic.Parser;
import cz.vutbr.fit.gja.lastevents.logic.Query;

public class LastFmApiTest {

	private Parser lastApi;

	public LastFmApiTest() {
		lastApi = new Parser("c8e71fc5e7255264940483b4228c010f");
	}

	@Test
	public void testArtist(){

		Query query = new Query();
		String url = new String();

		url = lastApi.getEventsByArtist("Wohnout", 5);
		String err = Parser.parseEvents(url, query, Query.Types.SEARCH_BY_ARTIST);
		String result = query.getJSONResult();

		query = new Query();
		url = lastApi.getEventsByArtist("Wohnout", 5);
		Parser.parseEvents(url, query, Query.Types.SEARCH_BY_ARTIST);
		String result2 = query.getJSONResult();

		query = new Query();
		url = lastApi.getEventsByArtist("Wohnout", 3);
		Parser.parseEvents(url, query, Query.Types.SEARCH_BY_ARTIST);
		String result3 = query.getJSONResult();

		assertNull(err);
		assertTrue(result.length() > 2);
		assertEquals(result, result2);
		assertTrue(result != result3);
	}

	@Test
	public void testLocation(){
		Query query = new Query();
		String url = new String();

		url = lastApi.getEventsByLocation("Brno", 10, 5);
		String err = Parser.parseEvents(url, query, Query.Types.SEARCH_BY_LOCATION);
		String result = query.getJSONResult();

		query = new Query();
		url = lastApi.getEventsByLocation("Brno", 10, 5);
		Parser.parseEvents(url, query, Query.Types.SEARCH_BY_LOCATION);
		String result2 = query.getJSONResult();

		query = new Query();
		url = lastApi.getEventsByLocation("Brno", 10, 3);
		Parser.parseEvents(url, query, Query.Types.SEARCH_BY_LOCATION);
		String result3 = query.getJSONResult();

		assertNull(err);
		assertTrue(result.length() > 2);
		assertEquals(result, result2);
		assertTrue(result != result3);
	}
}