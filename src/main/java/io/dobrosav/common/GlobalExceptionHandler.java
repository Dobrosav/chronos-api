package io.dobrosav.common;

import io.quarkus.logging.Log;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class GlobalExceptionHandler {


    public record ErrorMessage(String errorMessage, String errorDescription) {
    }

    @ServerExceptionMapper
    public Response handleNotFound(NotFoundException ex) {
        Log.warn(ex.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage(Response.Status.NOT_FOUND.name(), ex.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleAllExceptions(Throwable ex) {
        Log.errorf("Occurred unexpected error", ex);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorMessage("Occurred unexpected error", ex.getMessage()))
                .build();
    }
}