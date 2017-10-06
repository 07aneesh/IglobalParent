package com.cts.cheetah.model;

/**
 * Created by manu.palassery on 04-05-2017.
 */

public class DocumentImage {

    public static final String REMOTE = "remote";
    public static final String LOCAL = "local";

    String imagePath;
    String imageName;
    String imageLocation;
    boolean isThumb;

    public DocumentImage() {

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public boolean isThumb() {
        return isThumb;
    }

    public void setThumb(boolean thumb) {
        isThumb = thumb;
    }
}
