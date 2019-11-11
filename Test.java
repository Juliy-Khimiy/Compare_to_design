import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {
    private static WebDriver driver;

    //private static test.CompareEyes eyes;
    private static CompareEyes eyes;
    private static String appName = "My Application Name";
    private static String testName = "My Test Name";
    private static RectangleSize viewPortSize = new RectangleSize(320,600); //разришение хочу брать с файла
    private static String htmlFileLocation = "C:\\Users\\ЮлиЯ\\Downloads\\mockup.html";
    private static ArrayList<String[]> dataBase = new ArrayList<>();

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "c:\\Program Files (x86)\\webdriver\\chromedriver.exe"); //какой драйвер использовать тоже хочу с файла
        try {
            ChromeOptions options = new ChromeOptions();
            //options.setBinary("C:\\Users\\admin\\AppData\\Local\\Google\\Chrome SxS\\Application\\chrome.exe");
            //options.addArguments("window-size=320x600");
            options.addArguments("--headless");
            driver = new ChromeDriver(options);
            //driver = new ChromeDriver();
            //driver.manage().window().setSize(new Dimension(1366, 625));
            initEyes();
            eyes.setEnableComparison(true);
            initDataBase();
            eyes.open(driver, appName, testName, viewPortSize);
            uploadMockups();
            eyes.switchToComparisonMode(driver);
            runTest();
            eyes.close();
            driver.quit();
        } catch (UnexpectedException e) {
            e.printStackTrace();
        }finally {
            driver.quit();
            eyes.abortIfNotClosed();
        }
    }
    public static void initEyes(){
        // Create CompareEyes instance
        //eyes = new test.CompareEyes();
        eyes = new CompareEyes();
        // Set API key
        //eyes.setApiKey(System.getenv("        eyes = new test.CompareEyes();\n"));
        eyes.setApiKey("VpEXwHtKENoxwxc7njHv2AJVzJp4Lqt8uGsr103Etp4Ss110");
        // Set batch
        eyes.setBatch(new BatchInfo("My Batch"));
        // Set match level to Layout
        eyes.setMatchLevel(MatchLevel.LAYOUT);
        // Enable full page screenshot with CSS stitching
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
    }

    public static void initDataBase(){
        // Creating a database of URL<->Mockup pairs.
        //dataBase.add(new String[]{"http://www.applitools.com", "/Users/john/Desktop/Applitools.png"});
        //dataBase.add(new String[]{"https://help.applitools.com", "/Users/john/Desktop/Sup    port.png"});
        dataBase.add(new String[]{"http://107.170.55.161/secureakey/", " c:/Users/ЮлиЯ/Desktop/320-600.png"}); //сайт и картинку тоже хо с файла
    }

    public static void uploadMockups(){

        String width;
        String getWidthCommand = "function getWidth() { var scrollWidth = document.documentElement.scrollWidth; var bodyScrollWidth = document.body.scrollWidth; return Math.max(scrollWidth, bodyScrollWidth);}; return getWidth();";
        String hideScrollbarCommand = "document.body.style.overflow = 'hidden';";

        // Taking screenshots of the mockups
        for (String entry[]:dataBase) {
            driver.get(entry[0]);
            eyes.setHideScrollbars(true);
            ((JavascriptExecutor) driver).executeScript(hideScrollbarCommand);
            width = ((JavascriptExecutor) driver).executeScript(getWidthCommand).toString();
            driver.get(htmlFileLocation);
            ((JavascriptExecutor) driver).executeScript("addImage('" + entry[1] + "','" + width + "');");
            eyes.check(entry[0], Target.region(By.cssSelector("img")).timeout(10));
        }
    }

    public static void runTest(){

        // Taking screenshots of the URLs
        for (String entry[]:dataBase) {
            driver.get(entry[0]);
            eyes.check(entry[0], Target.window());
        }
    }

}
