package com.cts.cheetah.helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by manu.palassery on 09-05-2017.
 * Store the status codes from API, into a text file.
 * Values are retrieved with getStatusName() and getStatusCode functions in this class;
 */

public class StatusCodeManager {
    Context context;
    static final String filename = "status_codes.txt";

    public StatusCodeManager(Context context){
        this.context = context;
    }



    public static void writeStatusCodeFile(Context context,String data){
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        }catch (Exception e){

        }
    }

    public static JSONObject readStatusCodeFile(Context context){
        String data="";
        try{
            FileInputStream fileIn=context.openFileInput(filename);
            InputStream fis=fileIn;
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));

            try {
                char[] buffer=new char[200];
                while(true)
                {   int temp= br.read(buffer,0,buffer.length);
                    if(temp<0)
                        break;
                    else
                        data = data+new String(buffer, 0, temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch(Exception e){
            System.err.println("Error: Target File Cannot Be Read");
        }

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(data);
        }catch (JSONException e){

        }
        return jsonObject;
    }

    public static String getStatusCode(Context context,String group,String value){
        String code=null;
        try {
            JSONArray jsonArray = null;
            JSONObject jsonObject = readStatusCodeFile(context);
            if(jsonObject != null) {
                jsonArray = jsonObject.getJSONArray(group);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = new JSONObject(jsonArray.get(i).toString());
                    if (o.getString("ValueString").equals(value)) {
                        code = o.getString("Key");
                    }
                }
            }
        }catch (JSONException e){
            Log.i("STATUS CODE EXCPN",e+"");
        }
        return code;
    }

    public static String getStatusName(Context context,String group,String value){
        String name=null;
        try {
            JSONArray jsonArray = null;
            JSONObject jsonObject = readStatusCodeFile(context);
            if(jsonObject != null) {
                jsonArray = jsonObject.getJSONArray(group);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = new JSONObject(jsonArray.get(i).toString());
                    if (o.getString("Key").equals(value)) {
                        name = o.getString("ValueString");
                    }
                }
            }
        }catch (JSONException e){
            Log.i("STATUS CODE NAME EXCPN",e+"");
        }
        return name;
    }
}
