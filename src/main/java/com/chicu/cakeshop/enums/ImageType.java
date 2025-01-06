package com.chicu.cakeshop.enums;

import lombok.Getter;

@Getter
public enum ImageType {
    PRODUCT_IMAGE(600, 744, "product-images"),
    CATEGORY_IMAGE(600, 404, "category-images"),
    ABOUT_IMAGE(800, 800, "about-images"),
    CAROUSEL_IMAGE((int) (1600 * 1.2), (int) (775 * 1.2), "carousel-images"),
    FILLER_IMAGE(600, 372 * 2, "filler-images"),
    DECOR_IMAGE(600, 372 * 2, "decor-images"),
    BLOG_IMAGE(800, 400, "blog-images");

    ImageType(int width, int height, String directoryName) {
        this.width = width;
        this.height = height;
        this.ratio = (float) width / height;
        this.directoryName = directoryName;
    }


    private final float ratio;
    private final int width;
    private final int height;
    private final String directoryName;
}
