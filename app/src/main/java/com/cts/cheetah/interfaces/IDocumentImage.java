package com.cts.cheetah.interfaces;

/**
 * Created by manu.palassery on 03-04-2017.
 */

public interface IDocumentImage {
    void onImageSave(String filename,String filePath);
    void showImageOptions();
    void removeDocumentImage(int position);
}
