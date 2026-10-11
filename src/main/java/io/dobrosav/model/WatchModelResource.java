package io.dobrosav.model;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/watch-models")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WatchModelResource {

    private final WatchModelService watchModelService;

    public WatchModelResource(WatchModelService watchModelService) {
        this.watchModelService = watchModelService;
    }


    @GET
    public Response getAllWatchModels() {
        List<WatchModelDto> models = watchModelService.getAllWatchModels();
        return Response.ok(models).build();
    }

    @GET
    @Path("/{id}")
    public Response getWatchModelById(@PathParam("id") Long id) {
        WatchModelDto model = watchModelService.getWatchModelById(id);
        return Response.ok(model).build();
    }

    @POST
    public Response createWatchModel(WatchModelDto dto) {
        WatchModelDto createdModel = watchModelService.createWatchModel(dto);
        return Response.status(Response.Status.CREATED).entity(createdModel).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteWatchModel(@PathParam("id") Long id) {
        boolean deleted = watchModelService.deleteWatchModel(id);
        if (!deleted) {
            throw new NotFoundException("Watch model with ID " + id + " not found.");
        }
        return Response.noContent().build();
    }
}