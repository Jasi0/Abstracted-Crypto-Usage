package web.resteasy.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class PingResource {
    private static final Logger log = LoggerFactory.getLogger(PingResource.class);

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        log.info("Ping endpoint called");
        return "pong";
    }

    @GET
    @Path("/echo")
    @Produces(MediaType.TEXT_PLAIN)
    public Response echo(@QueryParam("msg") String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            log.warn("Echo called with blank msg");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("msg must not be blank")
                    .build();
        }
        log.debug("Echo returning '{}'", msg);
        return Response.ok(msg).build();
    }
}