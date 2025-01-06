package com.chicu.cakeshop.service;


import com.chicu.cakeshop.enums.ImageType;
import com.chicu.cakeshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void deleteImage(Image image) throws IOException;
    void deleteImageById(int imageId) throws IOException;
    Image saveImage(MultipartFile file, ImageType imageType) throws IOException;
    Image getByName(String name) throws IOException;
    byte[] getImageAsByteArray(String name) throws IOException;
}
