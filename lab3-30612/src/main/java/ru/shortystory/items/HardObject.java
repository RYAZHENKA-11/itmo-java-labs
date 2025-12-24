package ru.shortystory.items;

import ru.shortystory.characters.Shorty;
import ru.shortystory.enums.IncidentType;
import ru.shortystory.exceptions.ItemMalfunctionException;
import ru.shortystory.records.NewsReport;

public class HardObject extends AbstractItem {

    public HardObject(String name) {
        super(name);
    }

    @Override
    public NewsReport use(Shorty user, Shorty target) throws ItemMalfunctionException {
        if (Math.random() < 0.1) {
            throw new ItemMalfunctionException(getName() + " слишком тяжелый, не долетел!");
        }

        String desc = user.getName() + " бросил из окна " + getName() + " в " + target.getName();
        return new NewsReport("Опасные предметы", desc, IncidentType.HARD_OBJECT);
    }
}