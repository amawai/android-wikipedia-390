package org.wikipedia.imagesearch;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mnhn3 on 2018-04-12.
 */

public class Encoder {

    public String encodeFileToBase64Binary(File file){
        String encodedFile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            //encodedFile = new String(Base64.encodeBase64(bytes), "UTF-8");
            encodedFile = Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedFile;
    }

    //Encodes a file given a content Uri as input
    public String encodeUriToBase64Binary(Context context, Uri uri){
        String encodedFile = null;
        InputStream inputStream;
        File file = new File(getPath(context, uri));
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[(int)file.length()];
            inputStream.read(bytes);
            encodedFile = Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedFile;
    }

    //Retrieves the file path given a uri
    private String getPath(Context context, Uri uri ) {
        String filePath = null;
        if (uri != null && "content".equals(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }
}
