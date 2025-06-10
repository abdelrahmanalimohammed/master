package PaymentTestCases;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.io.File;

public class Login
{

    @Test (priority = 1)
    public void CheckLoginWithValidDataPassedSuccessfully()
    {
        File body= new File ("src/test/resources/PaymentCompanyLoginValidData.json");

        given()

                .baseUri("https://optest.souhoola.net")
                .contentType("application/json")
                .body(body)
                .log().all()
        .when()
                .post("/api/onlinepayment/Authenticate")

        .then()

                .log().all()
                .assertThat().statusCode(200)
                .body("isSuccess", equalTo(true))
                .body("errors.code", nullValue())
                .body("errors.message", nullValue())
                .body("data.token", notNullValue());
    }

    @Test (priority = 2)
    public void VerifyThatLoginCanNotBeProcessedSuccessfullyWithEmptyInputData()
    {
        File body= new File ("src/test/resources/PaymentCompanyLoginEmptyInput.json");

        given()

                .baseUri("https://optest.souhoola.net")
                .contentType("application/json")
                .body(body)
                .log().all()
        .when()
                .post("/api/onlinepayment/Authenticate")

        .then()

                .log().all()
                .assertThat().statusCode(200)
                .body("isSuccess",equalTo(false))
                .body("errors.code",equalTo("62"))
                .body("errors.message",equalTo("Invalid UserName or Password"))
                .body("data", nullValue());
    }

    @Test (priority = 3)
    public void VerifyThatLoginCanNotBeProcessedSuccessfullyWithInvalidData()
    {
        File body= new File ("src/test/resources/PaymentCompanyLoginInvalidData.json");

        given()

                .baseUri("https://optest.souhoola.net")
                .contentType("application/json")
                .body(body)
                .log().all()
        .when()
                .post("/api/onlinepayment/Authenticate")

        .then()

                .log().all()
                .assertThat().statusCode(200)
                .body("isSuccess",equalTo(false))
                .body("errors.code",equalTo("62"))
                .body("errors.message",equalTo("Invalid UserName or Password"))
                .body("data", nullValue());
    }

}

