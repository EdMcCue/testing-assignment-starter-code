package uk.org.adacollege.apprenticeship.testing;

import static org.junit.Assert.assertTrue;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;

public class SolutionIT {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String startUrl;
    private static String myWhipbirdsMenuId = "my-whipbirds-menu";
    private static String aboutMenuId = "about-menu";
    private static String logOutMenuId = "log-out-menu";
    private static String logInMenuId = "log-in-menu";
    private static String emailInputId = "email";
    private static String passwordInputId = "password";
    private static String validEmail = "edward.mccue@adacollege.org.uk";
    private static String invalidEmail = validEmail + ".nothing";
    private static String validPassword = "whipit";
    private static String invalidPassword = validPassword + "-invalid";
    private static String logInButtonId = "login-button";
    private static String logOutButtonId = "log-out-button";
    private static String popupMessageId = "popup-message";
    private static String deletewhipbirdbutton = "delete-whipbird-button-0";
    private static int size;
    private static String enterbirdname = "name";
    private static String enterbirdage = "age";
    private static String newbird = "Karl";
    private static String newage = "199";
    private static String AddBirdButton = "add-new-whipbird-button";

    // ========= UTILITY METHODS =========

    /**
     * Source & usage: https://stackoverflow.com/a/5709805
     */
    private static Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
        return new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        };
    }

    private static void CountBirds(){
        size = driver.findElements(By.id("delete-whipbird-button-0")).size();
    }

    private static void DeleteBirds(){
        CountBirds();
        while (size > 0){
            DeleteFirstBird();
        CountBirds();
        }

    }

    private static void MakeBird(){
        wait.until(presenceOfElementLocated(By.id(enterbirdname)));
        driver.findElement(By.id(enterbirdname)).sendKeys(newbird);

        wait.until(presenceOfElementLocated(By.id(enterbirdage)));
        driver.findElement(By.id(enterbirdage)).sendKeys(newage);

        wait.until(presenceOfElementLocated(By.id(AddBirdButton)));
        driver.findElement(By.id(AddBirdButton)).click();

    }

    private static void DeleteFirstBird() {
        wait.until(presenceOfElementLocated(By.id(deletewhipbirdbutton)));
        driver.findElement(By.id(deletewhipbirdbutton)).click();
    }

    private static void logIn(Boolean withValidCredentials) {
        String email = withValidCredentials ? validEmail : invalidEmail;
        String password = withValidCredentials ? validPassword : invalidPassword;

        wait.until(presenceOfElementLocated(By.id(logInMenuId)));
        driver.findElement(By.id(logInMenuId)).click();

        wait.until(presenceOfElementLocated(By.id(emailInputId)));
        driver.findElement(By.id(emailInputId)).sendKeys(email);

        wait.until(presenceOfElementLocated(By.id(passwordInputId)));
        driver.findElement(By.id(passwordInputId)).sendKeys(password);

        wait.until(presenceOfElementLocated(By.id(logInButtonId)));
        driver.findElement(By.id(logInButtonId)).click();

        if (withValidCredentials) {
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return driver.getTitle().equals("whipbird: my whipbirds");
                }
            });
        }
    }

    private static void logOut() {
        Boolean isLoggedIn = (driver.findElements(By.id(logOutMenuId)).size() > 0);

        if (isLoggedIn) {
            wait.until(presenceOfElementLocated(By.id(logOutMenuId)));
            driver.findElement(By.id(logOutMenuId)).click();

            wait.until(presenceOfElementLocated(By.id(logOutButtonId)));
            driver.findElement(By.id(logOutButtonId)).click();
        }
    }

    private static void assertElementPresent(String elementId) {
        wait.until(presenceOfElementLocated(By.id(elementId)));
        assertTrue(driver.findElements(By.id(elementId)).size() == 1);
    }

    private static void assertElementNotPresent(String elementId) {
        assertTrue(driver.findElements(By.id(elementId)).size() == 0);
    }

    private static void assertTitleEquals(String expectedTitle) {
        Boolean result = wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.getTitle().equals(expectedTitle);
            }
        });
        assertTrue(result);
    }

    private static void assertUrlEquals(final String expectedUrl) {
        // TODO: implement this method
        // - use assertTitleEquals() as an example pattern to follow
        // - search the web for how to find the current URL with Selenium
        Boolean result = wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.getCurrentUrl().equals(expectedUrl);
            }
        });
        assertTrue(result);
    }

    private static void assertElementTextEquals(By selector, final String expectedText) {
        // TODO: implement this method
        // - use assertTitleEquals() as an example pattern to follow
        // - but instead of return driver.getTitle().equals(expectedTitle)
        //   call driver.findElement() with the selector provided
        //   and then get the text from that element
        //   and then check that it equals the expected text

        Boolean result = wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.findElement(selector).getText().equals(expectedText);
            }
        });
        assertTrue(result);
    }

    // ========= SCAFFOLDING =========

    @BeforeClass
    public static void beforeAll() {
        startUrl = "http://whipbird.mattcalthrop.com/";
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
    }

    @AfterClass
    public static void afterAll() {
        driver.close();
        driver.quit();
    }

    @Before
    public void beforeEach() {
        driver.get(startUrl);
    }

    @After
    public void afterEach() {
        logOut();
    }

    // ========= TESTS =========

    // --------- WHEN NOT LOGGED IN ---------

    // Step 1
    @Test
    public void notLoggedIn_checkMenus() {
        //All required menus should be visible.
        assertElementPresent(logInMenuId);
        assertElementPresent(aboutMenuId);
        //Other menus should not be visible
        assertElementNotPresent(logOutMenuId);
        assertElementNotPresent(myWhipbirdsMenuId);
    }

    // Step 2
    @Test
    public void notLoggedIn_checkCurrentPage() {
        //Page title should be set correctly
        assertTitleEquals("whipbird: log in");
        //URL should be set correctly
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/login");
        //Page heading (text at top left of page, below navigation bar) should be set correctly.
        assertElementTextEquals(By.tagName("h4"), "Log in");
        //Page footer (right) should be empty
        assertElementTextEquals(By.id("footer-right"), "");
    }


    // Step 3
    @Test
    public void notLoggedIn_clickAboutMenu() {
        wait.until(presenceOfElementLocated(By.id(aboutMenuId)));
        driver.findElement(By.id(aboutMenuId)).click();
        //URL should be set correctly.
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/about");
        //Page title should be set correctly
        assertTitleEquals("whipbird: about");
        //Page heading should be set correctly.
        assertElementTextEquals(By.tagName("h4"), "About this app");
    }

    // Step 4
    @Test
    public void notLoggedIn_logInWithIncorrectCredentials() {
        logIn(false);
        // All required menus should be visible.
        assertElementPresent(logInMenuId);
        assertElementPresent(aboutMenuId);
        //Other menus should not be visible.
        assertElementNotPresent(logOutMenuId);
        assertElementNotPresent(myWhipbirdsMenuId);
        //URL should be set correctly.
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/login");
        //Page title should be set correctly.
        assertTitleEquals("whipbird: log in");
        //Specific error message should be displayed.
        wait.until(presenceOfElementLocated(By.id(popupMessageId)));
        assertElementTextEquals(By.id("popup-message"), "Username or password incorrect");
        //Page footer (right) should be empty.
        assertElementTextEquals(By.id("footer-right"), "");
    }

    // --------- WHEN LOGGED IN ---------

    // Step 5
    @Test
    public void loggedIn_checkMenus() {
        logIn(true);

        //All required menus should be visible.
        assertElementPresent(logOutMenuId);
        assertElementPresent(aboutMenuId);
        assertElementPresent(myWhipbirdsMenuId);
        //Other menus should not be visible
        assertElementNotPresent(logInMenuId);
    }

    // Step 6
    @Test
    public void loggedIn_checkCurrentPage() {
        // TODO
        logIn(true);
        //URL should be set correctly.
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/my-whipbirds");
        //Page title should be set correctly.
        assertTitleEquals("whipbird: my whipbirds");
        //Page heading should be set correctly.
        assertElementTextEquals(By.tagName("h4"), "Current whipbirds for Ed McCue");
        //Page footer (right) should contain user’s full name.
        assertElementTextEquals(By.id("footer-right"), "Ed McCue");
    }

    // Step 7
    @Test
    public void loggedIn_clickLogOutMenu() {
        // TODO
        logIn(true);
        driver.findElement(By.id(logOutMenuId)).click();
        //URL should be set correctly.
        assertUrlEquals("http://whipbird.mattcalthrop.com/#!/logout");
        //Page title should be set correctly.
        assertTitleEquals("whipbird: log out");
        //Page heading should be set correctly.
        assertElementTextEquals(By.tagName("h4"), "Log out");
    }


    // Step 8
    @Test
    public void loggedIn_addNewWhipbird() {
        logIn(true);
        driver.findElement(By.id(myWhipbirdsMenuId)).click();
        DeleteBirds();
        MakeBird();
        assertElementTextEquals(By.id("whipbird-name-0"), newbird);
        assertElementTextEquals(By.id("whipbird-age-0"), newage);
        wait.until(presenceOfElementLocated(By.id(popupMessageId)));
        assertElementTextEquals(By.id("popup-message"), "Whipbird added: Karl");
        //Specific feedback message should be displayed.
        //The name of the whipbird just created should exist on the page.

    }

    /*
    // Step 9
    @Test
    public void loggedIn_addNewWhipbirdThenDeleteIt() {
        // TODO
    }
    */
}

