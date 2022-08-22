package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class HWWebTest {
    @ValueSource(strings = {"Hello", "World"})
    @ParameterizedTest(name = "Результаты перевода не пустые для слова {0}")
    void commonSearchTest(String testData){
        open("https://translate.yandex.ru/");
        $("#fakeArea").setValue(testData);
        $$("#translation").shouldBe(CollectionCondition.sizeGreaterThan(0));


    }

    @CsvSource(value = {
            "Hello, Здравствуйте",
            "World, Мир"
    })
    @ParameterizedTest(name = "Результаты перевода - \"{1}\" для слова: \"{0}\"")
    void commonComplexSearchTest(String testData, String expectedResult){
        open("https://translate.yandex.ru/");
        $("#fakeArea").setValue(testData);
        $$("#translation")
                .first()
                .shouldHave(text(expectedResult));
    }


    static Stream<Arguments> dataForTranslation() {
        return Stream.of(
                Arguments.of("Hello", List.of("привет м", "м", "Здравствуй м", "м")),
                Arguments.of("Falcon", List.of("Сокол м", "м", "кречет м", "м", "фальконет м", "м"))
        );
    }
    @MethodSource("dataForTranslation")
    @ParameterizedTest(name = "Для слова {0} отображаются варианты перевода {1}")
    void selenideSiteMenuTest(String word, List<String> translationVariants) {
        open("https://translate.yandex.ru/");
        $("#fakeArea").setValue(word);
        $$(".dict-meanings-value span")
                .filter(text("м"))
                .shouldHave(CollectionCondition.texts(translationVariants));
    }

}
