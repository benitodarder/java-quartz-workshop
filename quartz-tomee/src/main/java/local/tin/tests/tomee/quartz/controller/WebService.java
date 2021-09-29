package local.tin.tests.tomee.quartz.controller;

import java.util.Date;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author benitodarder
 */

@Path("/quartz")
public class WebService {
    
    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response helloBeing()  {

          return  Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("Hi being... It's " + new Date()).build();
    }    

}
    

