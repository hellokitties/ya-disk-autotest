import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class DeleteDirectoryTest extends BaseTest {
    private static final String DIRECTORY_NAME = "Тестовый_каталог_b3713ef4-7397-45e8-8056-7d1e38c7e8f3";
    private static final String PATH = "disk:/" + DIRECTORY_NAME;

    @BeforeEach
    @DisplayName("Создание каталога через API Яндекс.Диска")
    void createDirectory() {
       given()
                .spec(getBaseRequestSpec())
                .queryParam("path", PATH)
                .when()
                .put(BASE_URL + "/resources")
                .then()
                .statusCode(201)
                .log()
                .ifValidationFails();
    }

    @Test
    @DisplayName("Успешное удаление директории - директория попадает в корзину")
    public void testDeleteDirectory() {
        given()
                .spec(getBaseRequestSpec())
                .queryParam("path", PATH)
                .when()
                .delete(BASE_URL + "/resources")
                .then().log().ifValidationFails()
                .statusCode(204);

        given().spec(getBaseRequestSpec())
                .queryParam("path", "disk:/")
                .when()
                .get(BASE_URL + "/resources")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .body(not(containsString("\"path\":\"" + PATH + "\"")));

        given().spec(getBaseRequestSpec())
                .queryParam("path", "trash:/")
                .when()
                .get(BASE_URL + "/trash/resources")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .body("_embedded.items",
                        hasItem(allOf(hasEntry("type", "dir"),
                                hasEntry("name", DIRECTORY_NAME),
                                hasEntry("origin_path", PATH))
                        )
                );
    }
}

