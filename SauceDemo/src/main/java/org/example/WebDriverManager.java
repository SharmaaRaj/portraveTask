package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;

import java.time.Duration;

public class WebDriverManager {

    public WebDriver returnDriverInfo(){

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito"); //used this as temporary solution to skip password breach popup
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }
}
