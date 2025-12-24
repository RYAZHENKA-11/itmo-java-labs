package ru.shortystory.environment;

import ru.shortystory.records.NewsReport;

import java.util.ArrayList;
import java.util.Objects;

public class Newspaper {
    private String title;
    private ArrayList<NewsReport> headlines = new ArrayList<>();

    public Newspaper(String title) {
        this.title = title;
    }

    public void addNews(NewsReport news) {
        if (news != null) {
            headlines.add(news);
        }
    }

    public ArrayList<NewsReport> getHeadlines() {
        return new ArrayList<>(headlines);
    }

    public void printIssue() {
        System.out.println("--- ГАЗЕТА: " + title + " ---");
        if (headlines.isEmpty()) {
            System.out.println("Новостей нет. В городе спокойно.");
        } else {
            for (NewsReport report : headlines) {
                System.out.println(report);
            }
        }
        System.out.println();
        headlines.clear();
    }

    @Override
    public String toString() {
        return "Newspaper{title='" + title + "', headlinesCount=" + headlines.size() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Newspaper newspaper = (Newspaper) o;
        return Objects.equals(title, newspaper.title) &&
                Objects.equals(headlines, newspaper.headlines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, headlines);
    }
}