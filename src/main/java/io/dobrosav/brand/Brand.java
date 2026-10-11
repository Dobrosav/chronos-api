package io.dobrosav.brand;

import io.dobrosav.model.WatchModel;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "brands")
public class Brand extends PanacheEntity {
    @Column(nullable = false, unique = true)
    public String name;

    public String countryOfOrigin;

    public int foundationYear;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<WatchModel> models;

    public Brand() {
    }

    public Brand(String name, String countryOfOrigin, int foundationYear) {
        this.name = name;
        this.countryOfOrigin = countryOfOrigin;
        this.foundationYear = foundationYear;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                ", countryOfOrigin='" + countryOfOrigin + '\'' +
                ", foundationYear=" + foundationYear +
                '}';
    }
}
