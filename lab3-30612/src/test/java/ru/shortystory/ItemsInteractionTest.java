package ru.shortystory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.shortystory.characters.Shorty;
import ru.shortystory.enums.IncidentType;
import ru.shortystory.exceptions.ItemMalfunctionException;
import ru.shortystory.items.Hose;
import ru.shortystory.items.Rope;
import ru.shortystory.records.NewsReport;

public class ItemsInteractionTest {

    @Test
    public void testHoseUsageReport() {
        Shorty attacker = new Shorty("Хулиган");
        Shorty victim = new Shorty("Прохожий");
        Hose hose = new Hose();

        boolean success = false;
        for (int i = 0; i < 20; i++) {
            try {
                NewsReport report = hose.use(attacker, victim);
                Assertions.assertEquals(IncidentType.WATER_ATTACK, report.type());
                Assertions.assertTrue(report.description().contains("облил водой"));
                success = true;
                break;
            } catch (ItemMalfunctionException ignored) {
            }
        }
        Assertions.assertTrue(success, "Предмет должен был успешно сработать хотя бы один раз");
    }

    @Test
    public void testRopeUsageExceptionMessage() {
        Shorty user = new Shorty("Тестер");
        Rope rope = new Rope();

        Assertions.assertDoesNotThrow(() -> {
            try {
                rope.use(user, user);
            } catch (ItemMalfunctionException e) {
                Assertions.assertTrue(e.getMessage().contains("Ошибка предмета"));
            }
        });
    }
}