package com.project.projectboard.domain.type;

import lombok.Getter;

public enum FormStatus {
    CREATE("저장", false),
    UPDATE("수정", true);

    @Getter private String description;
    @Getter private Boolean update;

    FormStatus(String description, Boolean update) {
        this.description = description;
        this.update = update;
    }
}
