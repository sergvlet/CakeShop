package com.chicu.cakeshop.service.impl;

import com.chicu.cakeshop.repositories.ImageRepository;
import com.chicu.cakeshop.service.SettingService;

public class ImageServiceImplBuilder {
    private ImageRepository imageRepository;
    private SettingService settingService;

    public ImageServiceImplBuilder setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
        return this;
    }

    public ImageServiceImplBuilder setSettingService(SettingService settingService) {
        this.settingService = settingService;
        return this;
    }

    public ImageServiceImpl createImageServiceImpl() {
        return new ImageServiceImpl(imageRepository, settingService);
    }
}