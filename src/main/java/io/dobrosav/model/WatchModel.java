package io.dobrosav.model;

import io.dobrosav.brand.Brand;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "watch_models")
public class WatchModel extends PanacheEntity {
    @Column(nullable = false, unique = true)
    public String referenceNumber;

    public String movementType;

    public String braceletMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    public Brand brand;

    @Override
    public String toString() {
        return "WatchModel{" +
                "referenceNumber='" + referenceNumber + '\'' +
                ", movementType='" + movementType + '\'' +
                ", braceletMaterial='" + braceletMaterial + '\'' +
                ", brand=" + brand +
                '}';
    }
}
