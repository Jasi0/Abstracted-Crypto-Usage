package web.resteasy;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import web.resteasy.resource.MathResource;
import web.resteasy.resource.PingResource;

@ApplicationPath("/")
public class RestEasyApp extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(PingResource.class);
        classes.add(MathResource.class);
        return classes;
    }
}