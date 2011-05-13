package cz.vutbr.fit.gja.lastevents.rest;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cz.vutbr.fit.gja.lastevents.rest.Resource.Type;

/**
 * Handle URL routes.
 * @author Ondrej Benes <xbenes00@stud.fit.vutbr.cz>
 *
 */
public class API extends Application {


	/**
	 * Default constructor
	 * @param parentContext
	 */
	public API(Context parentContext) {
		super(parentContext);
	}

	/**
	 * Create REST.
	 */
	@Override
	public synchronized Restlet createRoot() {
		Router router = new Router(getContext());

		// nastaveni jednotlivych rout
		router.attach("/search/artist/{query}", new Resource(Type.SEARCH_ARTIST));
		router.attach("/search/artist/{query}/{count}", new Resource(Type.SEARCH_ARTIST_WITH_COUNTS));
		router.attach("/search/location/{query}", new Resource(Type.SEARCH_LOCATION));
		router.attach("/search/location/{query}/{count}", new Resource(Type.SEARCH_LOCATION_WITH_COUNTS));
		router.attach("/search/tag/{query}", new Resource(Type.SEARCH_TAG));
		router.attach("/search/tag/{query}/{count}", new Resource(Type.SEARCH_TAG_WITH_COUNTS));
		router.attach("/artist/{query}", new Resource(Type.ARTIST));
		router.attach("/artist/{query}/{count}", new Resource(Type.ARTIST_WITH_COUNT));
		router.attach("/location/{query}", new Resource(Type.LOCATION));
		router.attach("/location/{query}/{distance}", new Resource(Type.LOCATION_WITH_DISTANCE));
		router.attach("/location/{query}/{distance}/{count}", new Resource(Type.LOCATION_WITH_DISTANCE_AND_COUNT));

		return router;
	}
}
