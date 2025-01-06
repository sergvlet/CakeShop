package com.chicu.cakeshop.service;


import com.chicu.cakeshop.enums.SettingEnum;
import com.chicu.cakeshop.model.Setting;

public interface SettingService {
    Setting getSetting(SettingEnum setting);
}
