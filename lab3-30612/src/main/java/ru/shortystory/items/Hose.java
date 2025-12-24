package ru.shortystory.items;

import ru.shortystory.characters.Shorty;
import ru.shortystory.enums.IncidentType;
import ru.shortystory.exceptions.ItemMalfunctionException;
import ru.shortystory.records.NewsReport;

public class Hose extends AbstractItem {

    public Hose() {
        super("Hose");
    }

    @Override
    public NewsReport use(Shorty user, Shorty target) throws ItemMalfunctionException {
        if (Math.random() < 0.1) {
            throw new ItemMalfunctionException("Шланг прохудился, вода брызжет в " + user.getName() + "!");
        }

        String desc = user.getName() + " облил водой " + target.getName() + " из шланга";
        return new NewsReport("Водная атака", desc, IncidentType.WATER_ATTACK);
    }
}