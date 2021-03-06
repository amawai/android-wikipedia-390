package org.wikipedia.imagesearch;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by mnhn3 on 2018-04-09.
 */

public class ImageLabeler extends AsyncTask<String, Void, String> {
    private static final int MAXLABELS = 5;
    private static final double LABELTHRESHHOLD = 0.9;
    private final String key;
    private final String path;


    public ImageLabeler() {
        key = "Key cf037dad49144c56b882dec39ef8a832";
        path = "https://api.clarifai.com/v2/models/aaa03c23b3724a16a56b629203edc62c/outputs";
    }

    // calls to the API are made within this method
    @Override
    protected String doInBackground(String... strings) {

        String encodedString = strings[0];
        BufferedReader reader = null;
        String response;
        String result = "";

        //create JSON request body, called inputs
        JSONObject base64 = new JSONObject();
        JSONObject image = new JSONObject();
        JSONObject data = new JSONObject();
        JSONArray inputsArr = new JSONArray();
        JSONObject inputs = new JSONObject();
        try {
            inputs.put("inputs",  inputsArr);
                inputsArr.put(data);
                    data.put("data", image);
                        image.put("image", base64);
                            base64.put("base64", encodedString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            URL httpurl = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", key);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false);
            //add json as requestbody
            OutputStream os = conn.getOutputStream();
            os.write(inputs.toString().getBytes("UTF-8"));
            os.close();

            int statusCode = conn.getResponseCode();
            if (statusCode ==  HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                response = sb.toString();
                JSONObject jsonObject = new JSONObject(response);

                JSONArray outputs = jsonObject.getJSONArray("outputs");
                JSONArray concepts = outputs.getJSONObject(0).getJSONObject("data").getJSONArray("concepts");

                for (int i = 0; i < concepts.length(); i++) { //parses through json for concepts with high correlation
                    if (i == MAXLABELS) {
                        break;
                    }
                    JSONObject concept = concepts.getJSONObject(i);
                    double value = concept.getDouble("value");
                    String name = concept.getString("name");
                    if (value > LABELTHRESHHOLD) {
                        result += name + ",";
                    }
                }
            }

        } catch (Exception e) {
            return  "-1";
        }

        return result;
    }

    // Grabs the result from doInBackground and returns it to the caller
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
