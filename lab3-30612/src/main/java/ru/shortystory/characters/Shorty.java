package ru.shortystory.characters;

import ru.shortystory.characters.parts.Face;
import ru.shortystory.enums.Lifestyle;
import ru.shortystory.environment.City;
import ru.shortystory.exceptions.ItemMalfunctionException;
import ru.shortystory.exceptions.SocialCreditBankruptcyException;
import ru.shortystory.items.AbstractItem;
import ru.shortystory.items.Clothing;
import ru.shortystory.records.NewsReport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Shorty {
    private String name;
    private Lifestyle style = Lifestyle.NORMAL;
    private Face face = new Face();
    private ArrayList<AbstractItem> items = new ArrayList<>();
    private ArrayList<Clothing> clothes = new ArrayList<>();
    private double socialCredit = 105.0;

    public Shorty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLifestyle(Lifestyle style) {
        this.style = style;
    }

    public Lifestyle getLifestyle() {
        return style;
    }

    public Face getFace() {
        return face;
    }

    public void setItems(AbstractItem... item) {
        items = new ArrayList<>(Arrays.asList(item));
    }

    public void addItem(AbstractItem item) {
        items.add(item);
    }

    public void removeItem(AbstractItem item) {
        items.remove(item);
    }

    public ArrayList<AbstractItem> getItems() {
        return new ArrayList<>(items);
    }

    public void setClothes(Clothing... clothing) {
        clothes = new ArrayList<>(Arrays.asList(clothing));
    }

    public void addClothing(Clothing clothing) {
        clothes.add(clothing);
    }

    public void removeClothing(Clothing clothing) {
        clothes.remove(clothing);
    }

    public ArrayList<Clothing> getClothes() {
        return new ArrayList<>(clothes);
    }

    public double getSocialCredit() {
        return socialCredit;
    }

    public void performAction(City city) {
        checkBankruptcy();

        if (style == Lifestyle.DONKEY_FOLLOWER) {
            handleDonkeyBehavior(city);
        } else {
            System.out.println(name + " занимается полезным делом.");
            socialCredit += 1.0;
        }
    }

    private void handleDonkeyBehavior(City city) {
        double choice = Math.random();

        if (choice < 0.3) {
            groomFace();
        } else if (choice < 0.6) {
            wanderAndSpit();
        } else {
            makeTrouble(city);
        }
    }

    public void groomFace() {
        System.out.println(name + " любуется собой.");
        face.makeFancyGrimace();
        socialCredit -= 0.5;
    }

    public void wanderAndSpit() {
        System.out.println(name + " бесцельно шатается по улицам, никому не уступая дороги и поминутно плюясь.");
        socialCredit -= 5.0;
    }

    public void makeTrouble(City city) {
        if (items.isEmpty()) {
            System.out.println(name + " хотел нахулиганить, но у него нет предметов!");
            return;
        }

        Shorty victim = city.getRandomCitizen();
        if (victim == null || victim.equals(this)) {
            System.out.println(name + " не нашел подходящей жертвы.");
            return;
        }

        Random random = new Random();
        AbstractItem item = items.get(random.nextInt(items.size()));

        try {
            NewsReport report = item.use(this, victim);
            city.broadcastNews(report);
            socialCredit -= 20.0;
        } catch (ItemMalfunctionException e) {
            System.out.println("Провал пранка: " + e.getMessage());
            socialCredit -= 10.0;
        }
    }

    public void receivePrank(String description) {
        System.out.println(name + " пострадал: " + description);
        socialCredit += 2.0;
    }

    private void checkBankruptcy() {
        if (socialCredit < 0) {
            throw new SocialCreditBankruptcyException("Коротышка " + name + " стал ослом!");
        }
    }

    @Override
    public String toString() {
        return "Shorty{name='" + name + "', style=" + style + ", credit=" + socialCredit + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, style, face, items, clothes, socialCredit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shorty shorty = (Shorty) o;
        return Double.compare(shorty.socialCredit, socialCredit) == 0 &&
                Objects.equals(name, shorty.name) &&
                style == shorty.style &&
                Objects.equals(face, shorty.face) &&
                Objects.equals(items, shorty.items) &&
                Objects.equals(clothes, shorty.clothes);
    }
}