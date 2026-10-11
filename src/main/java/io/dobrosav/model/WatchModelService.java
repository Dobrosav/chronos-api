package io.dobrosav.model;

import io.dobrosav.brand.Brand;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class WatchModelService {

    public List<WatchModelDto> getAllWatchModels() {
        Log.info("Fetching all watch models from database");
        List<WatchModel> models = WatchModel.listAll();
        Log.infof("Found %d watch models", models.size());
        return models.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public WatchModelDto getWatchModelById(Long id) {
        Log.infof("Fetching watch model with id: %d", id);
        WatchModel model = WatchModel.findById(id);
        if (model == null) {
            Log.warnf("Watch model with id %d not found", id);
            throw new NotFoundException("Watch model with ID " + id + " not found.");
        }
        return mapToDto(model);
    }

    @Transactional
    public WatchModelDto createWatchModel(WatchModelDto dto) {
        Log.infof("Attempting to create a new watch model with reference number: %s", dto.referenceNumber());
        if (WatchModel.count("referenceNumber", dto.referenceNumber()) > 0) {
            Log.warnf("Watch model creation failed: Reference number '%s' already exists", dto.referenceNumber());
            throw new ClientErrorException("Watch model with reference number " + dto.referenceNumber() + " already exists", Response.Status.CONFLICT);
        }
        Brand brand = Brand.findById(dto.brandId());
        if (brand == null) {
            Log.warnf("Watch model creation failed: Brand with id %d not found", dto.brandId());
            throw new NotFoundException("Brand with ID " + dto.brandId() + " not found.");
        }

        WatchModel model = new WatchModel();
        model.referenceNumber = dto.referenceNumber();
        model.movementType = dto.movementType();
        model.braceletMaterial = dto.braceletMaterial();
        model.brand = brand;

        model.persist();
        Log.infof("Successfully created watch model '%s' with id: %d", model.referenceNumber, model.id);

        return mapToDto(model);
    }

    @Transactional
    public boolean deleteWatchModel(Long id) {
        Log.infof("Attempting to delete watch model with id: %d", id);
        boolean deleted = WatchModel.deleteById(id);
        if (deleted) {
            Log.infof("Successfully deleted watch model with id: %d", id);
        } else {
            Log.warnf("Failed to delete: Watch model with id %d not found", id);
        }
        return deleted;
    }

    private WatchModelDto mapToDto(WatchModel model) {
        return new WatchModelDto(
                model.id,
                model.referenceNumber,
                model.movementType,
                model.braceletMaterial,
                model.brand.id
        );
    }
}