package PaymentTestCases;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;


public class Payment extends BaseTest
{
    @Test(priority = 1)
    public void CheckPaymentForClientWithValidDataPassedSuccessfully()
    {
        {
            performClientInquiry();
            executePayment();
            checkPayment();
        }
    }

    private void performClientInquiry()
    {
        File body = new File("src/test/resources/ClientValidNidInquiry.json");

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
    public void executePayment()
    {
        String Body = "{\n" +
                "    \"NID\": \"" + nid + "\",\n" +
                "    \"ReceiptNumber\": \"0\",\n" +
                "    \"Amount\": 1,\n" +
                "    \"RequestId\": " + requestId + "\n" +
                "}";

        given()

                .baseUri("https://optest.souhoola.net")
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(Body)
                .log().all()

        .when()

                .post("/api/onlinepayment/payment")

        .then()

                .log().all()
                .assertThat().statusCode(200)
                .body("isSuccess", equalTo(true))
                .body("errors.code", nullValue())
                .body("errors.message", nullValue())
                .body("data", equalTo(true));
    }

    public void checkPayment()
    {
        given()

                .baseUri("https://optest.souhoola.net")
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .queryParam("requestId", requestId)
                .log().all()

        .when()
                .get("/api/onlinepayment/CheckPayment")
        .then()
                .log().all()
                .assertThat().statusCode(200)
                .body("isSuccess", equalTo(true))
                .body("errors.code", nullValue())
                .body("errors.message", nullValue())
                .body("data", equalTo("Payment Success"));
    }

    @Test(priority = 2)
    public void CheckPaymentForClientWithNegativeAmountCannotPassedSuccessfully()
    {
        {
            executePaymentWithNegativeAmount();
        }
    }
    private void executePaymentWithNegativeAmount()
    {
        String Body = "{\n" +
                "    \"NID\": \"" + nid + "\",\n" +
                "    \"ReceiptNumber\": \"0\",\n" +
                "    \"Amount\": -1,\n" +
                "    \"RequestId\": " + requestId + "\n" +
                "}";

        given()

                .baseUri("https://optest.souhoola.net")
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(Body)
                .log().all()

        .when()

                .post("/api/onlinepayment/payment")

        .then()

                .log().all()
                .assertThat().statusCode(200)
                .body("isSuccess", equalTo(false))
                .body("errors.code", equalTo("58"))
                .body("errors.message", equalTo("Payment amount can't be 0 or less"))
                .body("data",nullValue());
    }
    @Test(priority = 3)
    public void CheckPaymentForClientWithInvalidRequestIdCannotPassedSuccessfully()
    {
        {
            executePaymentWithInvalidRequestId();
        }
    }
    private void executePaymentWithInvalidRequestId()
    {
        String Body = "{\n" +
                "    \"NID\": \"" + nid + "\",\n" +
                "    \"ReceiptNumber\": \"0\",\n" +
                "    \"Amount\": 1,\n" +
                "    \"RequestId\": 0\n" +
                "}";

        given()

                .baseUri("https://optest.souhoola.net")
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(Body)
                .log().all()

                .when()

                .post("/api/onlinepayment/payment")

                .then()

                .log().all()
                .assertThat().statusCode(200)
                .body("isSuccess", equalTo(false))
                .body("errors.code", equalTo("63"))
                .body("errors.message", equalTo("Invalid RequestId"))
                .body("data",nullValue());
    }
    @Test(priority = 4)
    public void CheckPaymentForClientWithTheSameRequestIdTwiceCannotPassedSuccessfully()
    {
        {
            executePaymentWithSameRequestId();
        }
    }
    private void executePaymentWithSameRequestId()
    {
        String Body = "{\n" +
                "    \"NID\": \"" + nid + "\",\n" +
                "    \"ReceiptNumber\": \"0\",\n" +
                "    \"Amount\": 1,\n" +
                "    \"RequestId\": " + requestId + "\n" +
                "}";

        given()

                .baseUri("https://optest.souhoola.net")
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(Body)
                .log().all()

                .when()

                .post("/api/onlinepayment/payment")

                .then()

                .log().all()
                .assertThat().statusCode(200)
                .body("isSuccess", equalTo(false))
                .body("errors.code", equalTo("68"))
                .body("errors.message", equalTo("Payment can't complete because Inquiry failed"))
                .body("data",nullValue());
    }

}
