package io.dobrosav.collection;

import java.time.LocalDate;

public record WatchItemDto(
        Long id,
        Long watchModelId,
        LocalDate purchaseDate,
        String serialNumber,
        String condition
) {
}