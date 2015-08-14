package com.exadel.rts.webtest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;

public class SmokeTest {
    public static final String url = "http://v34.exadel.by";

    private WebDriver driver;


    @Before
    public void setUp() {
        // Create a new instance of the html unit driver
        driver = new HtmlUnitDriver();

        //Navigate to desired web page
        driver.get(url);
    }

    @Test
    public void signinTest() {
        // verify title of initial page
        verifyTitle("Sign in");

        // case 1 with empty Login and Password
        enterFieldByName("Login", "");
        enterFieldByName("Password", "");
        clickSignIn();
        verifyTitle("Sign in");

        enterFieldByName("Login", "opetrovich");
        enterFieldByName("Password", "111");
        clickSignIn();
        verifyTitle("Sign in");
        verifyError("Invalid operation exception. Please contact to Administrator.");

        enterFieldByName("Login", "opetovich");
        enterFieldByName("Password", "11Piter");
        clickSignIn();
        verifyTitle("Sign in");
        verifyError("Invalid operation exception. Please contact to Administrator.");

        enterFieldByName("Login", "opetrovich");
        enterFieldByName("Password", "11Piter");
        clickSignIn();
        verifyTitle("Start");
    }

    @Test
    public void startPageTest() {
        enterFieldByName("Login", "opetrovich");
        enterFieldByName("Password", "11Piter");
        clickSignIn();
        verifyTitle("Start");

        List list = driver.findElements(By.tagName("a"));
        for (Object o : list) {
            WebElement e = (WebElement)o;
            if (e.getText().equals("View Details")) {
                String href = e.getAttribute("href");
                String nav = url + href;
                driver.get(nav);

                if (href.equals("/assets")) {
                    verifyTitle("Manage Assets");
                }
            }
        }
    }

    private void verifyTitle(String expectedTitle) {
        String actualTitle = driver.getTitle();
        Assert.assertEquals(expectedTitle, actualTitle);
    }

    private void enterFieldByName(String fieldName, String str) {
        WebElement element = driver.findElement(By.name(fieldName));
        element.clear();
        element.sendKeys(str);
    }

    private void clickSignIn() {
        driver.findElement(By.xpath("//*[@type='submit']")).click();
    }

    private void verifyError(String expectedMsg) {
        WebElement element = driver.findElement(By.xpath("//*[@data-valmsg-for='Error']"));
        String actualMsg = element.getText();
        Assert.assertEquals(expectedMsg, actualMsg);
    }
}
