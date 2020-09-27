/**
 * Created by Aleksandr on 9/20/2020.
 */

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RegressionSuite {
    @Test(testName = "Get tutors. Test 1")
    public void apiTest1() {
        given().
            filter(new AllureRestAssured()).
            when().
            get("http://51.15.94.14:5001/tutors").
            then().
            assertThat().statusCode(200);
    }

    @Test(testName = "Get tutors. Schema validation. Test 2")
    public void apiTest2() {
        given().
                filter(new AllureRestAssured()).
                when().
                get("http://51.15.94.14:5001/tutors")
                .then()
                    .assertThat()
                .body(matchesJsonSchemaInClasspath("tutors-schema.json"));
    }

    @Test(testName = "Post a tutor. Test 3")
    public void apiTest3() {
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
                "    \"email\": \"robmc8@gmail.com\",\n" +
                "    \"county\": \"Australia\",\n" +
                "    \"expirienceYears\": 15,\n" +
                "    \"skillsDirections\": [\n" +
                "        \"for work\", \"for interview\"\n" +
                "    ]\n" +
                "}";
        given().
                filter(new AllureRestAssured()).
                contentType(ContentType.JSON).
                body(payload).
                when().
                post("http://51.15.94.14:5001/tutors").
                then().
                assertThat().statusCode(201);
    }

    @Test
    //chromedriver.exe must be added to PATH variable
    public void uiTest1() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://qainusa.com");
        Assert.assertEquals(driver.getTitle(), "QA in USA course");
        driver.quit();
    }

    @Test(testName = "1. Positive: POST (verify code, check response time, verify data, validate schema) -> Get (verify code)\n" +
            "\tPostcondition: DELETE by id (verify code 200)")
    public void createATutorPositive() {
        String body = "{   \n" +
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
                "    \"email\": \"robmc11@gmail.com\",\n" +
                "    \"county\": \"Australia\",\n" +
                "    \"expirienceYears\": 15,\n" +
                "    \"skillsDirections\": [\n" +
                "        \"for work\", \"for interview\"\n" +
                "    ]\n" +
                "}";
        given().
                filter(new AllureRestAssured()).
                contentType(ContentType.JSON).
                body(body).
                when().
                post("http://51.15.94.14:5001/tutors").
                then().
                assertThat().statusCode(201);
    }
}
