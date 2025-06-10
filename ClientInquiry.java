package PaymentTestCases;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.io.File;

public class ClientInquiry extends BaseTest
 {
    @Test (priority = 4)
    public void CheckInquiryWithValidNidPassedSuccessfully()
    {
        File body= new File ("src/test/resources/ClientValidNidInquiry.json");

        given()

                .baseUri("https://optest.souhoola.net")
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(body)
                .log().all()
        .when()
                .post("/api/onlinepayment/Inquiry")

        .then()

                .log().all()
                .assertThat().statusCode(200)
                .body("isSuccess", equalTo(true))
                .body("errors.code", nullValue())
                .body("errors.message", nullValue())
                .body("data.nationalId", equalTo("29507040101045"))
                .body("data.clientName", equalTo("Test"));
    }
     @Test (priority = 5)
     public void CheckInquiryWithInValidNidCanNotBePassedSuccessfully()
     {
         File body= new File ("src/test/resources/ClientInValidNidInquiry.json");

         given()

                 .baseUri("https://optest.souhoola.net")
                 .header("Authorization", "Bearer " + authToken)
                 .contentType("application/json")
                 .body(body)
                 .log().all()
                 .when()
                 .post("/api/onlinepayment/Inquiry")

         .then()

                 .log().all()
                 .assertThat().statusCode(200)
                 .body("isSuccess",equalTo(false))
                 .body("errors.code",equalTo("51"))
                 .body("errors.message",equalTo("National Id can't be empty and must be 14 number"))
                 .body("data", nullValue());
     }

}
