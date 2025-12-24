package ru.shortystory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.shortystory.environment.City;
import ru.shortystory.environment.Newspaper;
import ru.shortystory.records.NewsReport;
import ru.shortystory.enums.IncidentType;

public class CityTest {

    @Test
    public void testBroadcastNews() {
        City city = new City("Тестоград");
        Newspaper paper = new Newspaper("Вестник");
        city.addNewspaper(paper);

        NewsReport news = new NewsReport("Заголовок", "Описание", IncidentType.FASHION_VICTIM);
        city.broadcastNews(news);

        Assertions.assertEquals(1, paper.getHeadlines().size(), "Газета должна получить новость");
        Assertions.assertEquals(news, paper.getHeadlines().get(0));
    }

    @Test
    public void testAddRemoveCitizens() {
        City city = new City("Пустой город");
        Assertions.assertEquals(0, city.getCitizens().size());

        Assertions.assertNull(city.getRandomCitizen());
    }
}