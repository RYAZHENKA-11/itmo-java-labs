package ru.shortystory.items;

import ru.shortystory.characters.Shorty;
import ru.shortystory.enums.IncidentType;
import ru.shortystory.exceptions.ItemMalfunctionException;
import ru.shortystory.records.NewsReport;

public class Rope extends AbstractItem {

    public Rope() {
        super("Rope");
    }

    @Override
    public NewsReport use(Shorty user, Shorty target) throws ItemMalfunctionException {
        if (Math.random() < 0.1) {
            throw new ItemMalfunctionException("Веревка запуталась!");
        }

        String desc = user.getName() + " натянул веревку, и " + target.getName() + " споткнулся";
        return new NewsReport("Падение дня", desc, IncidentType.TRIPPING);
    }
}