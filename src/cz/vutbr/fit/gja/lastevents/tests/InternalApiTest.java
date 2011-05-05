package cz.vutbr.fit.gja.lastevents.tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.junit.Test;

import cz.vutbr.fit.gja.lastevents.rest.Resource;
import cz.vutbr.fit.gja.lastevents.rest.Resource.Type;

/**
 * Trida testujici interni rozhrani
 * @author Ondrej Benes <xbenes00@stud.fit.vutbr.cz>
 *
 */
public class InternalApiTest {

	@Test
	public void testArtistCount(){

		assertEquals(getData(Type.ARTIST, "Wohnout", Resource.DEFAULT_COUNT).size(), Resource.DEFAULT_COUNT);
		assertEquals(getData(Type.ARTIST, "Wohnout", Resource.DEFAULT_COUNT),
				getData(Type.ARTIST, "Wohnout", Resource.DEFAULT_COUNT));
	}

	@Test
	public void testLocationCount(){

		assertEquals(getData(Type.LOCATION, "Brno", Resource.DEFAULT_COUNT).size(), Resource.DEFAULT_COUNT);
		assertEquals(getData(Type.LOCATION, "Brno", Resource.DEFAULT_COUNT),
				getData(Type.LOCATION, "Brno", Resource.DEFAULT_COUNT));
	}

	@Test
	public void testSearchArtist(){
		assertEquals(getData(Type.SEARCH_ARTIST, "Madonna", Resource.DEFAULT_COUNT).size(), Resource.DEFAULT_COUNT);
		assertEquals(getData(Type.SEARCH_ARTIST, "Madonna", Resource.DEFAULT_COUNT),
				getData(Type.SEARCH_ARTIST, "Madonna", Resource.DEFAULT_COUNT));
	}

	@Test
	public void testSearchLocation(){
		assertEquals(getData(Type.SEARCH_LOCATION, "Brno", Resource.DEFAULT_COUNT).size(), Resource.DEFAULT_COUNT);
		assertEquals(getData(Type.SEARCH_LOCATION, "Brno", Resource.DEFAULT_COUNT),
				getData(Type.SEARCH_LOCATION, "Brno", Resource.DEFAULT_COUNT));
	}

	/**
	 * Ziskani data dle typu
	 * @param type
	 * @param query
	 * @param limit
	 * @return
	 */
	private static JSONArray getData(Resource.Type type, String query, int limit){
		JSONArray obj = new JSONArray();
		String jsonData = new String();
		String typeString = new String();

		if(type == Type.ARTIST) typeString = "artist";
		if(type == Type.LOCATION) typeString = "location";
		if(type == Type.SEARCH_ARTIST) typeString = "search/artist";
		if(type == Type.SEARCH_LOCATION) typeString = "search/location";

		if(!typeString.isEmpty()){
			try {
				URL url = new URL("http://localhost:8888/api/" + typeString + "/" + query + "/" + limit);
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String line;

				while ((line = reader.readLine()) != null) {
					jsonData += line;
				}
				reader.close();

				obj = (JSONArray) JSONValue.parse(jsonData);

			} catch (MalformedURLException e) {
				Assert.fail("MalformedURL " + e.getMessage());
			} catch (IOException e) {
				Assert.fail("Asi neni pusten server na localhostu " + e.getMessage());
			}
		}
		else {
			Assert.fail("Spatne nastaveny typ udalosti ktere chceme ziskat");
		}

        return obj;


	}
}
