/**
 * Created by Aleksandr on 9/20/2020.
 */

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RegressionSuite {
    @Test
    public void apiTest1() {
        given().
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
