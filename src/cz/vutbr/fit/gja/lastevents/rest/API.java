package cz.vutbr.fit.gja.lastevents.rest;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
  
public class API extends Application {  
  
   public API(Context parentContext) {  
      super(parentContext);  
   }  
  
   /** 
    * Creates a root Restlet that will receive all incoming calls. 
    */  
   @Override  
   public synchronized Restlet createRoot() {  
      Router router = new Router(getContext());
      router.attachDefault(EventsResource.class);  
  
      return router;  
   }  
} 
