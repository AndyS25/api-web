package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import static org.junit.jupiter.api.Assertions.*;

public class DebitCardApplicationTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless"); //отключать этот параметр при отправке в CI
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    //Позитивные тесты
    @Test
    //ввод валидных данных
    void shouldPositiveDataSendFormDebitCard() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Макаров Иван"); //ввели в первое поле ввода
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233"); //ввели во второе поле ввода
        driver.findElement(By.cssSelector("[data-test-id ='agreement']")).click(); //кликнули по чек-боксу
        driver.findElement(By.className("button")).click(); //кликнули по кнопке

        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));

        assertTrue(result.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                result.getText().trim());
    }

    //Негативные тесты
    @Test
    //Латиница в поле имени
    void shouldLatinicaNameFormDebitCard() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Makarov Ivan");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id ='agreement']")).click();
        driver.findElement(By.className("button")).click();

        //WebElement result = driver.findElement(By.cssSelector(".input_invalid[data-test-id='name'] span.input__sub"));
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));

        assertTrue(result.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                result.getText().trim());
    }

    @Test
        //Пустое поле имени
    void shouldEmptyNameFormDebitCard() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id ='agreement']")).click();
        driver.findElement(By.className("button")).click();

        //WebElement result = driver.findElement(By.cssSelector(".input_invalid[data-test-id='name'] span.input__sub"));
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));

        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения",
                result.getText().trim());
    }

    @Test
        //Пустое поле номера телефона
    void shouldEmptyPhoneNumberFormDebitCard() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Макаров Иван");
        driver.findElement(By.cssSelector("[data-test-id ='agreement']")).click();
        driver.findElement(By.className("button")).click();

        //WebElement result = driver.findElement(By.cssSelector(".input_invalid[data-test-id='phone'] span.input__sub"));
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));

        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения",
                result.getText().trim());
    }

    @Test
        //Знак + не первый в поле номера телефона
    void shouldNotFirstPlusInPhoneNumberFormDebitCard() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Макаров Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("7+9991112233");
        driver.findElement(By.cssSelector("[data-test-id ='agreement']")).click();
        driver.findElement(By.className("button")).click();

        //WebElement result = driver.findElement(By.cssSelector(".input_invalid[data-test-id='phone'] span.input__sub"));
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));

        assertTrue(result.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                result.getText().trim());
    }

    @Test
        //Не отмечен чек-бокс согласия об обработке персональных данных
    void shouldCheckBoxNotCheckedFormDebitCard() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Макаров Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        //driver.findElement(By.cssSelector("[data-test-id ='agreement']")).click();
        driver.findElement(By.className("button")).click();

        //WebElement result = driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__text"));
        WebElement result = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text"));

        assertTrue(result.isDisplayed());
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных " +
                        "и разрешаю сделать запрос в бюро кредитных историй",
                result.getText().trim());
    }
}
