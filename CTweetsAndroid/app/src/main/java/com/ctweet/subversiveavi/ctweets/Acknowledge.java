package com.ctweet.subversiveavi.ctweets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Acknowledge {

    final static String serverUrl = "http://www.onlineparikshya.com/tweets/phps/";
    HttpClient client;
    JSONArray json2;
    String tweetid;
    Context parent;


    public void create(Context parent, String tweetid) {
        this.tweetid= tweetid;
        this.parent = parent;
        client = new DefaultHttpClient();
        new Read().execute();

    }

    public JSONArray gotData () throws IOException, JSONException{
        UserDetails ud= getUserDetails();
        String url = serverUrl + "acknowledgeTweet.php?token=" + ud.token + "&tweetID="+ tweetid;
        HttpGet get = new HttpGet(url);
        HttpResponse r = client.execute(get);
        int status = r.getStatusLine().getStatusCode();
        if (status == 200){
            HttpEntity e = r.getEntity();
            String data = EntityUtils.toString(e);
            JSONArray returnedData = new JSONArray(data);
            return returnedData;
        }else{
            return null;
        }
    }

    public class Read extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                json2 = gotData();
                return json2.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String st) {


        }
    }

    public UserDetails getUserDetails(){
        UserDetails ud = new UserDetails();


        try {

            SharedPreferences preferences = parent.getSharedPreferences("login_records", Context.MODE_PRIVATE);
            ud.token = preferences.getString("token", "");
            ud.name = preferences.getString("name", "");
            ud.type = preferences.getString("type", "");



        } catch (Exception f) {
            f.printStackTrace();
        }
        return ud;
    }

}
