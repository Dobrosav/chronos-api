package io.dobrosav.brand;

public record BrandDto(
        Long id,
        String name,
        String countryOfOrigin,
        int foundationYear
) {
}