package io.dobrosav.brand;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BrandService {


    public List<BrandDto> getBrands() {
        Log.info("Fetching all brands from database");
        List<Brand> brands = Brand.listAll();
        Log.infof("Found %d brands", brands.size());
        return brands.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public BrandDto getBrandById(Long id) {
        Log.infof("Fetching brand with id: %d", id);
        Brand brand = Brand.findById(id);
        if (brand == null) {
            Log.warnf("Brand with id %d not found", id);
            throw new NotFoundException("Brand with id " + id + " not found");
        }
        return mapToDto(brand);
    }

    @Transactional
    public BrandDto createBrand(BrandDto brandDto) {
        Log.infof("Attempting to create a new brand: %s", brandDto.name());
        if (Brand.count("name", brandDto.name()) > 0) {
            Log.warnf("Brand creation failed: Name '%s' already exists", brandDto.name());
            throw new ClientErrorException("Brand with name " + brandDto.name() + " already exists", Response.Status.CONFLICT);
        }
        Brand brand = new Brand(brandDto.name(), brandDto.countryOfOrigin(), brandDto.foundationYear());
        Brand.persist(brand);
        Log.infof("Successfully created brand '%s' with id: %d", brand.name, brand.id);
        return mapToDto(brand);
    }

    @Transactional
    public BrandDto updateBrand(Long id, BrandDto brandDto) {
        Log.infof("Attempting to update brand with id: %d", id);
        Brand brand = Brand.findById(id);
        if (brand == null) {
            Log.warnf("Update failed: Brand with id %d not found", id);
            throw new NotFoundException("Brand with id " + id + " not found");
        }
        if (Brand.count("name = ?1 and id <> ?2", brandDto.name(), id) > 0) {
            Log.warnf("Update failed: Another brand with name '%s' already exists", brandDto.name());
            throw new ClientErrorException("Brand with name " + brandDto.name() + " already exists", Response.Status.CONFLICT);
        }
        
        Log.infof("Updating brand %d values. Old name: '%s', New name: '%s'", id, brand.name, brandDto.name());
        brand.name = brandDto.name();
        brand.countryOfOrigin = brandDto.countryOfOrigin();
        brand.foundationYear = brandDto.foundationYear();
        
        return mapToDto(brand);
    }

    @Transactional
    public boolean deleteBrand(Long id) {
        Log.infof("Attempting to delete brand with id: %d", id);
        boolean deleted = Brand.deleteById(id);
        if (deleted) {
            Log.infof("Successfully deleted brand with id: %d", id);
        } else {
            Log.warnf("Failed to delete: Brand with id %d not found", id);
        }
        return deleted;
    }

    private BrandDto mapToDto(Brand brand) {
        return new BrandDto(
                brand.id,
                brand.name,
                brand.countryOfOrigin,
                brand.foundationYear
        );
    }

}