package com.smartmatic.sitesurvey;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Reynaldo on 24/11/2015.
 */
public class FileReader {

    static String response;
    private static final String getFile = "GetUrl.txt";
    private static final String postFile = "PostUrl.txt";

    public static String getUrl(Context context, int method) {
        try {
            return parse(getConfigFile(context,method));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedReader getConfigFile(Context context,int method) {
        if(isExternalStorageReadable()){
            // Get the directory for the app's private files
            File file = null;
            try {
                if(method==1) file = new File(context.getExternalFilesDir(null), getFile);
                if(method==2) file = new File(context.getExternalFilesDir(null), postFile);

                if (file!=null) {
                    java.io.FileReader fr = new java.io.FileReader(file);
                    return new BufferedReader(fr);
                }else{
                    return null;
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /* Checks if external storage is available to at least read */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private static String parse(BufferedReader reader) throws IOException {
        try {
            String line = reader.readLine();
            while( line !=null){
                response=line;
                line = reader.readLine();
            }

            return response;
        } finally {
            reader.close();
        }
    }
}
