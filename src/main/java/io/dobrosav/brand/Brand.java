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

    public String countyOfOrigin;

    public int foundationYear;

    public Brand() {}

    public Brand(String name, String countyOfOrigin, int foundationYear) {
        this.name = name;
        this.countyOfOrigin = countyOfOrigin;
        this.foundationYear = foundationYear;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                ", countyOfOrigin='" + countyOfOrigin + '\'' +
                ", foundationYear=" + foundationYear +
                '}';
    }
}
