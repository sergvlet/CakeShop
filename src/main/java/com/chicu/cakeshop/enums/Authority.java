package com.chicu.cakeshop.enums;

import lombok.Getter;

@Getter
public enum Authority {
    ROLE_ADMIN("Админ"),
    ROLE_USER("Клиент");

    private final String readableName;

    Authority(String readableName) {
        this.readableName = readableName;
    }
}
