/**
 * Created by Aleksandr on 9/20/2020.
 */

import io.qameta.allure.Issue;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

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
                .assertThat()
                .time(lessThan(1L), TimeUnit.SECONDS)
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("tutors-post-schema.json"))
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

    @Test(description = "Positive. Get tutors.")
    public void apiTestREADPositiveGetTutors() {
        String email1 = new Random().nextInt(999999999) + "qa@gmail.com";
        String email2 = new Random().nextInt(999999999) + "qa@gmail.com";
        String payload1 = "{   \n" +
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
                "    \"email\": \"" + email1 + "\",\n" +
                "    \"county\": \"Australia\",\n" +
                "    \"expirienceYears\": 15,\n" +
                "    \"skillsDirections\": [\n" +
                "        \"for work\", \"for interview\"\n" +
                "    ]\n" +
                "}";
        String payload2 = "{   \n" +
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
                "    \"email\": \"" + email2 + "\",\n" +
                "    \"county\": \"Australia\",\n" +
                "    \"expirienceYears\": 15,\n" +
                "    \"skillsDirections\": [\n" +
                "        \"for work\", \"for interview\"\n" +
                "    ]\n" +
                "}";
        String url = "http://51.15.94.14:5001/tutors";
        String tutor1Id =
            given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(payload1).
            when()
                .post(url).
            then()
                .assertThat()
                .statusCode(201)
                .extract().
                        path("id");

        String tutor2Id = given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(payload2).
                        when()
                .post(url).
                        then()
                .assertThat()
                .statusCode(201)
                .extract().
                        path("id");
        String urlWithTutor1Id = url + "/" + tutor1Id;
        String urlWithTutor2Id = url + "/" + tutor2Id;
        given()
                .filter(new AllureRestAssured())
        .when().
                get(url)
        .then()
                .assertThat().statusCode(200)
                .time(lessThan(1L), TimeUnit.SECONDS)
                .body(matchesJsonSchemaInClasspath("tutors-schema.json"));
        //DELETE tutor 1
        given().
                filter(new AllureRestAssured()).
                when()
                .delete(urlWithTutor1Id).
                then()
                .assertThat().statusCode(200);
        //DELETE tutor 2
        given().
                filter(new AllureRestAssured()).
                when()
                .delete(urlWithTutor2Id).
                then()
                .assertThat().statusCode(200);
    }

    @Test(description = "Get tutor by ID")
    public void apiTestREADPositiveGetTutorById() {
        String email = new Random().nextInt(9999999) + "qa@gmail.com";
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
        String tutorId =
            given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(payload).
            when()
                .post(url).
            then()
                .assertThat()
                .statusCode(201)
                .extract().
                        path("id");
        String urlWithTutorId = url + "/" + tutorId;
        given()
                .filter(new AllureRestAssured())
                .when().
                get(urlWithTutorId)
                .then()
                .assertThat().statusCode(200)
                .time(lessThan(1L), TimeUnit.SECONDS)
                .body(matchesJsonSchemaInClasspath("tutor-get-schema.json"));
        given().
                filter(new AllureRestAssured()).
                when()
                .delete(urlWithTutorId).
                then()
                .assertThat().statusCode(200);
    }

    @Test(description = "Negative \"Delete all tutors\" (verify 405 error code,  verify schema)")
    public void apiTestDELETENegativeDeleteAllTutors() {
        String url = "http://51.15.94.14:5001/tutors";
        given()
                .filter(new AllureRestAssured())
        .when()
                .delete(url).
        then()
                .assertThat().statusCode(405).time(lessThan(5l), TimeUnit.SECONDS)
                .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));
    }

    @Test(description = "Positive \"Delete a tutor\" ")
    public void apiTestDELETEPositiveDeleteATutor() {
        String url = "http://51.15.94.14:5001/tutors";
        String email = new Random().nextInt(9999999) + "qa@gmail.com";
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
        String tutorId =
        given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(payload).
        when()
            .post(url).
        then()
            .assertThat()
                .statusCode(201)
                .extract()
                .path("id");
        String urlWithId = url + "/" + tutorId;
        given()
                .filter(new AllureRestAssured())
        .when()
                .delete(urlWithId)
        .then().assertThat().statusCode(200).time(lessThan(1L), TimeUnit.SECONDS)
                .body(matchesJsonSchemaInClasspath("tutor-delete-schema.json"));

        given()
                .filter(new AllureRestAssured())
        .when()
                    .get(urlWithId)
        .then()
                    .assertThat().statusCode(404).time(lessThan(1l), TimeUnit.SECONDS)
                    .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));
    }

    @Test(description = "Update tutor name. Positive")
    public void apiTestUPDATEPositiveUpdateATutor() {
        String url = "http://51.15.94.14:5001/tutors";
        String email = new Random().nextInt(9999999) + "qa@gmail.com";
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
        String tutorId =
                given()
                        .filter(new AllureRestAssured())
                        .contentType(ContentType.JSON)
                        .body(payload).
                        when()
                        .post(url).
                        then()
                        .assertThat()
                        .statusCode(201)
                        .extract()
                        .path("id");

        String expectedFirstLastName = "UPDATED Robert M2c";
        String urlWithTutorId = url + "/" + tutorId;
            given()
                .filter(new AllureRestAssured()).contentType(ContentType.JSON).body("{\"firstLastName\": \""+ expectedFirstLastName + "\"}")
            .when()
                .patch(urlWithTutorId)
            .then()
                .assertThat().statusCode(200)
                .time(lessThan(1l), TimeUnit.SECONDS).body(matchesJsonSchemaInClasspath("tutors-post-schema.json"));

        given()
                .filter(new AllureRestAssured())
            .when()
                .get(urlWithTutorId)
            .then().assertThat().statusCode(200).body("firstLastName", equalTo(expectedFirstLastName));

        //Post-conditions
        given().filter(new AllureRestAssured())
                .when().delete(urlWithTutorId).then().statusCode(200);
        }

        @Test(description = "Negative \"Update a tutor with the empty name\"")
        @Issue("E2L-48")
        public void apiTestUPDATENegativeUpdateATutor() {
            String url = "http://51.15.94.14:5001/tutors";
            String email = new Random().nextInt(9999999) + "qa@gmail.com";
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
            String tutorId =
                    given()
                            .filter(new AllureRestAssured())
                            .contentType(ContentType.JSON)
                            .body(payload).
                            when()
                            .post(url).
                            then()
                            .assertThat()
                            .statusCode(201)
                            .extract()
                            .path("id");

            String urlWithTutorId = url + "/" + tutorId;
            given()
                    .filter(new AllureRestAssured()).contentType(ContentType.JSON).body("{\"carrots\": 20}")
                    .when()
                    .patch(urlWithTutorId)
                    .then()
                    .assertThat().statusCode(400)
                    .time(lessThan(1l), TimeUnit.SECONDS).body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));

            Response response = given()
                    .filter(new AllureRestAssured())
                    .when()
                    .get(urlWithTutorId);
            String responseBody = response.getBody().toString();
            Assert.assertFalse(responseBody.contains("sell"));

            //Post-conditions
            given().filter(new AllureRestAssured())
                    .when().delete(urlWithTutorId).then().statusCode(200);
        }
}
