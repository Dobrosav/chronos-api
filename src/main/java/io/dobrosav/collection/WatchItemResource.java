package io.dobrosav.collection;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/watch-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WatchItemResource {

    private final WatchItemService watchItemService;

    public WatchItemResource(WatchItemService watchItemService) {
        this.watchItemService = watchItemService;
    }

    @GET
    public Response getAllWatchItems() {
        List<WatchItemDto> items = watchItemService.getAllWatchItems();
        return Response.ok(items).build();
    }

    @GET
    @Path("/{id}")
    public Response getWatchItemById(@PathParam("id") Long id) {
        WatchItemDto item = watchItemService.getWatchItemById(id);
        return Response.ok(item).build();
    }

    @POST
    public Response createWatchItem(WatchItemDto dto) {
        WatchItemDto createdItem = watchItemService.createWatchItem(dto);
        return Response.status(Response.Status.CREATED).entity(createdItem).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteWatchItem(@PathParam("id") Long id) {
        boolean deleted = watchItemService.deleteWatchItem(id);
        if (!deleted) {
            throw new NotFoundException("Watch item with ID " + id + " not found.");
        }
        return Response.noContent().build();
    }
}