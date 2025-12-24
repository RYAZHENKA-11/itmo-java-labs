package ru.shortystory.environment;

import ru.shortystory.characters.Shorty;
import ru.shortystory.items.AbstractItem;
import ru.shortystory.records.NewsReport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class City {
    private String name;
    private ArrayList<Shorty> citizens = new ArrayList<>();
    private ArrayList<Newspaper> newspapers = new ArrayList<>();
    private ArrayList<AbstractItem> items = new ArrayList<>();

    public City(String name) {
        this.name = name;
    }

    public void setCitizens(Shorty... citizen) {
        this.citizens = new ArrayList<>(Arrays.asList(citizen));
    }

    public void addCitizen(Shorty citizen) {
        this.citizens.add(citizen);
    }

    public void removeCitizen(Shorty citizen) {
        this.citizens.remove(citizen);
    }

    public ArrayList<Shorty> getCitizens() {
        return new ArrayList<>(citizens);
    }

    public void setNewspapers(Newspaper... paper) {
        this.newspapers = new ArrayList<>(Arrays.asList(paper));
    }

    public void addNewspaper(Newspaper paper) {
        this.newspapers.add(paper);
    }

    public void removeNewspaper(Newspaper paper) {
        this.newspapers.remove(paper);
    }

    public ArrayList<Newspaper> getNewspapers() {
        return new ArrayList<>(newspapers);
    }

    public void setItems(AbstractItem... item) {
        this.items = new ArrayList<>(Arrays.asList(item));
    }

    public void addItem(AbstractItem item) {
        this.items.add(item);
    }

    public void removeItem(AbstractItem item) {
        this.items.remove(item);
    }

    public ArrayList<AbstractItem> getItems() {
        return new ArrayList<>(items);
    }

    public Shorty getRandomCitizen() {
        if (citizens.isEmpty()) return null;
        Random random = new Random();
        return citizens.get(random.nextInt(citizens.size()));
    }

    public void broadcastNews(NewsReport news) {
        System.out.println("НОВОСТЬ!: " + news.headline());
        for (Newspaper paper : newspapers) {
            paper.addNews(news);
        }
    }

    public void liveDay() {
        System.out.println("\n--- Новый день в городе " + name + " ---");
        for (Shorty shorty : new ArrayList<>(citizens)) {
            try {
                shorty.performAction(this);
            } catch (RuntimeException e) {
                System.err.println("Инцидент с " + shorty.getName() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        return "City{name='" + name + "', citizensCount=" + citizens.size() + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, citizens, newspapers, items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(name, city.name) &&
                Objects.equals(citizens, city.citizens) &&
                Objects.equals(newspapers, city.newspapers) &&
                Objects.equals(items, city.items);
    }
}