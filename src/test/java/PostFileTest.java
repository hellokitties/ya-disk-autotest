import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasEntry;

public class PostFileTest extends BaseTest {
    private static final String FILE_NAME = "flowers.jpg";
    private static final String PATH = "disk:/" + FILE_NAME;
    private static final String EXTERNAL_IMAGE_URL = "https://avatars.mds.yandex.net/i?id=923dd649118e22d10c381f0b01204b68c29bdc80-5343653-images-thumbs&n=13";

    @Test
    @DisplayName("Асинхронная загрузка файла по внешней ссылке")
    void testUploadFile() {
        var response = given()
                .spec(getBaseRequestSpec())
                .queryParams("path", PATH, "url", EXTERNAL_IMAGE_URL)
                .when()
                .post(BASE_URL + "/resources/upload")
                .then()
                .statusCode(202)
                .log().ifValidationFails();

        var href = response.extract().jsonPath().getString("href");

        await()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        given().spec(getBaseRequestSpec())
                                .when()
                                .get(href)
                                .then()
                                .statusCode(200)
                                .body("status", equalTo("success"))
                );

        given().spec(getBaseRequestSpec())
                .queryParam("path", "disk:/")
                .when()
                .get(BASE_URL + "/resources")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .body("_embedded.items",
                        hasItem(allOf(hasEntry("type", "file"),
                                hasEntry("name", FILE_NAME),
                                hasEntry("path", PATH)))
                );
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

