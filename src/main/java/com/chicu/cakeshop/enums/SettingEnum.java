package com.chicu.cakeshop.enums;

import lombok.Getter;

@Getter
public enum SettingEnum {
    FILES_PATH("files-path"),
    DOMAIN("domain");

    SettingEnum(String settingName) {
        this.settingName = settingName;
    }


    private final String settingName;
}
