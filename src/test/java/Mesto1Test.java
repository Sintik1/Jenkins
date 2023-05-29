//Jenkins и Allure
       // Ты уже умеешь собирать проект в Jenkins. К нему можно подключить Allure, чтобы получать отчёты и собирать историю тестов.
      //  Для примера возьми такой код с тестами для API сервиса Mesto:
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Mesto1Test {

    String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2NDJiMmRiMjI0OGQ0NTAwMzYyN2Y5ZWQiLCJpYXQiOjE2ODUzNjIxMDEsImV4cCI6MTY4NTk2NjkwMX0.OhI516K955J3EnqhzR9aJ1wzbyp3WGS1o2zIh2smvPA";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }

    @Test
    @DisplayName("Add a new photo")
    @Description("This test is for adding a new photo to Mesto.")
    public void addNewPhoto() {
        given()
                .header("Content-type", "application/json") // Передаём Content-type в заголовке для указания типа файла
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .body("{\"name\":\"Москва\",\"link\":\"https://code.s3.yandex.net/qa-automation-engineer/java/files/paid-track/sprint1/photoSelenium.jpg\"}") // Формируем тело запроса
                .post("/api/cards") // Делаем POST-запрос
                .then().statusCode(201); // Проверяем код ответа
    }

    @Test
    @DisplayName("Like the first photo")
    @Description("This test is for liking the first photo on Mesto.")
    public void likeTheFirstPhoto() {
        String photoId = getTheFirstPhotoId();

        likePhotoById(photoId);
        deleteLikePhotoById(photoId);
    }

    @Step("Take the first photo from the list")
    private String getTheFirstPhotoId() {
        // Получение списка фотографий и выбор первой из него
        return given()
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .get("/api/cards") // Делаем GET-запрос
                .then().extract().body().path("data[0]._id"); // Получаем ID фотографии из массива данных
    }

    @Step("Like a photo by id")
    private void likePhotoById(String photoId) {
        // Лайк фотографии по photoId
        given()
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .put("/api/cards/{photoId}/likes", photoId) // Делаем PUT-запрос
                .then().assertThat().statusCode(200); // Проверяем, что сервер вернул код 200
    }

    @Step("Delete like from the photo by id")
    private void deleteLikePhotoById(String photoId) {
        // Снять лайк с фотографии по photoId
        given()
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .delete("/api/cards/{photoId}/likes", photoId) // Делаем DELETE-запрос
                .then().assertThat().statusCode(200); // Проверяем, что сервер вернул код 200
    }

}
   // В тестовых методах есть аннотации Allure, которые тебе уже знакомы:
//@DisplayName — читаемое имя метода.
//@Description — описание теста.
//@Step — описание шагов теста.
      //  Добавь этот код на GitHub. Не забудь про pom.xml.
