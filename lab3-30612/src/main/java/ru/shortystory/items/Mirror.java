package ru.shortystory.items;

import ru.shortystory.characters.Shorty;
import ru.shortystory.enums.IncidentType;
import ru.shortystory.exceptions.ItemMalfunctionException;
import ru.shortystory.records.NewsReport;

public class Mirror extends AbstractItem {

    public Mirror() {
        super("Mirror");
    }

    @Override
    public NewsReport use(Shorty user, Shorty target) throws ItemMalfunctionException {
        if (Math.random() < 0.1) {
            throw new ItemMalfunctionException(user.getName() + "разбил зеркало!");
        }

        String desc = target.getName() + " часами торчал перед зеркалом";
        return new NewsReport("Модный приговор", desc, IncidentType.FASHION_VICTIM);
    }
}