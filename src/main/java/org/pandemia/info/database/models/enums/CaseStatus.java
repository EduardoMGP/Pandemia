package org.pandemia.info.database.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum CaseStatus {
    suspect("Suspeito"),
    confirmed("Confirmado"),
    recovered("Recuperado"),
    deceased("Óbito");

    private final String status;

    CaseStatus(String status) {
        this.status = status;
    }

    public static List<String> getNames() {
        List<String> names = new ArrayList<>(List.of());
        for (CaseStatus status : CaseStatus.values()) {
            names.add(status.getStatus());
        }
        return names;
    }

    public String getStatus() {
        return status;
    }
}
