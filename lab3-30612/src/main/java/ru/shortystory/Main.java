package ru.shortystory;

import ru.shortystory.characters.Shorty;
import ru.shortystory.enums.Lifestyle;
import ru.shortystory.environment.City;
import ru.shortystory.environment.Newspaper;
import ru.shortystory.items.Clothing;
import ru.shortystory.items.HardObject;
import ru.shortystory.items.Hose;
import ru.shortystory.items.Mirror;
import ru.shortystory.items.Rope;

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> Запуск симуляции <<<\n");

        City flowerTown = new City("Цветочный");

        Newspaper fashionWeek = new Newspaper("Неделя Моды");
        Newspaper crimeDaily = new Newspaper("Криминальная Хроника");
        flowerTown.addNewspaper(fashionWeek);
        flowerTown.addNewspaper(crimeDaily);

        Shorty dunnow = new Shorty("Незнайка");
        dunnow.setLifestyle(Lifestyle.DONKEY_FOLLOWER);
        dunnow.addItem(new Hose());
        dunnow.addItem(new Rope());
        dunnow.addClothing(new Clothing("Модные брюки", true));

        Shorty kozlik = new Shorty("Козлик");
        kozlik.setLifestyle(Lifestyle.DONKEY_FOLLOWER);
        kozlik.addItem(new Mirror());
        kozlik.addItem(new HardObject("Стопка ПСЖ"));
        kozlik.addClothing(new Clothing("Модный пиджак", true));

        Shorty fixit = new Shorty("Винтик");
        fixit.setLifestyle(Lifestyle.NORMAL);
        fixit.addClothing(new Clothing("Рабочий комбинезон", false));

        flowerTown.setCitizens(dunnow, kozlik, fixit);

        for (int day = 1; day <= 3; day++) {
            System.out.printf("\n=== ДЕНЬ %d ===%n", day);
            flowerTown.liveDay();

            System.out.println("\n[Новости в конце дня]:");
            fashionWeek.printIssue();
            crimeDaily.printIssue();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}