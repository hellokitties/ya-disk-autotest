import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class PutDirectoryTest extends BaseTest {
    private static final String PATH = "disk:/Тестовый_каталог" + UUID.randomUUID();

    @Test
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

        given().spec(getBaseRequestSpec())
                .queryParam("path", "disk:/")
                .when()
                .get(BASE_URL + "/resources")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .body(containsString("\"path\":\"" + PATH + "\""));
    }

}

