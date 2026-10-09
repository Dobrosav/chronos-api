package io.dobrosav.brand;

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
        List<Brand> brands = Brand.listAll();
        return brands.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public BrandDto getBrandById(Long id) {
        Brand brand = Brand.findById(id);
        if (brand == null) {
            throw new NotFoundException("Brand with id " + id + " not found");
        }
        return mapToDto(brand);
    }

    @Transactional
    public BrandDto createBrand(BrandDto brandDto) {
        if (Brand.count("name", brandDto.name()) > 0) {
            throw new ClientErrorException("Brand with name " + brandDto.name() + " already exists", Response.Status.CONFLICT);
        }
        Brand brand = new Brand(brandDto.name(), brandDto.countryOfOrigin(), brandDto.foundationYear());
        Brand.persist(brand);
        return mapToDto(brand);
    }

    @Transactional
    public BrandDto updateBrand(Long id, BrandDto brandDto) {
        Brand brand = Brand.findById(id);
        if (brand == null) {
            throw new NotFoundException("Brand with id " + id + " not found");
        }
        if (Brand.count("name = ?1 and id <> ?2", brandDto.name(), id) > 0) {
            throw new ClientErrorException("Brand with name " + brandDto.name() + " already exists", Response.Status.CONFLICT);
        }
        brand.name = brandDto.name();
        brand.countryOfOrigin = brandDto.countryOfOrigin();
        brand.foundationYear = brandDto.foundationYear();
        return mapToDto(brand);
    }

    @Transactional
    public boolean deleteBrand(Long id) {
        return Brand.deleteById(id);
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