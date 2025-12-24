package ru.shortystory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.shortystory.characters.parts.Lip;
import ru.shortystory.characters.parts.Nose;
import ru.shortystory.items.Clothing;

import java.util.HashSet;
import java.util.Set;

public class EqualsHashCodeTest {

    @Test
    public void testClothingEquality() {
        Clothing shirt1 = new Clothing("Рубашка", true);
        Clothing shirt2 = new Clothing("Рубашка", true);
        Clothing shirt3 = new Clothing("Рубашка", false); // Другая мода

        Assertions.assertEquals(shirt1, shirt2, "Одинаковая одежда должна быть равна");
        Assertions.assertNotEquals(shirt1, shirt3, "Разная одежда не должна быть равна");
        Assertions.assertEquals(shirt1.hashCode(), shirt2.hashCode(), "Хэш-коды равных объектов должны совпадать");
    }

    @Test
    public void testBodyPartsInSet() {
        Set<Nose> noses = new HashSet<>();
        Nose nose1 = new Nose(10.0);
        Nose nose2 = new Nose(10.0);

        noses.add(nose1);
        noses.add(nose2);

        Assertions.assertEquals(1, noses.size(), "HashSet должен содержать только один уникальный нос");
    }

    @Test
    public void testLipEquality() {
        Lip lip1 = new Lip(5.0);
        Lip lip2 = new Lip(5.0);

        lip1.stretch(2.0);

        Assertions.assertNotEquals(lip1, lip2, "После изменения состояния объекты не должны быть равны");
    }
}