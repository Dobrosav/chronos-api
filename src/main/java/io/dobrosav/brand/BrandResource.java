package io.dobrosav.brand;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/brands")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BrandResource {

    private final BrandService brandService;

    public BrandResource(BrandService brandService) {
        this.brandService = brandService;
    }

    @GET
    public List<BrandDto> getBrands(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return brandService.getBrands(page, size);
    }

    @GET
    @Path("/{id}")
    public BrandDto getBrand(@PathParam("id") long id) {
        return brandService.getBrandById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public BrandDto createBrand(BrandDto brandDto) {
        return brandService.createBrand(brandDto);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public BrandDto updateBrand(@PathParam("id") long id, BrandDto brandDto) {
        return brandService.updateBrand(id, brandDto);
    }


    @DELETE
    @Path("/{id}")
    public Response deleteBrand(@PathParam("id") long id) {
        boolean deleted = brandService.deleteBrand(id);
        if (!deleted) {
            throw new NotFoundException("Brand with id " + id + " not found");
        }
        return Response.noContent().build();
    }
}
