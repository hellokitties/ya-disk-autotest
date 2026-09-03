import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class DiskResourcesDeleteTest extends BaseTest
{
    private static final String PATH = "disk:/Тестовый_каталог" + UUID.randomUUID();

    @BeforeEach
    @DisplayName("Успешное создание каталога через API Яндекс.Диска")
    void createDirectory() {
        given()
                .spec(getBaseRequestSpec())
                .queryParam("path", PATH)
                .when()
                .put(BASE_URL+"/resources")
                .then()
                .statusCode(201)
                .log()
                .ifValidationFails();
    }

    @Test
    @DisplayName("Успешное удаление директории")
    public void testDeleteDirectory() {
        given()
             .spec(getBaseRequestSpec())
                .queryParam("path", PATH)
                .when()
                .delete(BASE_URL+"/resources")
                .then().log().ifValidationFails()
                .statusCode(204);

        given().spec(getBaseRequestSpec())
                .queryParam("path", "disk:/")
                .when()
                .get(BASE_URL+"/resources")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .body(not(containsString("\"path\":\""+PATH+"\"")));
    }
}

