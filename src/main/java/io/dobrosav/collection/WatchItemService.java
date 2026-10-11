package io.dobrosav.collection;

import io.dobrosav.model.WatchModel;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class WatchItemService {

    public List<WatchItemDto> getAllWatchItems() {
        Log.info("Fetching all watch items from database");
        List<WatchItem> items = WatchItem.listAll();
        Log.infof("Found %d watch items", items.size());
        return items.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public WatchItemDto getWatchItemById(Long id) {
        Log.infof("Fetching watch item with id: %d", id);
        WatchItem item = WatchItem.findById(id);
        if (item == null) {
            Log.warnf("Watch item with id %d not found", id);
            throw new NotFoundException("Watch item with ID " + id + " not found.");
        }
        return mapToDto(item);
    }

    @Transactional
    public WatchItemDto createWatchItem(WatchItemDto dto) {
        Log.infof("Attempting to create a new watch item for model id: %d", dto.watchModelId());
        WatchModel model = WatchModel.findById(dto.watchModelId());
        if (model == null) {
            Log.warnf("Watch item creation failed: Watch model with id %d not found", dto.watchModelId());
            throw new NotFoundException("Watch model with ID " + dto.watchModelId() + " not found.");
        }

        WatchItem item = new WatchItem();
        item.watchModel = model;
        item.purchaseDate = dto.purchaseDate();
        item.serialNumber = dto.serialNumber();
        item.condition = dto.condition();

        item.persist();
        Log.infof("Successfully created watch item with id: %d", item.id);

        return mapToDto(item);
    }

    @Transactional
    public boolean deleteWatchItem(Long id) {
        Log.infof("Attempting to delete watch item with id: %d", id);
        boolean deleted = WatchItem.deleteById(id);
        if (deleted) {
            Log.infof("Successfully deleted watch item with id: %d", id);
        } else {
            Log.warnf("Failed to delete: Watch item with id %d not found", id);
        }
        return deleted;
    }

    private WatchItemDto mapToDto(WatchItem item) {
        return new WatchItemDto(
                item.id,
                item.watchModel.id,
                item.purchaseDate,
                item.serialNumber,
                item.condition
        );
    }
}