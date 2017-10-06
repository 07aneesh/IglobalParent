package com.cts.cheetah.interfaces;

/**
 * Created by manu.palassery on 17-05-2017.
 */

public interface IAmazonUpload {
    void imageUploadSuccess();
    void thumbImageUploadSuccess();
    void imageUploadError(String error);
}
