package com.geekbrains.tests

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {

    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()

    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName

    @Before
    fun setup() {
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()

        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        //Мы уже проверяли Интент на null в предыдущем тесте, поэтому допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)//Чистим бэкстек от запущенных ранее Активити
        context.startActivity(intent)

        //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    //Убеждаемся, что приложение открыто. Для этого достаточно найти на экране любой элемент
    //и проверить его на null
    @Test
    fun test_MainActivityIsStarted() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Проверяем на null
        Assert.assertNotNull(editText)
    }

    //Убеждаемся, что поиск работает как ожидается
    @Test
    fun test_SearchIsPositive() {
        uiDevice.findObject(By.res(packageName, "searchEditText"))
            .text = "UiAutomator"

        uiDevice.findObject(By.res(packageName, "searchButton"))
            .click()

        val totalCount =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountSearchTextView")),
                TIMEOUT
            )
        Assert.assertEquals(totalCount.text.toString(), "Number of results: 708")
    }

    //Убеждаемся, что DetailsScreen открывается
    @Test
    fun test_OpenDetailsScreen() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(
                packageName,
                "toDetailsActivityButton"
            )
        )
        //Кликаем по ней
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val totalCount =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            )
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        //Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        //Чтобы проверить отображение определенного количества репозиториев,
        //вам в одном и том же методе нужно отправить запрос на сервер и открыть DetailsScreen.
        Assert.assertEquals(totalCount.text, "Number of results: 0")
    }

    @Test
    fun test_SearchAndMatchDetailScreenTotalCount() {

        uiDevice.findObject(By.res(packageName, "searchEditText"))
            .text = "UiAutomator"

        uiDevice.findObject(By.res(packageName, "searchButton"))
            .click()

        val totalCountSearchScreen =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountSearchTextView")),
                TIMEOUT
            ).text.toString()

        uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
            .click()

        val totalCountDetailsScreen =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            ).text.toString()

        Assert.assertEquals(totalCountSearchScreen, totalCountDetailsScreen)
    }

    @Test
    fun test_DetailsScreenOnDecrement() {
        uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
            .click()
        uiDevice.findObject(UiSelector().text("-"))
            .click()

        val totalCountDetailsScreen =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            )

        Assert.assertEquals(totalCountDetailsScreen.text.toString(), "Number of results: -1")
    }

    @Test
    fun test_DetailsScreenOnIncrement() {
        uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
            .click()
        uiDevice.findObject(UiSelector().text("+"))
            .click()

        val totalCountDetailsScreen =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            )

        Assert.assertEquals(totalCountDetailsScreen.text.toString(), "Number of results: 1")
    }

    companion object {
        private const val TIMEOUT = 5000L
    }
}
