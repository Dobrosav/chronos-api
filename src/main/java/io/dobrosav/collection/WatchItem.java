package io.dobrosav.collection;

import io.dobrosav.model.WatchModel;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "watch_items")
public class WatchItem extends PanacheEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watch_model_id", nullable = false)
    public WatchModel watchModel;

    public LocalDate purchaseDate;
    public String serialNumber;
    public String condition;

    @Override
    public String toString() {
        return "WatchItem{" +
                "watchModel=" + watchModel +
                ", purchaseDate=" + purchaseDate +
                ", serialNumber='" + serialNumber + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}
