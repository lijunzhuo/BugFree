package com.example.mac.bugfree.controller;

import android.content.Context;

import com.example.mac.bugfree.module.ImageForElasticSearch;
import com.example.mac.bugfree.module.MoodEventList;
import com.example.mac.bugfree.module.User;
import com.example.mac.bugfree.util.LoadFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Zhi Li on 2017/3/30.
 */

public class ElasticsearchImageOfflineController {
    private static final String IMAGEONLINE = "ImageUploadList.sav";
    private static final String IMAGEUPLOADLIST = "ImageUploadList.sav";
    private static final String IMAGEDELETELIST = "ImageDeleteList.sav";
    private User user;
    private String base64str;
    private ArrayList<String> imageList;

    public void AddImageTask (){

    }
    public void DeleteImageTask(){
        
    }
    public void GetImageTask (){

    }
    /**
     *
     *  This method is for newly signed in user, it creates all the empty file for offline function for image
     *  This image offline function is for the current user's image only.
     *  Only being called once after valid signin.
     *  There are three files to be created:
     *  1. image.sav (file stores a dictionary contains image unique id and the image base64 String,
     *                  both already exist in elastic search and image need to be upload to/delete from ElasticSearch.)
     *  2. ImageOriginList.sav (Contains the [arraylist of [strings of [image unique id]]] user Originally have in the ElasticSearch)
     *  3. ImageUploadList.sav (Contains the [arraylist of [strings of [image unique id]]] to be uploaded to the ElasticSearch)
     *  4. ImageDeleteList.sav (Contains the [arraylist of [strings of [image unique id]]] to be deleted from the ElasticSearch)
     * @param context
     * @param user
     */
    public void prepImageOffline(Context context, User user){
        try {
            // Base64 dictionary file
//                FileOutputStream fos = context.openFileOutput(IMAGEFILENAME, Context.MODE_PRIVATE);
//                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
//
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("base64", "something");
////                System.out.println(map.get("dog"));
////                map.remove("base64");
//                Gson gson = new Gson();
//                gson.toJson(map, out);
//                out.flush();
//                fos.close();

            // Arraylist, [strings of [image unique id]]] already in ElasticSearch
//            FileOutputStream fos0 = context.openFileOutput(IMAGEONLINE, Context.MODE_PRIVATE);
//            BufferedWriter out0= new BufferedWriter(new OutputStreamWriter(fos0));

//            ArrayList<String> alreadyUp = new ArrayList<String>();
            MoodEventList MEL = user.getMoodEventList();
            for (int i = 0; i < MEL.getCount(); i++ ){
                String s = MEL.getMoodEvent(i).getPicId();
                if (s!=null) {
//                    alreadyUp.add(s);
                    ElasticsearchImageController.GetImageTask  getImageTask = new ElasticsearchImageController.GetImageTask();
                    getImageTask.execute(s);
                    ImageForElasticSearch imageForElasticSearch = new ImageForElasticSearch();
                    try {
                        imageForElasticSearch = getImageTask.get();
                        String base64 = imageForElasticSearch.getImageBase64();
                        saveBase64(context, base64,s);  // filename = moodevent.getId()
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
//
////
////            ElasticsearchUserController.GetUserTask getUserTask = new ElasticsearchUserController.GetUserTask();
////            getUserTask.execute(currentUserName);
////            User get = new User();
////
////            try {
////                get = getUserTask.get();
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//
//            ArrayList<String> picIdList = new ArrayList<>();
//            MoodEventList moodEventList = user.getMoodEventList();
//            for (MoodEvent moodEvent : moodEventList) {
//                if (moodEvent.getPicId() != null){
//                    picIdList.add(moodEvent.getPicId());
//
//                    ElasticsearchImageController.GetImageTask  getImageTask = new ElasticsearchImageController.GetImageTask();
//                    getImageTask.execute(moodEvent.getPicId());
//                    ImageForElasticSearch imageForElasticSearch = new ImageForElasticSearch();
//                    try {
//                        imageForElasticSearch = getImageTask.get();
//                        String base64 = imageForElasticSearch.getImageBase64();
//                        saveBase64(context, base64,s);;  // filename = moodevent.getId()
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }



//            Gson gson0 = new Gson();
//            gson0.toJson(alreadyUp, out0);
//            out0.flush();
//            fos0.close();

            // Arraylist, [strings of [image unique id]]] to be uploaded to the ElasticSearch
            FileOutputStream fos1 = context.openFileOutput(IMAGEUPLOADLIST, Context.MODE_PRIVATE);
            BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(fos1));

            ArrayList<String> upload = new ArrayList<String>();
            Gson gson1 = new Gson();
            gson1.toJson(upload, out1);
            out1.flush();
            fos1.close();

            // Arraylist, [strings of [image unique id]]] to be deleted from ElasticSearch
            FileOutputStream fos2 = context.openFileOutput(IMAGEDELETELIST, Context.MODE_PRIVATE);
            BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(fos2));

            ArrayList<String> delete = new ArrayList<String>();
            Gson gson2 = new Gson();
            gson2.toJson(delete, out2);
            out2.flush();
            fos2.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Save the base64String to file name by the uniqueID
     * @param context
     * @param imageBase64
     * @param base64Id
     */
    public void saveBase64(Context context, String imageBase64, String base64Id){
        try {
            FileOutputStream fos = context.openFileOutput(base64Id, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(imageBase64, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Load the origin list and add/delete the ids in the array list appropriately
     * @param context
     * @param mode
     * @param imageIdToBeSave
     * @param imageIdToBeDelete
     */
    public void updateOfflineArrayList(Context context, String mode, String imageIdToBeSave, String imageIdToBeDelete){
        try {
//            LoadFile load = new LoadFile();
            imageList = loadImageList(context, mode);

            // Change accordingly
            if (imageIdToBeDelete!= null) {
                imageList.remove(imageIdToBeDelete);
            }
            if (imageIdToBeDelete!= null) {
                imageList.add(imageIdToBeSave);
            }

            FileOutputStream fos = context.openFileOutput(IMAGEUPLOADLIST, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(imageList, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public String loadBase64(Context context, String fileName){
        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type type = new TypeToken<String>(){}.getType();
            base64str = gson.fromJson(in, type); // deserializes json into Map
            fis.close();
            return base64str;
        } catch (FileNotFoundException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Receives parameter of current context and the mode of loading, to load the online arraylist,
     * use "online",to load the upload list, use "upload", if want to have delete list, use "delete"
     * @param context
     * @param mode
     * @return
     */
    public ArrayList<String> loadImageList(Context context, String mode){
        try {
            String filename;
            if (mode.equals("upload")){
                filename = IMAGEUPLOADLIST;
            } else if (mode.equals("delete")){
                filename = IMAGEDELETELIST;
            }
//            else if (mode.equals("online")){
//                filename = IMAGEONLINE;
//            }
            else{
                return null;
            }

            FileInputStream fis = context.openFileInput(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            imageList = gson.fromJson(in, type); // deserializes json into ArrayList<String>
            fis.close();
            return imageList;
        } catch (FileNotFoundException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
