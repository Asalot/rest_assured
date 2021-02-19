/*
 * Created by Natalia 2/19/2021 based on Alexandr's file.
 */

import io.qameta.allure.Issue;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;



public class RegressionSuite {
    String ServerUrl="http://51.15.218.168:6001/tutors";
    public String GetBody(String id)//Body for request
    {
        String email = id + "qa@gmail.com";
        String payload = "{   \n" +
            "    \"id\":  \"" + id + "\",\n" +
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
        return payload;
    }

    @Test(description ="Positive Add a tutor")
    //"Steps: \n1.Create a tutor (post)\n2.Retrieve tutor's new record (get)" +
    //            "\n3.Delete created tutor (delete)\n4.Retrieve deleted record (get)"
    public void apiTestPositiveAddTutor() {
        String id = String.valueOf(new Random().nextInt(999999999));
        try {
            given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .body(GetBody(id)).
                    when()
                    .post(ServerUrl).
                    then()
                    .assertThat()
                    .statusCode(201)
                    .body(matchesJsonSchemaInClasspath("tutors-post-schema.json"))
                    .body("id", equalTo(id));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ",e.toString());
        }
        String urlWithTutorId = ServerUrl + "/" + id; //http://51.15.218.168:6001/tutors/1234123
        try {//step2
            given()
                    .filter(new AllureRestAssured())
                    .when().
                    get(urlWithTutorId)
                    .then()
                    .assertThat().statusCode(200)
           //         .body(matchesJsonSchemaInClasspath("tutors-get-schema.json"))
                    .body("id", equalTo(id));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ",e.toString());
        }
        try { //step3
            given().
                    filter(new AllureRestAssured()).
                    when()
                    .delete(urlWithTutorId).
                    then()
                    .assertThat().statusCode(200)
               // .body(matchesJsonSchemaInClasspath("tutors-delete-schema.json"))
                .body("id", equalTo(id));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ",e.toString());
        }
            given()//step4
                .filter(new AllureRestAssured())
                .when().
                        get(urlWithTutorId)
                .then()
                .assertThat().statusCode(404);
    }

