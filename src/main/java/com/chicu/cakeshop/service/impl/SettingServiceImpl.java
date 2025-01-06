package com.chicu.cakeshop.service.impl;


import com.chicu.cakeshop.enums.SettingEnum;
import com.chicu.cakeshop.model.Setting;
import com.chicu.cakeshop.repositories.SettingRepository;
import com.chicu.cakeshop.service.SettingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    public SettingServiceImpl(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    @Transactional
    public Setting getSetting(SettingEnum setting) {
        return settingRepository.findById(setting.getSettingName()).get();
    }
}
