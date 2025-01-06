package com.chicu.cakeshop.service.impl;


import com.chicu.cakeshop.enums.ImageType;
import com.chicu.cakeshop.enums.SettingEnum;
import com.chicu.cakeshop.model.Image;
import com.chicu.cakeshop.repositories.ImageRepository;

import com.chicu.cakeshop.service.ImageService;
import com.chicu.cakeshop.service.SettingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final SettingService settingService;

    public ImageServiceImpl(ImageRepository imageRepository, SettingService settingService) {
        this.imageRepository = imageRepository;
        this.settingService = settingService;
    }


    @Override
    public void deleteImageById(int imageId) throws IOException {
        Optional<Image> image = imageRepository.findById(imageId);
        if (image.isPresent()) {
            deleteImage(image.get());
        }
    }

    @Override
    public void deleteImage(Image image) throws IOException {
        String filesPath = settingService.getSetting(SettingEnum.FILES_PATH).getValue();
        Path imagePath = Paths.get(filesPath + File.separator
                + image.getImageType().getDirectoryName() + File.separator
                + image.getName());
        Files.deleteIfExists(imagePath);
        imageRepository.delete(image);
    }

    @Override
    public Image saveImage(MultipartFile file, ImageType imageType) throws IOException {
        String filesPath = settingService.getSetting(SettingEnum.FILES_PATH).getValue();
        String imageName = UUID.randomUUID() + ".jpg";

        File saveDirectory = new File(filesPath + File.separator + imageType.getDirectoryName());
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        Path path = Paths.get(saveDirectory + File.separator + imageName);
        cropCompressAndSave(file, path, imageType);

        return imageRepository.save(Image.builder()
                .imageType(imageType)
                .name(imageName)
                .build());
    }

    @Override
    public Image getByName(String name) throws IOException {
        return imageRepository.getImageByName(name);
    }

    @Override
    @Transactional
    public byte[] getImageAsByteArray(String name) throws IOException {
        Image image = getByName(name);
        String filesPath = settingService.getSetting(SettingEnum.FILES_PATH).getValue();
        File imageFile = new File(filesPath + File.separator + image.getImageType().getDirectoryName()
                + File.separator + image.getName());

        if (!imageFile.exists()) {
            throw new IOException("Image file not exists"); //todo change to our exception type
        }

        byte[] byteArray = new byte[(int) imageFile.length()];

        try (FileInputStream inputStream = new FileInputStream(imageFile)) {
            inputStream.read(byteArray);
        }

        return byteArray;
    }

    public void cropCompressAndSave(MultipartFile multipartFileImage, Path path, ImageType imageType) throws IOException {
        //cropping
        //-------------------
        BufferedImage bufferedImage = ImageIO.read(multipartFileImage.getInputStream());
        BufferedImage croppedImage;

        float initialRatio = (float) bufferedImage.getWidth() / bufferedImage.getHeight();
        if (initialRatio > imageType.getRatio()) {
            int neededWidth = Math.round(bufferedImage.getHeight() * imageType.getRatio());
            croppedImage = bufferedImage.getSubimage((bufferedImage.getWidth() - neededWidth) / 2, 0,
                    neededWidth, bufferedImage.getHeight());
        } else {
            int neededHeight = Math.round(bufferedImage.getWidth() / imageType.getRatio());
            croppedImage = bufferedImage.getSubimage(0, (bufferedImage.getHeight() - neededHeight) / 2,
                    bufferedImage.getWidth(), neededHeight);
        }


        BufferedImage imageForResize = removeAlphaChannel(croppedImage);

        //changing resolution
        //-------------------
        BufferedImage resizedImage = resizeImage(croppedImage, imageType.getWidth(), imageType.getHeight());

        //compressing
        //-------------------
        BufferedImage imageForCompression = removeAlphaChannel(resizedImage);
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("JPEG");
        ImageWriter writer = writers.next();
        File outputFile = new File(path.toUri());
        ImageOutputStream outputStream = ImageIO.createImageOutputStream(outputFile);
        writer.setOutput(outputStream);
        ImageWriteParam params = writer.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(0.85f);
        writer.write(null, new IIOImage(imageForCompression, null, null), params);
        outputStream.close();
        writer.dispose();
    }



    private BufferedImage resizeImage(BufferedImage srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, Transparency.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    private static BufferedImage removeAlphaChannel(BufferedImage img) {
        if (!img.getColorModel().hasAlpha()) {
            return img;
        }

        BufferedImage target = createImage(img.getWidth(), img.getHeight(), false);
        Graphics2D g = target.createGraphics();
        // g.setColor(new Color(color, false));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return target;
    }

    private static BufferedImage createImage(int width, int height, boolean hasAlpha) {
        return new BufferedImage(width, height, hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
    }
}
