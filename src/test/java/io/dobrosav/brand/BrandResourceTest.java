package io.dobrosav.brand;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrandResourceTest {

    @Test
    @Order(1)
    public void testCreateBrand() {
        // Pošto je BrandDto sada record, kreiramo ga pomoću konstruktora
        BrandDto newBrand = new BrandDto(null, "Seiko", "Japan", 1881);

        given()
                .contentType(ContentType.JSON)
                .body(newBrand)
                .when()
                .post("/brands")
                .then()
                .statusCode(200) // Ukoliko tvoj resurs vraća 200 OK. Ako si stavio Response.status(201) stavi 201
                .body("name", is("Seiko"))
                .body("countryOfOrigin", is("Japan"))
                .body("foundationYear", is(1881))
                .body("id", notNullValue());
    }

    @Test
    @Order(2)
    public void testGetAllBrands() {
        given()
                .when().get("/brands")
                .then()
                .statusCode(200)
                .body("size()", is(1)); // Sada već imamo 1 kreiran brend iz prvog testa
    }

    @Test
    @Order(3)
    public void testGetSingleBrand() {
        // ID prvog kreiranog elementa će verovatno biti 1
        given()
                .pathParam("id", 1)
                .when()
                .get("/brands/{id}")
                .then()
                .statusCode(200)
                .body("name", is("Seiko"));
    }

    @Test
    @Order(4)
    public void testCreateDuplicateBrandReturnsConflict() {
        BrandDto duplicate = new BrandDto(null, "Seiko", "Japan", 1881);

        given()
                .contentType(ContentType.JSON)
                .body(duplicate)
                .when()
                .post("/brands")
                .then()
                .statusCode(409);
    }
}
