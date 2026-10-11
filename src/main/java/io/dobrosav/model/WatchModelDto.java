package io.dobrosav.model;

public record WatchModelDto(
        Long id,
        String referenceNumber,
        String movementType,
        String braceletMaterial,
        Long brandId
) {
}