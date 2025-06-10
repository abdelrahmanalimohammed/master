package PaymentTestCases;
import io.restassured.response.Response;
import java.io.File;
import static io.restassured.RestAssured.*;

public class BaseTest {

    protected static String authToken;
    protected static String nid;
    protected static int requestId;

    static {
        generateAuthToken();
        retrieveNIDAndRequestId();
    }
    private static void generateAuthToken() {
        File body= new File ("src/test/resources/PaymentCompanyLoginValidData.json");

        Response response = given()
                .baseUri("https://optest.souhoola.net")
                .contentType("application/json")
                .body(body)
        .when()
                .post("/api/onlinepayment/Authenticate");

        authToken = response.jsonPath().getString("data.token");
        System.out.println("Auth Token: " + authToken);
    }
    private static void retrieveNIDAndRequestId() {
        File body= new File ("src/test/resources/ClientValidNidInquiry.json");
        Response response = given()
                .baseUri("https://optest.souhoola.net")
                .contentType("application/json")
                .accept("application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(body)
                .when()
                .post("/api/onlinepayment/Inquiry");

        // Debugging Logs
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        nid = response.jsonPath().getString("data.nationalId");
        requestId = response.jsonPath().getInt("data.requestId");

        if (nid == null) {
            System.out.println("NID retrieval failed: Check response structure.");
        } else {
            System.out.println("Retrieved NID: " + nid);
        }
    }


}