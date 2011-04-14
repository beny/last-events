package cz.vutbr.fit.gja.lastevents.rest;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import cz.vutbr.fit.gja.lastevents.logic.Parser;
import cz.vutbr.fit.gja.lastevents.logic.Query;
import cz.vutbr.fit.gja.lastevents.logic.QueryArtist;

/**
 * Trida reprezentujici zdroj dat podle typu aplikovane routy
 * @author Ondrej Benes <xbenes00@stud.fit.vutbr.cz>
 *
 */
public class Resource extends Restlet {

	public static enum Type {ARTIST, ARTIST_WITH_COUNT, LOCATION, LOCATION_WITH_DISTANCE, LOCATION_WITH_DISTANCE_AND_COUNT,
		NAMES, NAMES_WITH_COUNTS};
	public final static int DEFAULT_DISTANCE = 10;
	public final static int DEFAULT_COUNT = 5;

	private Type type;
	private final static String KEY = "c8e71fc5e7255264940483b4228c010f";

	public Resource(Type type) {
		this.type = type;
	}

	@Override
	public void handle(Request request, Response response) {

		Parser lastApi = new Parser(KEY);
		String message = new String();
		String url = new String();
		String keyword = (String) request.getAttributes().get("query");
		Query.Types typeQuery = Query.Types.SEARCH_BY_ARTIST;

		switch (type) {
		case ARTIST:
			url = lastApi.getEventsByArtist(
					keyword,
					DEFAULT_COUNT);
			typeQuery = Query.Types.SEARCH_BY_ARTIST;
			break;
		case ARTIST_WITH_COUNT:
			url = lastApi.getEventsByArtist(
					keyword,
					new Integer(request.getAttributes().get("count").toString()));
			typeQuery = Query.Types.SEARCH_BY_ARTIST;
			break;
		case LOCATION:
			url = lastApi.getEventsByLocation(
					keyword,
					DEFAULT_DISTANCE,
					DEFAULT_COUNT);
			typeQuery = Query.Types.SEARCH_BY_LOCATION;
			break;
		case LOCATION_WITH_DISTANCE:
			url = lastApi.getEventsByLocation(
					keyword,
					new Integer(request.getAttributes().get("distance").toString()),
					DEFAULT_COUNT);
			typeQuery = Query.Types.SEARCH_BY_LOCATION;
			break;
		case LOCATION_WITH_DISTANCE_AND_COUNT:
			url = lastApi.getEventsByLocation(
					keyword,
					new Integer(request.getAttributes().get("distance").toString()),
					new Integer(request.getAttributes().get("count").toString()));
			typeQuery = Query.Types.SEARCH_BY_LOCATION;
			break;
		case NAMES:
			url = lastApi.getArtists(keyword, DEFAULT_COUNT);
			break;
		case NAMES_WITH_COUNTS:
			url = lastApi.getArtists(keyword,
					new Integer(request.getAttributes().get("count").toString()));
			break;
		default:
			break;
		}

		// vyber z jakych objektu mas odpovidat
		String res = new String();
		if(type == Type.NAMES || type == Type.NAMES_WITH_COUNTS){
			QueryArtist queryArtist = new QueryArtist();
			res = Parser.loadArtists(url, queryArtist);
			if(res == null) {
				message = queryArtist.getJSONResult();
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		}
		else {
			Query query = new Query();
			res = Parser.loadEvents(keyword, url, query, typeQuery);
			if(res == null) {
				message = query.getJSONResult();
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		}

		// nastala chyba
		if(res != null){
			response.setEntity("Error during requesting data: " + res, MediaType.TEXT_PLAIN);
		}

	}
}
