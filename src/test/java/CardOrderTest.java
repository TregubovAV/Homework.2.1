import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderTest {
    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @Test
    public void shouldSubmitFormSuccessfully() {
        try {
            System.out.println("Запуск Chrome в headless-режиме...");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");

            WebDriver driver = new ChromeDriver(options);
            System.out.println("ChromeDriver запущен!");

            driver.get("http://localhost:9999");
            System.out.println("Страница загружена!");

            // Заполняем поле "Фамилия и имя"
            WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));
            nameInput.sendKeys("Иванов Иван");

            // Заполняем поле "Мобильный телефон"
            WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id=phone] input"));
            phoneInput.sendKeys("+79991112233");

            // Ставим галочку согласия
            WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id=agreement]"));
            checkbox.click();

            // Нажимаем кнопку "Продолжить"
            WebElement submitButton = driver.findElement(By.cssSelector("button.button"));
            submitButton.click();

            // Ожидаем появление сообщения
            WebElement successMessage = driver.findElement(By.cssSelector("[data-test-id=order-success]"));
            assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", successMessage.getText());

            System.out.println("Тест успешно завершён!");
            driver.quit();

        } catch (Exception e) {
            System.err.println("Ошибка во время теста: " + e.getMessage());
            e.printStackTrace();
        }
    }
}