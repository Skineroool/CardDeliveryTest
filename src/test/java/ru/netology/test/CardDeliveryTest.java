package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        Configuration.headless = true;
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    private String generateDate(int daysToAdd) {
        LocalDate futureDate = LocalDate.now().plusDays(daysToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return futureDate.format(formatter);
    }

    @Test
    void shouldSuccessfullyReserveCard() {
        $("[data-test-id='city'] input").setValue("Казань");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        String deliveryDate = generateDate(3);
        $("[data-test-id='date'] input").setValue(deliveryDate);

        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();

        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));

        String expectedText = "Встреча успешно забронирована на " + deliveryDate;
        $("[data-test-id='notification'] .notification__content")
                .shouldHave(text(expectedText));
    }
}