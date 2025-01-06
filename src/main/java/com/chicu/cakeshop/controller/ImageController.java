package com.chicu.cakeshop.controller;


import com.chicu.cakeshop.service.ImageService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping()
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/images", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@Param("name") String name) throws IOException, InterruptedException {
        return imageService.getImageAsByteArray(name);
    }

}
