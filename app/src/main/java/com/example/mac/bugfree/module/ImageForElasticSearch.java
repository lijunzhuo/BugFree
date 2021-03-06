package com.example.mac.bugfree.module;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by mac on 2017-03-25.
 */

public class ImageForElasticSearch {

    private String imageBase64;
    private String uniqueId;

    public ImageForElasticSearch(String base64) {
        this.imageBase64 = base64;
    }

    public ImageForElasticSearch(String base64, String id){
        this.imageBase64 = base64;
        this.uniqueId = id;
    }
    public ImageForElasticSearch(){

    }

    public Bitmap base64ToImage() {
        // TODO: change base64 String into image and store it
        byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                decodedString.length);

        return decodedByte;
    }
//TODO
    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
