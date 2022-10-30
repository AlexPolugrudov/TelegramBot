package com.polugrudov.telegrambot.parser;

import com.polugrudov.telegrambot.entity.SearchSubject;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

@Data
public class Parser {

    private static final String WEB_SITE_OTZOVIK = "https://otzovik.com";
    private static final String OTZOVIK_SEARCH_BTN = "//*[@id=\"header-search-input\"]";

    // TODO: Добавить проверку на наличие рекламы в первых 3 поисковых выдачах
    //      сейчас он выбирает 4 элемент и поэтому может выбирать не совсем корректно
    private static final String OTZOVIK_FOURTH_ELEMENT = "//*[@id=\"content\"]/div[3]/table/tbody/tr[4]/td[2]/h3/a";

    private static final String OTZOVIK_NAME_ELEMENT = "//*[@id=\"content\"]/div/div/div/div/div[1]/h1/span";

    private static final String WEB_SITE_IRECOMMEND = "https://irecommend.ru";
    private static final String IRECOMMEND_SEARCH_BTN = "//*[@id=\"search-theme-form\"]/div/div/input";
    private static final String IRECOMMEND_FIRST_ELEMENT = "//*[@id=\"content\"]/div[2]/ul/li[1]/div/div[1]/a";
    private static final String IRECOMMEND_NAME_ELEMENT = "//*[@id=\"content\"]/div[3]/div[1]/div[1]/h1";

    public SearchSubject findSearchSubjectOnOtzovik(String searchSubjectName) {

        SearchSubject searchSubject = new SearchSubject();

        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");

        WebDriver webDriver = new ChromeDriver();

        pathToTheElement(webDriver, WEB_SITE_OTZOVIK, OTZOVIK_SEARCH_BTN, OTZOVIK_FOURTH_ELEMENT, searchSubjectName);

        searchSubject.setName(findItemName(webDriver, OTZOVIK_NAME_ELEMENT));
        searchSubject.setUrl(findItemUrl(webDriver));
        searchSubject.setAverageRate(findAverageRateOnOtzovik(webDriver));
        searchSubject.setRates(findRatesOnOtzovik(webDriver));

        webDriver.quit();

        return searchSubject;
    }

    public SearchSubject findSearchSubjectOnIRecommend(String searchName) {

        SearchSubject searchSubject = new SearchSubject();

        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");

        WebDriver webDriver = new ChromeDriver();

        pathToTheElement(webDriver, WEB_SITE_IRECOMMEND, IRECOMMEND_SEARCH_BTN, IRECOMMEND_FIRST_ELEMENT, searchName);

        searchSubject.setName(findItemNameOnIRecommend(webDriver, IRECOMMEND_NAME_ELEMENT));
        searchSubject.setUrl(findItemUrl(webDriver));
        searchSubject.setAverageRate(findAverageRateOnIRecommend(webDriver));
        searchSubject.setRates(findRatesOnIRecommend());

        webDriver.quit();

        return searchSubject;
    }

    private void pathToTheElement(WebDriver webDriver ,String webSite, String button, String element, String searchName) {

        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");

        webDriver.get(webSite);

        WebElement searchButton = webDriver.findElement(By.xpath(button));

        searchButton.sendKeys(searchName, Keys.ENTER);

        webDriver.findElement(By.xpath(element)).click();
    }

    private String findItemName(WebDriver webDriver, String itemName) {
        return webDriver.findElement(By.xpath(itemName))
                .getText();
    }

    private String findItemUrl(WebDriver webDriver) {
        return webDriver.getCurrentUrl();
    }

    private String findAverageRateOnOtzovik(WebDriver webDriver) {
        return webDriver
                .findElement(By.className("rating"))
                .getAttribute("title");
    }

    private List<String> findRatesOnOtzovik(WebDriver webDriver) {
        List<String> rates = new ArrayList<>();

        webDriver.findElements(By.className("rating-item")).forEach(element -> {
            rates.add(element.getAttribute("title"));
        });

        return rates;
    }

    private String findItemNameOnIRecommend(WebDriver webDriver, String itemName) {
        return webDriver.findElement(By.xpath(itemName))
                .findElement(By.tagName("span"))
                .getText();
    }

    private String findAverageRateOnIRecommend(WebDriver webDriver) {
        return webDriver
                .findElement(By.className("description"))
                .findElement(By.tagName("span"))
                .getText();
    }

    private List<String> findRatesOnIRecommend() {
        List<String> rates = new ArrayList<>();

        rates.add("На сайте: IReccomend.ru нет подробных оценок");

        return rates;
    }
}
