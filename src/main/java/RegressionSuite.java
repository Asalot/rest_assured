/**
 * Created by Aleksandr on 9/20/2020.
 */

import io.qameta.allure.restassured.AllureRestAssured;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RegressionSuite {
    @Test(testName = "Get tutors. Test 1")
    public void apiTest1() {
        given().
            filter(new AllureRestAssured()).
            when().
            get("http://51.15.94.14:5001/tutors").
            then().
            assertThat().statusCode(200);
        //get("/products").then().assertThat().body(matchesJsonSchemaInClasspath("products-schema.json"));
    }

    @Test(testName = "Get tutors. Test 2")
    public void apiTest2() {
        given().
                filter(new AllureRestAssured()).
                when().
                get("http://51.15.94.14:5001/tutors").
                then().
                assertThat().statusCode(200);
    }

    @Test(testName = "Get tutors. Test 3")
    public void apiTest3() {
        given().
                filter(new AllureRestAssured()).
                when().
                get("http://51.15.94.14:5001/tutors").
                then().
                assertThat().statusCode(200);
    }

    @Test
    //chromedriver.exe must be added to PATH variable
    public void uiTest1() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://qainusa.com");
        Assert.assertEquals(driver.getTitle(), "QA in USA course");
        driver.quit();
    }
}
