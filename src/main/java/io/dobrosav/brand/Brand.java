package io.dobrosav.brand;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "brands")
public class Brand extends PanacheEntity {
    @Column(nullable = false, unique = true)
    public String name;

    public String countryOfOrigin;

    public int foundationYear;

    public Brand() {}

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
