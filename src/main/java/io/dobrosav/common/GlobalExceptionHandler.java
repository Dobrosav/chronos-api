package io.dobrosav.common;

import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class GlobalExceptionHandler {


    @RegisterForReflection
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
    public Response handleClientError(ClientErrorException ex) {
        Log.warn(ex.getMessage());
        int status = ex.getResponse().getStatus();
        Response.Status known = Response.Status.fromStatusCode(status);
        return Response.status(status)
                .entity(new ErrorMessage(known != null ? known.name() : "CLIENT_ERROR", ex.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleAllExceptions(Throwable ex) {
        Log.error("Unexpected error occurred", ex);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorMessage("Occurred unexpected error", ex.getMessage()))
                .build();
    }
}