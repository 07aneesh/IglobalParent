package com.cts.cheetah.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.cts.cheetah.model.DocumentImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by manu.palassery on 26-05-2017.
 * DocumentImageHelper - helps to download document images and saves to device.
 *
 */

public class DocumentImageHelper {
    Context context;
    int counter = 0;
    ArrayList<DocumentImage> imagesList = new ArrayList<>();
    IDocImagesDownloadComplete iDocImagesDownloadComplete;


    public DocumentImageHelper(Context context){
      this.context = context;
    }


    public void downloadImages(ArrayList<DocumentImage> imagesList,IDocImagesDownloadComplete iDocImagesDownloadComplete){
        this.iDocImagesDownloadComplete = iDocImagesDownloadComplete;
        this.imagesList = Utility.filterDownLoadedImages(imagesList,context);
        if(this.imagesList.size() > 0) {
            downloadImage(this.imagesList.get(0).getImagePath());
        }else{
            iDocImagesDownloadComplete.docImagesDownloadComplete();
        }
    }

    //Downloads a single image
    public void downloadImage(String url,IDocImagesDownloadComplete iDocImagesDownloadComplete ){
        this.iDocImagesDownloadComplete = iDocImagesDownloadComplete;
        downloadImage(url);
    }

    public void downloadImage(String url){
        class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

            public ImageDownloader() {

            }

            protected Bitmap doInBackground(String... urls) {
                String url = urls[0];
                Bitmap mIcon = null;
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return mIcon;
            }

            protected void onPostExecute(Bitmap result) {
                if(result != null) {

                }else{
                    //set noImage image.
                }
                onImageDownloaded(result);
            }
        }

        new ImageDownloader().execute(url);
    }

    private void onImageDownloaded(Bitmap bmp){
        try {
            String imgName = imagesList.get(counter).getImagePath();
            imgName = imgName.substring(imgName.lastIndexOf("/") + 1, imgName.length());
            if(imgName.contains("?")) {
                imgName = imgName.substring(0, imgName.lastIndexOf("?"));
            }
            saveDownloadedImage(imgName, bmp);
            ++counter;
            if (counter < imagesList.size()) {
                downloadImage(imagesList.get(counter).getImagePath());
            }else{
                //Notifies that all images are downloaded.
                iDocImagesDownloadComplete.docImagesDownloadComplete();
            }
        }catch (Exception e){
            Log.i("",e+"");
        }
    }


    //Downloaded images are saved.
    public void saveDownloadedImage(final String fileName, final Bitmap bmp){
       /* AsyncTask fileTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                File directory = new File(Utility.getDocImageFolderName(context));

                if (!directory.exists()) {
                    directory.mkdirs();
                }


                try {
                    File pictureFile = new File(directory, fileName);
                    pictureFile.createNewFile();
                    FileOutputStream out = new FileOutputStream(pictureFile);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        fileTask.execute();*/
        File directory = new File(Utility.getDocImageFolderName(context));

        if (!directory.exists()) {
            directory.mkdirs();
        }


        try {
            File pictureFile = new File(directory, fileName);
            pictureFile.createNewFile();
            FileOutputStream out = new FileOutputStream(pictureFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface IDocImagesDownloadComplete{
        void docImagesDownloadComplete();
    }
}
