package tests;

import com.github.javafaker.Faker;
import lombok.CreateUser;
import lombok.LombokUserData;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.*;

public class ReqResTests {

    static Faker faker = new Faker();

    static String firstName = faker.name().firstName();
    static String job = faker.job().title();

    @Test
    void singleUserTest() {
        LombokUserData userData = given()
                .spec(request)
        .when()
                .get("user/3")
        .then()
                .spec(response)
                .log().body()
                .extract().as(LombokUserData.class);
        assertEquals("Emma", userData.getUser().getFirstName());
    }

    @Test
    void updateUserTest() {
        CreateUser userData = given()
                .spec(request)
                .body("{\"name\": " + "\"" + firstName + "\"" + "," +
                "\"job\": " + "\"" + job + "\"}")
        .when()
                .patch("user/3")
        .then()
                .spec(response)
                .log().body()
                .extract().as(CreateUser.class);
        assertEquals(firstName, userData.getName());
        assertEquals(job, userData.getJob());
    }

    @Test
    void singleUserWithGroovyTest(){
        given()
                .spec(request)
        .when()
                .get("users")
        .then()
                .spec(response)
                .log().body()
                .body("data.findAll{it.email=~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"))
                .body("data.findAll{it.first_name}.first_name.flatten().",
                        hasItem("Emma"));
    }
}
