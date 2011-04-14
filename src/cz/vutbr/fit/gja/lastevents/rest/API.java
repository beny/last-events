package cz.vutbr.fit.gja.lastevents.rest;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cz.vutbr.fit.gja.lastevents.rest.Resource.Type;

/**
 * Trida zajistujici nastaveni rout na spravny zdroj dat
 * @author Ondrej Benes <xbenes00@stud.fit.vutbr.cz>
 *
 */
public class API extends Application {

	public API(Context parentContext) {
		super(parentContext);
	}

	/**
	 * Vytvori RESTlet ktery nasloucha konkretnim routam
	 */
	@Override
	public synchronized Restlet createRoot() {
		Router router = new Router(getContext());

		// nastaveni jednotlivych rout
		router.attach("/names/{query}", new Resource(Type.NAMES));
		router.attach("/names/{query}/{count}", new Resource(Type.NAMES_WITH_COUNTS));
		router.attach("/artist/{query}", new Resource(Type.ARTIST));
		router.attach("/artist/{query}/{count}", new Resource(Type.ARTIST_WITH_COUNT));
		router.attach("/location/{query}", new Resource(Type.LOCATION));
		router.attach("/location/{query}/{distance}", new Resource(Type.LOCATION_WITH_DISTANCE));
		router.attach("/location/{query}/{distance}/{count}", new Resource(Type.LOCATION_WITH_DISTANCE_AND_COUNT));

		return router;
	}
}
