package com.TripRider.TripRider.domain.user;

import lombok.Getter;

@Getter
public enum BadgeType {
    BEGINNER("🌱 Beginner Rider", 10),   // 10km
    EXPLORER("🛵 Road Explorer", 50),    // 50km
    SCENIC("🌄 Scenic Rider", 100),      // 100km
    MASTER("🏍️ Master Rider", 500),     // 500km
    LEGEND("🏆 Legend Rider", 1000);     // 1000km

    private final String name;
    private final int distanceRequired;

    BadgeType(String name, int distanceRequired) {
        this.name = name;
        this.distanceRequired = distanceRequired;
    }
}
