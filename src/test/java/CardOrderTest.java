import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().clearDriverCache();
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-dev-shm-usage", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void shouldSubmitFormSuccessfully() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement successMessage = driver.findElement(By.cssSelector("[data-test-id=order-success]"));
        String actualText = successMessage.getText();
        assertTrue(actualText.contains("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время"),
               "Фактический текст: " + actualText);
    }

    @Test
    public void shouldShowErrorForInvalidName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivan Petrov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement nameError = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", nameError.getText());
    }

    @Test
    public void shouldShowErrorForInvalidPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("99911122333");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement phoneError = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", phoneError.getText());
    }

    @Test
    public void shouldShowErrorForUncheckedAgreement() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement agreementError = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"));
        assertTrue(agreementError.isDisplayed());
    }

    @Test
    public void shouldShowOnlyFirstErrorWhenMultipleFieldsInvalid() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivan Petrov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("99911122333");
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement nameError = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", nameError.getText());

        boolean phoneInvalid = driver.findElements(By.cssSelector("[data-test-id=phone].input_invalid")).size() > 0;
        boolean agreementInvalid = driver.findElements(By.cssSelector("[data-test-id=agreement].input_invalid")).size() > 0;

        assertFalse(phoneInvalid);
        assertFalse(agreementInvalid);
    }

    @Test
    public void shouldShowErrorForEmptyNameField() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement nameError = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        assertEquals("Поле обязательно для заполнения", nameError.getText());
    }

    @Test
    public void shouldShowErrorForEmptyPhoneField() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement phoneError = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        assertEquals("Поле обязательно для заполнения", phoneError.getText());
    }
}