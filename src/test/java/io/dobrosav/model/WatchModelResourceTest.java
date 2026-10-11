package io.dobrosav.model;

import io.dobrosav.brand.BrandDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WatchModelResourceTest {

    private static Long brandId;
    private static Long modelId;

    @Test
    @Order(1)
    public void testCreateWatchModel() {
        BrandDto brand = new BrandDto(null, "Orient-WatchModelTest", "Japan", 1950);

        brandId = given()
                .contentType(ContentType.JSON)
                .body(brand)
                .when()
                .post("/brands")
                .then()
                .statusCode(200)
                .extract().jsonPath().getLong("id");

        WatchModelDto dto = new WatchModelDto(null, "REF-001", "Automatic", "Steel", brandId);

        modelId = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/watch-models")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("referenceNumber", is("REF-001"))
                .body("movementType", is("Automatic"))
                .body("braceletMaterial", is("Steel"))
                .body("brandId", is(brandId.intValue()))
                .extract().jsonPath().getLong("id");
    }

    @Test
    @Order(2)
    public void testCreateWatchModelWithUnknownBrandReturnsNotFound() {
        WatchModelDto dto = new WatchModelDto(null, "REF-002", "Quartz", "Leather", 999999L);

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/watch-models")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(3)
    public void testCreateDuplicateWatchModelReturnsConflict() {
        WatchModelDto duplicate = new WatchModelDto(null, "REF-001", "Automatic", "Steel", brandId);

        given()
                .contentType(ContentType.JSON)
                .body(duplicate)
                .when()
                .post("/watch-models")
                .then()
                .statusCode(409);
    }

    @Test
    @Order(4)
    public void testGetAllWatchModels() {
        given()
                .when().get("/watch-models")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(5)
    public void testGetWatchModelById() {
        given()
                .pathParam("id", modelId)
                .when()
                .get("/watch-models/{id}")
                .then()
                .statusCode(200)
                .body("referenceNumber", is("REF-001"));
    }

    @Test
    @Order(6)
    public void testGetUnknownWatchModelReturnsNotFound() {
        given()
                .pathParam("id", 999999)
                .when()
                .get("/watch-models/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    public void testDeleteWatchModel() {
        given()
                .pathParam("id", modelId)
                .when()
                .delete("/watch-models/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", modelId)
                .when()
                .get("/watch-models/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(8)
    public void testDeleteUnknownWatchModelReturnsNotFound() {
        given()
                .pathParam("id", 999999)
                .when()
                .delete("/watch-models/{id}")
                .then()
                .statusCode(404);
    }
}
