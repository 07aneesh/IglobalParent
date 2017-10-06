package com.cts.cheetah.helpers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cts.cheetah.interfaces.IImageDownload;
//import com.cts.cheetah.view.orders.activties.adapters.TripDocumentsRecyclerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by manu on 3/9/2016.
 */
public class ImageProcessor {
    private final int FILE_SIZE_MB_LIMIT = 5;
    int THUMBNAIL_SIZE = 1200;
    Context context;
   public ImageProcessor(){

    }

    /**
     *
     * @param selectedImageUri
     * @param context
     * copyGalleryImage() copies an image selected from Gallery to app's image folder.
     */
    public void copyGalleryImage(Uri selectedImageUri, Context context,File folder){

        this.context = context;
        Long fileSize = (folder.length()/1024)/1024;
        if(fileSize <= FILE_SIZE_MB_LIMIT) {

            try {

                int thumbWidth = 250;
                int thumbHeight = 250;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                //
                String thumbUrl = folder.getAbsolutePath();
                String thumbFolder = thumbUrl.substring(0,thumbUrl.lastIndexOf("/")) + "/thumb/";
                thumbUrl = thumbUrl.substring(thumbUrl.lastIndexOf("/") + 1, thumbUrl.length());
                //
                boolean isImageSaved = false;

                Bitmap bitmap = null;
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdir();
                }

                if (success) {
                    try {
                        /*BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inDither = false;
                        options.inSampleSize = 4;
                        options.inScaled = false;
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;*/


                        //THUMBNAIL_SIZE is 1200 for saving image
                        bitmap = getThumbnail(selectedImageUri);

                        //---------------------------------------------------------------------------------
                        //Scale bitmap if image is bigger in size
                        int pictWidth = bitmap.getWidth();
                        int pictHeight = bitmap.getHeight();
                        if (pictHeight >= 2000 || pictWidth >= 2000) {
                            pictWidth = pictWidth / 2;
                            pictHeight = pictHeight / 2;
                        } else if (pictHeight >= 2000 && pictWidth >= 2000) {
                            pictWidth = pictWidth / 4;
                            pictHeight = pictHeight / 4;
                        }
                        //bitmap = Bitmap.createScaledBitmap(bitmap, pictWidth, pictHeight, false);
                        //---------------------------------------------------------------------------------

                        FileOutputStream out = new FileOutputStream(folder);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                        out.flush();
                        out.close();
                        isImageSaved = true;
                        //
                        folder = new File(thumbFolder);
                        success = true;
                        if (!folder.exists()) {
                            success = folder.mkdir();
                        }

                        if (success) {
                            try {
                                THUMBNAIL_SIZE = 100;

                                //THUMBNAIL_SIZE is 100 for saving thumb image
                                bitmap = getThumbnail(selectedImageUri);
                                File mypath = new File(thumbFolder, thumbUrl);
                                out = new FileOutputStream(mypath);
                                //bitmap = ThumbnailUtils.extractThumbnail(bitmap, thumbWidth, thumbHeight);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                out.flush();
                                out.close();
                                isImageSaved = true;
                            } catch (Exception e) {
                                Log.e("THUMB SAVING ", "ERROR:" + e.toString());
                                isImageSaved = false;
                            }
                        } else {
                            isImageSaved = false;
                        }

                        bitmap = null;

                    } catch (Exception exception) {
                        Log.e("BMP", exception + "");
                        isImageSaved = false;
                    }

                } else {
                    isImageSaved = false;
                }

                if (isImageSaved) {
                    //----------------------------------------------
                }
            } catch (Exception e) {

            }
        }else{
            Toast.makeText(context,"Sorry. Files larger than 5MB cannot be processed.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    //-----------------------
    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException{
        InputStream input = context.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    public static void createThumbImage(String filePath,String directory){
        try {
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
            Bitmap bmp = Utility.getBitmapFromUrl(50,50,filePath);
            File _directory = new File(directory);
            if(!_directory.exists()){
                _directory.mkdir();
            }

            File pictureFile = new File(_directory, fileName);
            pictureFile.createNewFile();
            FileOutputStream out = new FileOutputStream(pictureFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //-----------------------

    public static void downloadImage(ImageView imageView, String url, IImageDownload iImageDownload){
        class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;
            IImageDownload iImageDownload;

            public ImageDownloader(ImageView bmImage,IImageDownload iImageDownload) {
                this.bmImage = bmImage;
                this.iImageDownload = iImageDownload;
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
                iImageDownload.onImageDownloaded(result);
            }
        }

        new ImageDownloader(imageView,iImageDownload).execute(url);
    }


}


