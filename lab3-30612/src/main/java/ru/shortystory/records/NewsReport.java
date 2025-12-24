package ru.shortystory.records;

import ru.shortystory.enums.IncidentType;

public record NewsReport(String headline, String description, IncidentType type) {
}