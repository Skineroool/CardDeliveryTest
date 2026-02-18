package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

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
        // Город
        $("[data-test-id='city'] input").setValue("Казань");

        // Дата (очистка и ввод)
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        String deliveryDate = generateDate(3);
        $("[data-test-id='date'] input").setValue(deliveryDate);

        // Имя
        $("[data-test-id='name'] input").setValue("Иванов Иван");

        // Телефон
        $("[data-test-id='phone'] input").setValue("+79000000000");

        // Чекбокс согласия
        $("[data-test-id='agreement']").click();

        // Кнопка "Забронировать"
        $(byText("Забронировать")).click();

        // Явное ожидание уведомления (видимость)
        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15));

        // Проверка текста
        String expectedText = "Встреча успешно забронирована на " + deliveryDate;
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(expectedText));
    }
}