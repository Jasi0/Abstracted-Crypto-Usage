package web.resteasy.resource;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/api/math")
public class MathResource {
    private static final Logger log = LoggerFactory.getLogger(MathResource.class);

    @GET
    @Path("/sum")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Number> sum(@QueryParam("a") double a, @QueryParam("b") double b) {
        double s = a + b;
        log.info("SUM a={} b={} -> {}", a, b, s);
        Map<String, Number> res = new HashMap<>();
        res.put("a", a);
        res.put("b", b);
        res.put("sum", s);
        return res;
    }

    @GET
    @Path("/avg")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Number> avg(@QueryParam("a") double a, @QueryParam("b") double b) {
        double s = a + b;
        double avg = s / 2.0;
        log.info("AVG a={} b={} -> {}", a, b, avg);
        Map<String, Number> res = new HashMap<>();
        res.put("a", a);
        res.put("b", b);
        res.put("avg", avg);
        return res;
    }
}