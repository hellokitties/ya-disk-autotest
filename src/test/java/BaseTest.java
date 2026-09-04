import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseTest {
    static final String BASE_URL = "https://cloud-api.yandex.net/v1/disk";

    RequestSpecification getBaseRequestSpec() {
        return
                new RequestSpecBuilder().setBaseUri(BASE_URL)
                        .addHeader("Authorization", "OAuth " + System.getenv("OAUTH_TOKEN"))
                        .addHeader("Accept", ContentType.JSON.toString())
                        .build();
    }
}

