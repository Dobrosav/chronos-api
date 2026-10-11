package io.dobrosav.collection;

import io.dobrosav.brand.BrandDto;
import io.dobrosav.model.WatchModelDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WatchItemResourceTest {

    private static Long watchModelId;
    private static Long watchItemId;

    @Test
    @Order(1)
    public void testCreateWatchItem() {
        BrandDto brand = new BrandDto(null, "Tudor-WatchItemTest", "Switzerland", 1926);
        Long brandId = given()
                .contentType(ContentType.JSON)
                .body(brand)
                .when()
                .post("/brands")
                .then()
                .statusCode(200)
                .extract().jsonPath().getLong("id");

        WatchModelDto modelDto = new WatchModelDto(null, "M79030N-0001", "Automatic", "Steel", brandId);
        watchModelId = given()
                .contentType(ContentType.JSON)
                .body(modelDto)
                .when()
                .post("/watch-models")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        WatchItemDto itemDto = new WatchItemDto(
                null,
                watchModelId,
                LocalDate.of(2023, 5, 20),
                "SN-12345678",
                "MINT"
        );

        watchItemId = given()
                .contentType(ContentType.JSON)
                .body(itemDto)
                .when()
                .post("/watch-items")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("watchModelId", is(watchModelId.intValue()))
                .body("serialNumber", is("SN-12345678"))
                .body("condition", is("MINT"))
                .body("purchaseDate", is("2023-05-20"))
                .extract().jsonPath().getLong("id");
    }

    @Test
    @Order(2)
    public void testCreateWatchItemWithUnknownModelReturnsNotFound() {
        WatchItemDto itemDto = new WatchItemDto(
                null,
                999999L,
                LocalDate.of(2023, 1, 1),
                "SN-000000",
                "NEW"
        );

        given()
                .contentType(ContentType.JSON)
                .body(itemDto)
                .when()
                .post("/watch-items")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(3)
    public void testGetAllWatchItems() {
        given()
                .when().get("/watch-items")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(4)
    public void testGetWatchItemById() {
        given()
                .pathParam("id", watchItemId)
                .when()
                .get("/watch-items/{id}")
                .then()
                .statusCode(200)
                .body("id", is(watchItemId.intValue()))
                .body("watchModelId", is(watchModelId.intValue()))
                .body("serialNumber", is("SN-12345678"))
                .body("condition", is("MINT"))
                .body("purchaseDate", is("2023-05-20"));
    }

    @Test
    @Order(5)
    public void testGetUnknownWatchItemReturnsNotFound() {
        given()
                .pathParam("id", 999999)
                .when()
                .get("/watch-items/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(6)
    public void testDeleteWatchItem() {
        given()
                .pathParam("id", watchItemId)
                .when()
                .delete("/watch-items/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", watchItemId)
                .when()
                .get("/watch-items/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    public void testDeleteUnknownWatchItemReturnsNotFound() {
        given()
                .pathParam("id", 999999)
                .when()
                .delete("/watch-items/{id}")
                .then()
                .statusCode(404);
    }
}
