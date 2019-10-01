package ArvatoTst;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class StepDefs {

    //region UI Steps

    Integer desktopWidth = 980;
    String contactUsPage = "Contact Us";

    @Given("I am on the front page")
    public void i_am_on_the_front_page() {
        Configuration.browser = "chrome";
        open("https://itarvato.ee");
        $("title")
            .waitUntil(Condition.exist,2000)
            .shouldHave(Condition.attribute("text", "IT Arvato"));
    }

    @When("page width {int}")
    public void page_width(Integer width) {
        getWebDriver().manage().window().setSize(new Dimension(width, 768));
    }

    @When("I click on {string} link")
    public void i_click_on_link(String page) {
        Integer width = getWebDriver().manage().window().getSize().width;

        switchTo().window(0);

        if (width < desktopWidth){
            $(".oceanwp-mobile-menu-icon")
                .shouldBe(Condition.visible)
                .find(By.cssSelector(".mobile-menu"))
                .click();

           $$(".sidr-inner .sidr-class-menu-link")
                .findBy(Condition.text(page))
                .click();
        } else {
            $$(".main-navigation .menu-link")
                .findBy(Condition.text(page))
                .click();
        }
    }

    @Then("I should see open vacancies on {string} page")
    public void i_should_see_open_vacancies_on_page(String page) {
        $("title")
            .waitUntil(Condition.exist,2000)
            .shouldHave(Condition.attribute("text", page + " – IT Arvato"));

        $$("div")
            .find(Condition.text("Current openings"))
            .waitUntil(Condition.exist,2000);
    }

    @When("I click on Contact Us link")
    public void i_click_on_Contact_Us_link() {
        switchTo().window(0);
        i_click_on_link(contactUsPage);
    }

    @Then("I should see company location")
    public void i_should_see_company_location() {
        $("title")
                .waitUntil(Condition.exist,2000)
                .shouldHave(Condition.attribute("text", contactUsPage + " – IT Arvato"));

        $$("div")
                .find(Condition.attribute("data-id","44d561f"))
                .waitUntil(Condition.exist,2000)
                .shouldHave(Condition.text("Telliskivi 60-2, I hoone, 10412 Tallinn"));
    }

    @Then("should be able to open the map link")
    public void should_be_able_to_open_the_map_link() {
        $$("div")
                .find(Condition.attribute("data-id", "44d561f"))
                .find(By.tagName("a"))
                .click();

        switchTo().window(1);

        if (!url().contains("google.com/maps/")) {
            Assert.fail("URL should contain google.com/maps/");
        }
    }

    @Then("should be able to navigate to company social media")
    public void should_be_able_to_navigate_to_company_social_media() {
        boolean facebookError = false;
        boolean linkedinError = false;

        switchTo().window(0);

        SelenideElement element = $$("div").find(Condition.attribute("data-id","ae8893a"));

        element.find(By.className("fa-facebook-official")).click();

        if (!url().contains("facebook.com")) {
            facebookError = true;
        }

        element.find(By.className("fa-linkedin-square")).click();

        if (!url().contains("linkedin.com")) {
            linkedinError = true;
        }

        if (facebookError && linkedinError) {
            Assert.fail("Facebook icon click should open facebook.com; LinkedIn icon click should open LinkedIn");
        } else if (facebookError) {
            Assert.fail("Facebook icon click should open facebook.com");
        } else if (linkedinError) {
            Assert.fail("LinkedIn icon click should open LinkedIn");
        }
    }
    //endregion

    //region REST Steps

    String restApi = "https://reqres.in/api/users/";

    @When("I get user by id {int} I get back user data")
    public void i_get_user_by_id(Integer id) {
        given()
                .when()
                .get(restApi + id)
                .then()
                .statusCode(200)
                .assertThat()
                .body("data.id", equalTo(id));
    }

    @Given("I create user with {string} and {string} I get back user data I created")
    public void i_create_user_with_and(String name, String job) {
        Map<String, Object>  json = new HashMap<>();
        json.put("name", name);
        json.put("job", job);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(restApi)
                .then()
                .statusCode(201)
                .assertThat()
                .body("name", equalTo(name))
                .body("job", equalTo(job));
    }

    @Given("I update user by {int} with {string} I get back user data I updated")
    public void i_update_user_by_with(Integer id, String job) {
        Map<String, Object>  json = new HashMap<>();
        json.put("job", job);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put(restApi + id)
                .then()
                .statusCode(200)
                .assertThat()
                .body("job", equalTo(job));
    }

    @Given("I delete user by {int} I get HTTP 204")
    public void i_delete_user_by(Integer id) {
        given()
                .when()
                .delete(restApi + id)
                .then()
                .statusCode(204);
    }

    //endregion
}



