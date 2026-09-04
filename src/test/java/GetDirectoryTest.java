import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetDirectoryTest extends BaseTest {
    private static final String DIRECTORY_NAME = "Тестовый_каталог_" +  UUID.randomUUID();
    private static final String PATH = "disk:/" + DIRECTORY_NAME;

    @BeforeEach
    @DisplayName("Успешное создание каталога через API Яндекс.Диска")
    void testCreateDirectorySuccessfully() {
        given()
                .spec(getBaseRequestSpec())
                .queryParam("path", PATH)
                .when()
                .put(BASE_URL + "/resources")
                .then()
                .statusCode(201)
                .log().ifValidationFails();
    }

    @Test
    @DisplayName("Получение метаинформации о каталоге")
    void getDirectoryTest(){
        given().spec(getBaseRequestSpec())
                .queryParam("path", PATH)
                .when()
                .get(BASE_URL + "/resources")
                .then()
                .statusCode(200)
                .body("type", equalTo("dir"))
                .and()
                .body("name", equalTo(DIRECTORY_NAME))
                .and().body("path", equalTo(PATH))
                .and().body("_embedded.path", equalTo(PATH))
                .and().body("_embedded.total", equalTo(0))
                .and().body("_embedded.items", not(hasItem(anything())));
    }

    @AfterEach
    @DisplayName("Удаление файла через API Яндекс.Диска")
    void deleteDirectory() {
        given()
                .spec(getBaseRequestSpec())
                .queryParams("path", PATH, "permanently", true)
                .when()
                .delete(BASE_URL + "/resources")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(204);
    }
}

