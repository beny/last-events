package cz.vutbr.fit.gja.lastevents.rest;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import cz.vutbr.fit.gja.lastevents.logic.Parser;
import cz.vutbr.fit.gja.lastevents.logic.QueryEvent;
import cz.vutbr.fit.gja.lastevents.logic.QueryArtist;
import cz.vutbr.fit.gja.lastevents.logic.QueryLocation;
import cz.vutbr.fit.gja.lastevents.logic.QueryTag;

/**
 * Class representing data source by a specific type of route.
 * @author Ondrej Benes <xbenes00@stud.fit.vutbr.cz>
 *
 */
public class Resource extends Restlet {

	/**
	 * enum with resource type
	 */
	public static enum Type {ARTIST, ARTIST_WITH_COUNT, LOCATION, LOCATION_WITH_DISTANCE, LOCATION_WITH_DISTANCE_AND_COUNT,
		SEARCH_ARTIST, SEARCH_ARTIST_WITH_COUNTS, SEARCH_LOCATION, SEARCH_LOCATION_WITH_COUNTS, SEARCH_TAG, SEARCH_TAG_WITH_COUNTS};


	/**
	 * Default distance
	 */
	public final static int DEFAULT_DISTANCE = 10;


	/**
	 * Default query result count
	 */
	public final static int DEFAULT_COUNT = 5;

	/**
	 * Default query result for autocomplete count
	 */
	public final static int DEFAULT_COUNT_SEARCH = 5;

	private Type type;
	private final static String LASTFM_KEY = "c8e71fc5e7255264940483b4228c010f";
	private final static String GEONAMES_USERNAME = "lastevents";

	/**
	 * Default constructor
	 * @param type
	 */
	public Resource(Type type) {
		this.type = type;
	}

	@Override
	public void handle(Request request, Response response) {

		Parser lastApi = new Parser(LASTFM_KEY, GEONAMES_USERNAME);
		String message = new String();
		String url = new String();
		String keyword = (String) request.getAttributes().get("query");
		int count = DEFAULT_COUNT;
		int distance = DEFAULT_DISTANCE;
		QueryEvent.Types typeQuery = QueryEvent.Types.SEARCH_BY_ARTIST;

		switch (type) {
		case ARTIST:
			url = lastApi.getEventsByArtist(
					keyword,
					DEFAULT_COUNT);
			typeQuery = QueryEvent.Types.SEARCH_BY_ARTIST;
			break;
		case ARTIST_WITH_COUNT:
			url = lastApi.getEventsByArtist(
					keyword,
					new Integer(request.getAttributes().get("count").toString()));
			typeQuery = QueryEvent.Types.SEARCH_BY_ARTIST;
			count = Integer.parseInt((String) request.getAttributes().get("count"));
			break;
		case LOCATION:
			url = lastApi.getEventsByLocation(
					keyword,
					DEFAULT_DISTANCE,
					DEFAULT_COUNT);
			typeQuery = QueryEvent.Types.SEARCH_BY_LOCATION;
			break;
		case LOCATION_WITH_DISTANCE:
			url = lastApi.getEventsByLocation(
					keyword,
					new Integer(request.getAttributes().get("distance").toString()),
					DEFAULT_COUNT);
			typeQuery = QueryEvent.Types.SEARCH_BY_LOCATION;
			distance = Integer.parseInt((String) request.getAttributes().get("distance"));
			break;
		case LOCATION_WITH_DISTANCE_AND_COUNT:
			url = lastApi.getEventsByLocation(
					keyword,
					new Integer(request.getAttributes().get("distance").toString()),
					new Integer(request.getAttributes().get("count").toString()));
			typeQuery = QueryEvent.Types.SEARCH_BY_LOCATION;
			distance = Integer.parseInt((String) request.getAttributes().get("distance"));
			count = Integer.parseInt((String) request.getAttributes().get("count"));
			break;
		case SEARCH_ARTIST:
			url = lastApi.getArtists(keyword, DEFAULT_COUNT_SEARCH);
			break;
		case SEARCH_ARTIST_WITH_COUNTS:
			url = lastApi.getArtists(keyword,
					new Integer(request.getAttributes().get("count").toString()));
			break;
		case SEARCH_LOCATION:
			url = lastApi.getLocations(keyword, DEFAULT_COUNT_SEARCH);
			break;
		case SEARCH_LOCATION_WITH_COUNTS:
			url = lastApi.getLocations(keyword,
					new Integer(request.getAttributes().get("count").toString()));
			break;
		case SEARCH_TAG:
			url = lastApi.getTags(keyword, DEFAULT_COUNT_SEARCH);
			break;
		case SEARCH_TAG_WITH_COUNTS:
			url = lastApi.getTags(keyword,
					new Integer(request.getAttributes().get("count").toString()));
			break;
		default:
			break;
		}


		// vyber z jakych objektu mas odpovidat
		String res = new String();
		if(type == Type.SEARCH_ARTIST || type == Type.SEARCH_ARTIST_WITH_COUNTS){
			QueryArtist queryArtist = new QueryArtist();
			res = Parser.loadArtists(url, queryArtist);
			if(res == null) {
				message = queryArtist.getJSONResult();
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		}
		else if(type == Type.SEARCH_LOCATION || type == Type.SEARCH_LOCATION_WITH_COUNTS){
			QueryLocation queryLocation = new QueryLocation();
			res = Parser.loadLocations(keyword, url, queryLocation);
			if(res == null) {
				message = queryLocation.getJSONResult();
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		}
		else if(type == Type.SEARCH_TAG || type == Type.SEARCH_TAG_WITH_COUNTS){
			QueryTag queryTag = new QueryTag();
			res = Parser.loadTags(url, queryTag);
			if(res == null) {
				message = queryTag.getJSONResult();
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		}
		else {
			QueryEvent query = new QueryEvent();
			res = Parser.loadEvents(keyword, distance, count, url, query, typeQuery);
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
