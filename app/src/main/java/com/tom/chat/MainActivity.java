package com.tom.chat;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    GoogleCloudMessaging gcm ;
    public static final String USERID = "tom";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GCMRegistrationAsyncTask().execute();
    }

    class GCMRegistrationAsyncTask extends AsyncTask<Void, Void, String>{
        private static final String SENDER_ID = "642623149983";
        String rid = null;
        @Override
        protected String doInBackground(Void... params) {
            //向GCM雲端註冊 並取得Registration Id
            if (gcm==null){
                gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            }
            try {
                rid = gcm.register(SENDER_ID);
                //將rid與userid 送到後端儲存
                URL url = new URL("http://140.137.215.10:8080/atm/r?rid="+rid+"&uid="+USERID);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                int data = is.read();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return rid;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
