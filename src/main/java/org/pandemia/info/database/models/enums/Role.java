package org.pandemia.info.database.models.enums;

public enum Role {

    RESIDENT("resident"),
    ADMIN("admin"),
    NURSE("nurse");

    private final String name;

    Role(String role) {
        this.name = role;
    }

    public String getName() {
        return name;
    }

    public static Role fromString(String text) {
        for (Role role : Role.values()) {
            if (role.name.equalsIgnoreCase(text)) {
                return role;
            }
        }
        return null;
    }
}
