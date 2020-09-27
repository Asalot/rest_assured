/**
 * Created by Aleksandr on 9/20/2020.
 */

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RegressionSuite {
    @Test(testName = "Positive \"Add a tutor\"")
    public void apiTestCREATEPositiveAddATutor() {
        String email = new Random().nextInt(999999999) + "qa@gmail.com";
        String payload = "{   \n" +
                "    \"title\": \"TEST\",\n" +
                "    \"pricePerHourTutorTakesDollars\": 200,\n" +
                "    \"isActive\": true,\n" +
                "    \"firstLastName\": \"Robert M2c\",\n" +
                "    \"photoLocation\": \"https://thumb.tildacdn.com/tild3731-3938-4237-b962-346136376436/-/cover/560x560/center/center/-/format/webp/sdfewfwe.jpg\",\n" +
                "    \"languagesTutorTeaches\": [\n" +
                "        {\n" +
                "            \"english\": \"Native\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"phone\": \"+23238583372974\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"county\": \"Australia\",\n" +
                "    \"expirienceYears\": 15,\n" +
                "    \"skillsDirections\": [\n" +
                "        \"for work\", \"for interview\"\n" +
                "    ]\n" +
                "}";
        String url = "http://51.15.94.14:5001/tutors";
        String tutorId = given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(payload).
        when()
                .post(url).
        then()
                .assertThat().statusCode(201).body(matchesJsonSchemaInClasspath("tutors-post-schema.json"))
                .extract().
                path("id");
        String urlWithTutorId = url + "/" + tutorId; //http://51.15.94.14:5001/tutors/1234123
        given()
                .filter(new AllureRestAssured())
        .when().
                get(urlWithTutorId)
        .then()
                .assertThat().statusCode(200);

        given().
                filter(new AllureRestAssured()).
        when()
                .delete(urlWithTutorId).
        then()
                .assertThat().statusCode(200);
    }

    @Test(testName = "Negative create tutor")
    public void apiTestCREATENegativeAddATutor() {
        String existingEmail = "robmc8@gmail.com";
        String payload = "{   \n" +
                "    \"title\": \"TEST\",\n" +
                "    \"pricePerHourTutorTakesDollars\": 200,\n" +
                "    \"isActive\": true,\n" +
                "    \"firstLastName\": \"Robert M2c\",\n" +
                "    \"photoLocation\": \"https://thumb.tildacdn.com/tild3731-3938-4237-b962-346136376436/-/cover/560x560/center/center/-/format/webp/sdfewfwe.jpg\",\n" +
                "    \"languagesTutorTeaches\": [\n" +
                "        {\n" +
                "            \"english\": \"Native\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"phone\": \"+23238583372974\",\n" +
                "    \"email\": \"" + existingEmail + "\",\n" +
                "    \"county\": \"Australia\",\n" +
                "    \"expirienceYears\": 15,\n" +
                "    \"skillsDirections\": [\n" +
                "        \"for work\", \"for interview\"\n" +
                "    ]\n" +
                "}";
        String url = "http://51.15.94.14:5001/tutors";
        given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(payload).
        when()
                .post(url).
        then()
                .assertThat().statusCode(409)
                .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));
    }
}
