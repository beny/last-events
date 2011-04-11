package cz.vutbr.fit.gja.lastevents.rest;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import cz.vutbr.fit.gja.lastevents.logic.Parser;
import cz.vutbr.fit.gja.lastevents.logic.Query;

/**
 * Trida reprezentujici zdroj dat podle typu aplikovane routy
 * @author Ondrej Benes <xbenes00@stud.fit.vutbr.cz>
 *
 */
public class Resource extends Restlet {

	public static enum Type {ARTIST, ARTIST_WITH_COUNT, LOCATION, LOCATION_WITH_DISTANCE, LOCATION_WITH_DISTANCE_AND_COUNT};

	private Type type;
	private final static String KEY = "c8e71fc5e7255264940483b4228c010f";
	private final static int DEFAULT_DISTANCE = 10;
	private final static int DEFAULT_COUNT = 5;

	public Resource(Type type) {
		this.type = type;
	}

	@Override
	public void handle(Request request, Response response) {

		Parser lastApi = new Parser(KEY);
		String message = new String();
		String url = new String();

		switch (type) {
		case ARTIST:
			url = lastApi.getEventsByArtist(
					(String)request.getAttributes().get("query"),
					DEFAULT_COUNT);
			break;
		case ARTIST_WITH_COUNT:
			url = lastApi.getEventsByArtist(
					(String)request.getAttributes().get("query"),
					new Integer(request.getAttributes().get("count").toString()));
			break;
		case LOCATION:
			url = lastApi.getEventsByLocation(
					(String) request.getAttributes().get("query"),
					DEFAULT_DISTANCE,
					DEFAULT_COUNT);
			break;
		case LOCATION_WITH_DISTANCE:
			url = lastApi.getEventsByLocation(
					(String)request.getAttributes().get("query"),
					new Integer(request.getAttributes().get("distance").toString()),
					DEFAULT_COUNT);
			break;
		case LOCATION_WITH_DISTANCE_AND_COUNT:
			url = lastApi.getEventsByLocation(
					(String)request.getAttributes().get("query"),
					new Integer(request.getAttributes().get("distance").toString()),
					new Integer(request.getAttributes().get("count").toString()));
			break;
		default:
			break;
		}

		Query query = new Query();

		String res = Parser.parseEvents(url, query, Query.Types.SEARCH_BY_ARTIST);
		if(res == "") message = query.getJSONQuery();

		response.setEntity(message, MediaType.TEXT_PLAIN);
	}
}