    @Test(description ="Negative Create tutor with not all required fields")
    //"Steps: \n1.Create a tutor (post)\n2.Delete created tutor (delete)"
    public void apiTestNegativeTutorWithNotAllRequiredFields() {
        int id = new Random().nextInt(999999999);
        String payload = "{   \n" +
                "    \"id\":  \"" + id + "\",\n" +
                "    \"email\": \"" +id+ "qa@gmail.com\"" +
                         "}";
        given()//step1
            .filter(new AllureRestAssured())
            .contentType(ContentType.JSON)
            .body(payload).
            when()
            .post(ServerUrl).
            then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("tutors-post-schema.json"))
            .statusCode(greaterThanOrEqualTo(400));
       given()//step2
                .filter(new AllureRestAssured()).
               when()
                .delete(ServerUrl + "/" + id).
                then()
                .assertThat()
                .body("id",equalTo(String.valueOf(id)));

    }

    @Test(description ="Negative Create duplicate tutor (same ID)")
    // "Steps: \n1.Create new tutor" +
    //                    "\n2.Create a duplicate tutor with same ID (post)"+
    //                    "\n3.Delete created tutor (delete)\n4.Retrieve deleted record (get)"
     public void apiTestNegativeDuplicateTutorWithSameId() {
        String id = String.valueOf(new Random().nextInt(999999999));
        given()//step1
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(GetBody(id)).
        when()
                .post(ServerUrl).
        then()
                .assertThat().statusCode(201)
                .body("id",equalTo(id));

        given()//step2
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(GetBody(id)).
                when()
                .post(ServerUrl).
                then()
                .assertThat().statusCode(greaterThanOrEqualTo(400))
                .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));
        try { //step3
            given().
                    filter(new AllureRestAssured()).
                    when()
                    .delete(ServerUrl+"/"+id).
                    then()
                    .assertThat().statusCode(200)
                    // .body(matchesJsonSchemaInClasspath("tutors-delete-schema.json"))
                    .body("id", equalTo(id));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ",e.toString());
        }
        given()//step4
                .filter(new AllureRestAssured())
                .when().
                get(ServerUrl+"/"+id)
                .then()
                .assertThat().statusCode(404);
    }

    @Test(description ="Negative Create duplicate tutor (same email)")
    // "Steps: \n1.Create a tutor(post)\n" +
    //                    "2.Create a second tutor with email from first tutor" +
    //                    "\n3.Delete first tutor\n4.Delete second tutor")
    public void apiTestNegativeDuplicateTutorWithSameEmail() {
       String id = String.valueOf(new Random().nextInt(999999999));
       given()//step1
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(GetBody(id)).
                when()
                .post(ServerUrl).
                then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("tutors-post-schema.json"))
                .statusCode(201);
        String email = id + "qa@gmail.com";
        String id2 = String.valueOf(new Random().nextInt(999999999));
        String payload = "{   \n" +
                "    \"id\":  \"" + id2 + "\",\n" +
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
        given()//step2
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(payload).
                when()
                .post(ServerUrl).
                then()
                .assertThat().statusCode(409)
                .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));
        given()//step3
                .filter(new AllureRestAssured()).
                when()
                .delete(ServerUrl + "/" + id).
                then()
                .assertThat()
                .body("id",equalTo(id));
        given()//step4
                .filter(new AllureRestAssured()).
                when()
                .delete(ServerUrl + "/" + id2).
                then()
                .assertThat()
                .body("id",equalTo(id2));

    }

    @Test(description ="Negative Create a tutor with the empty email")
    public void apiTestNegativeCreateTutorEmptyEmail() {
        String id = String.valueOf(new Random().nextInt(999999999));
        String payload = "{   \n" +
                "    \"id\":  \"" + id + "\",\n" +
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
                "    \"email\": \"\",\n" +
                "    \"county\": \"Australia\",\n" +
                "    \"expirienceYears\": 15,\n" +
                "    \"skillsDirections\": [\n" +
                "        \"for work\", \"for interview\"\n" +
                "    ]\n" +
                "}";
        given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(payload).
                when()
                .post(ServerUrl).
                then()
                .assertThat()
                .statusCode(greaterThanOrEqualTo(400));

         /*   Response response = given()
                    .filter(new AllureRestAssured())
                    .when()
                    .get(urlWithTutorId);
            String responseBody = response.getBody().toString();
            Assert.assertFalse(responseBody.contains("carrots"));
*/
        //Post-conditions
    }

    @Test(description ="Negative Create a tutor with the empty ID")
    public void apiTestNegativeCreateTutorEmptyEId() {
        String payload = "{   \n" +
                "    \"id\":  \"\",\n" +
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
                "    \"email\": \"asd@asd.ru\",\n" +
                "    \"county\": \"Australia\",\n" +
                "    \"expirienceYears\": 15,\n" +
                "    \"skillsDirections\": [\n" +
                "        \"for work\", \"for interview\"\n" +
                "    ]\n" +
                "}";
        given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(payload).
                when()
                .post(ServerUrl).
                then()
                .assertThat()
                .statusCode(greaterThanOrEqualTo(400));
    }

    @Test(description ="Positive Get tutors")
    // testName ="Steps:\n1.Retrieve all tutors(get)" +
    //            "\n2.Create 3 new tutors\n3.Retrieve all tutors after adding new ones(get)" +
    //            "\n4.Delete 3 new created tutors(delete)\n4.Retrieve all tutors(get) and verify that total records are same")
    public void apiTestPositiveGetTutors() {
        Response response = (Response) //step1
                given()
                .filter(new AllureRestAssured())
                .when().
                        get(ServerUrl)
                .then()
                .assertThat().statusCode(200)
                .time(lessThan(1L), TimeUnit.SECONDS)
                .extract().
                        response();
        int TotalAmountOfTutors = response.path("list.size()");
        Allure.addAttachment("TotalAmountOfTutors:  ",String.valueOf(TotalAmountOfTutors));
        String[] ListOfTutor={"","",""};
        for(int i=0;i<3;i++) {//step2
            ListOfTutor[i] = String.valueOf(new Random().nextInt(999999999));
            given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .body(GetBody(ListOfTutor[i])).
                            when()
                    .post(ServerUrl).
                            then()
                    .assertThat()
                    .statusCode(201)
                    .body("id", equalTo(ListOfTutor[i]));
        }
        given()//step3
                .filter(new AllureRestAssured())
        .when().
                get(ServerUrl)
        .then()
                .assertThat().statusCode(200)
                .time(lessThan(1L), TimeUnit.SECONDS)
                .body("size()", equalTo(TotalAmountOfTutors+3));

      //DELETE tutors step4
        for (String i : ListOfTutor) {
            given().
                    filter(new AllureRestAssured()).
                    when()
                    .delete(ServerUrl + "/" + i).
                    then()
                    .assertThat().statusCode(200);
        }
        given()//step5
                .filter(new AllureRestAssured())
                .when().
                get(ServerUrl)
                .then()
                .assertThat().statusCode(200)
                .time(lessThan(1L), TimeUnit.SECONDS)
                .body("size()", equalTo(TotalAmountOfTutors));
         //       .body(matchesJsonSchemaInClasspath("tutors-get-schema.json"));
        Allure.addAttachment("TotalAmountOfTutorsAfterActivities:  ",String.valueOf(TotalAmountOfTutors));
    }

    @Test(description ="Positive Get tutor by ID")
    //testName ="Steps: \n1.Create a tutor (post)" +
    //            "\n2.Retrieve tutor's new record (get)" +
    //            "\n3.Delete created tutor (delete)\n4.Retrieve deleted record (get)")
    public void apiTestPositiveGetTutorById() {
        String id = String.valueOf(new Random().nextInt(999999999));
        given()//step1
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(GetBody(id)).
            when()
                .post(ServerUrl).
            then()
                .assertThat()
                .statusCode(201)
                .body("id",equalTo(id));
       String urlWithTutorId = ServerUrl + "/" + id;
        try {//step2
            given()
                    .filter(new AllureRestAssured())
                    .when().
                    get(urlWithTutorId)
                    .then()
                    .assertThat().statusCode(200)
                    .time(lessThan(1L), TimeUnit.SECONDS)
                    .body("id", equalTo(id))
                    .body(matchesJsonSchemaInClasspath("tutor-get-schema.json"));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ", e.toString());
        }
        given().//step3
                filter(new AllureRestAssured()).
                when()
                .delete(urlWithTutorId).
                then()
                .assertThat().statusCode(200)
                .body("id", equalTo(id));
        given()//step4
                .filter(new AllureRestAssured())
                .when().
                get(urlWithTutorId)
                .then()
                .assertThat().statusCode(404);

    }

    @Test(description ="Negative Delete all tutors (verify 405 error code,  verify schema)")
    public void apiTestNegativeDeleteAllTutors() {
        given()
                .filter(new AllureRestAssured())
        .when()
                .delete(ServerUrl).
        then()
                .assertThat().statusCode(405).time(lessThan(5l), TimeUnit.SECONDS)
                .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));
    }

    @Test(description ="Positive Delete a tutor")
    //           testName ="Steps: \n1.Create a tutor (post)\n2.Delete created tutor (delete)" +
    //                    "\n3.Retrieve deleted record (get)")
    public void apiTestPositiveDeleteTutor() {
        String id = String.valueOf(new Random().nextInt(999999999));
        given()//step1
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(GetBody(id)).
        when()
            .post(ServerUrl).
        then()
            .assertThat()
                .statusCode(201);
        String urlWithId = ServerUrl + "/" + id;
        try{//step2
            given()
                    .filter(new AllureRestAssured())
                    .when()
                    .delete(urlWithId)
                    .then().assertThat().statusCode(200).time(lessThan(1L), TimeUnit.SECONDS)
                    .body("id", equalTo(id))
                    .body(matchesJsonSchemaInClasspath("tutor-delete-schema.json"));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ", e.toString());
        }
        given()//step3
                    .filter(new AllureRestAssured())
                    .when()
                    .get(urlWithId)
                    .then()
                    .assertThat().statusCode(404).time(lessThan(1l), TimeUnit.SECONDS)
                    .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));
    }

    @Test(description ="Positive Update tutor name")
    //          testName ="Steps: \n1.Create a tutor (post)\n2.Update tutor by new name (patch)" +
    //            "\n3.Retrieve tutor's record (get)\n4.Delete tutor (delete)\n5.Retrieve deleted record (get)")
    //
    public void apiTestPositiveUpdateATutor() {
        String id = String.valueOf(new Random().nextInt(999999999));
        given()//step1
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(GetBody(id)).
                when()
                .post(ServerUrl).
                then()
                .assertThat()
                .statusCode(201);
        String expectedFirstLastName = "UPDATED Robert M2c";
        String urlWithTutorId = ServerUrl + "/" + id;
        try {//step2
            given()
                    .filter(new AllureRestAssured()).contentType(ContentType.JSON)
                     .body("{\"id\": \"" + id + "\", \"firstLastName\": \"" + expectedFirstLastName + "\"}")
                    .when()
                    .patch(urlWithTutorId)
                    .then()
                    .assertThat().statusCode(200)
                    .body("firstLastName", equalTo(expectedFirstLastName))
                    .time(lessThan(1l), TimeUnit.SECONDS).body(matchesJsonSchemaInClasspath("tutors-post-schema.json"));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ", e.toString());
        }
        try {//step3
            given()
                    .filter(new AllureRestAssured())
                    .when()
                    .get(urlWithTutorId)
                    .then().assertThat().statusCode(200).body("firstLastName", equalTo(expectedFirstLastName));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ", e.toString());
        }
            //step 4
        try {
            given().filter(new AllureRestAssured())
                    .when().delete(urlWithTutorId).then().statusCode(200)
                    .body("firstLastName", equalTo(expectedFirstLastName));

        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ", e.toString());
        }
        given()//step5
                .filter(new AllureRestAssured())
                .when()
                .get(ServerUrl+"/"+id)
                .then()
                .assertThat().statusCode(404).time(lessThan(1l), TimeUnit.SECONDS)
                .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));

    }

    @Test(description ="Negative Update tutor ID by \"\"")
    //          testName ="Steps: \n1.Create a tutor (post)\n2.Update tutor's ID=\"\" (patch)" +
    //                    "\n3.Retrieve tutor's record (get)\n4.Delete tutor (delete)\n5.Retrieve deleted record (get)")
    public void apiTestNegativeUpdateID() {
        String id = String.valueOf(new Random().nextInt(999999999));
        given()//step1
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(GetBody(id)).
                when()
                .post(ServerUrl).
                then()
                .assertThat()
                .statusCode(201);
        String urlWithTutorId = ServerUrl + "/" + id;
        try {//step2
            given()
                    .filter(new AllureRestAssured()).contentType(ContentType.JSON)
                    .body("{\"id\": \"\"}")
                    .when()
                    .patch(urlWithTutorId)
                    .then()
                    .assertThat().statusCode(greaterThanOrEqualTo(400))
                    .body("id", equalTo(""))
                    .time(lessThan(1l), TimeUnit.SECONDS).body(matchesJsonSchemaInClasspath("tutors-post-schema.json"));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ", e.toString());
        }
        try {//step3
            given()
                    .filter(new AllureRestAssured())
                    .when()
                    .get(ServerUrl + "/")
                    .then().assertThat().statusCode(200)
                    .body("id", equalTo(""));
        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ", e.toString());
        }
        //step 4
        try {
            given().filter(new AllureRestAssured())
                    .when().delete(ServerUrl + "/").then().statusCode(200)
                    .body("id", equalTo(""));

        } catch (AssertionError e) {
            Allure.addAttachment("Error:  ", e.toString());
        }
        given()//step5
                .filter(new AllureRestAssured())
                .when()
                .get(ServerUrl+"/")
                .then()
                .assertThat().statusCode(404).time(lessThan(1l), TimeUnit.SECONDS)
                .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));

    }

    @Test(description ="Negative Update tutor email by \"\"")
    //      testName ="Steps: \n1.Create a tutor (post)\n2.Update tutor's email=\"\" (patch)" +
    //                "\n3.Retrieve tutor's record (get)\n4.Delete tutor (delete)\n5.Retrieve deleted record (get)")
     public void apiTestNegativeUpdateEmail() {
        String id = String.valueOf(new Random().nextInt(999999999));
        given()//step1
            .filter(new AllureRestAssured())
            .contentType(ContentType.JSON)
            .body(GetBody(id)).
            when()
            .post(ServerUrl).
            then()
            .assertThat()
            .statusCode(201);
    String urlWithTutorId = ServerUrl + "/" + id;
    try {//step2
        given()
                .filter(new AllureRestAssured()).contentType(ContentType.JSON)
                .body("{\"id\": \""+id+"\",\"email\":\"\"}")
                .when()
                .patch(urlWithTutorId)
                .then()
                .assertThat().statusCode(greaterThanOrEqualTo(400))
                .body("email", equalTo(""))
                .time(lessThan(1l), TimeUnit.SECONDS).body(matchesJsonSchemaInClasspath("tutors-post-schema.json"));
    } catch (AssertionError e) {
        Allure.addAttachment("Error:  ", e.toString());
    }
    try {//step3
        given()
                .filter(new AllureRestAssured())
                .when()
                .get(urlWithTutorId)
                .then().assertThat().statusCode(200)
                .body("id", equalTo(""))
                .body("email", equalTo(""));
    } catch (AssertionError e) {
        Allure.addAttachment("Error:  ", e.toString());
    }
    //step 4
    try {
        given().filter(new AllureRestAssured())
                .when().delete(urlWithTutorId).then().statusCode(200)
                .body("id", equalTo(""))
                .body("email", equalTo(""));

    } catch (AssertionError e) {
        Allure.addAttachment("Error:  ", e.toString());
    }
    given()//step5
            .filter(new AllureRestAssured())
            .when()
            .get(ServerUrl+"/")
            .then()
            .assertThat().statusCode(404).time(lessThan(1l), TimeUnit.SECONDS)
            .body(matchesJsonSchemaInClasspath("tutors-error-schema.json"));

}

}
