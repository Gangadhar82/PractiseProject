package com.mmadapps.practiseproject.Flight;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mmadapps.practiseproject.Flight.beans.flightdetails;
import com.mmadapps.practiseproject.R;
import com.mmadapps.practiseproject.Utils.Helper;
import com.mmadapps.practiseproject.Utils.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostMethod extends AppCompatActivity {
String result,result1;
    InputStream inputStream;
    String posturl="http://192.168.30.123/FlightByDateAndFlightNo";

    ArrayList<flightdetails>  flightdetails;



Button postbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_method);
        postbutton= (Button) findViewById(R.id.postbutton);
        flightdetails=new ArrayList<>();
        postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Postmethod().execute();
            }
        });
    }

    /**-------------------------------LoginPage-------------------------------------------------------------*/
    /**-------------------------------------------------------------------------------------------------------------*/

    public class Postmethod extends AsyncTask<Void, Void, Boolean> {
        private String convertInputStreamToString(InputStream inputStream)
                throws IOException {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return callService();
        }

        private Boolean callService() {

            Boolean isValid = false;


            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(posturl);
            String json = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("Carrier", "6E");
                jsonObject.accumulate("FlightNo", "433");
                jsonObject.accumulate("DepYear", "2016");
                jsonObject.accumulate("DepMonth", "01");
                jsonObject.accumulate("DepDay", "10");
                json = jsonObject.toString();
                Log.e("name", json);
            } catch (Exception j) {
                j.printStackTrace();
            }
            try {
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = client.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.e("check_result", result);

                } else {
                    Log.e("check_result", "null");
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

               /* JSONObject jsonObject1 = new JSONObject(result);
                result1 = jsonObject1.getString("IsError");
                if (result1.equalsIgnoreCase("false")) {
                    isValid = true;
                } else {
                    isValid = false;

                }
*/
                JsonParser jsonParser=new JsonParser();
            flightdetails= jsonParser.parsingflightdetails(result);
            if(flightdetails==null||flightdetails.size()==0)
            {
                isValid=false;
            }
            else {
                Helper helper=new Helper(PostMethod.this);
                try {
                    helper.openDataBase();
                    helper.insertflightdetails(flightdetails);
                    helper.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                isValid=true;

            }


            return isValid;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {

                {
                    Toast.makeText(getApplicationContext(), "successfully ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PostMethod.this, listFlight.class);
                    startActivity(intent);

                }


            } else {
                Toast.makeText(getApplicationContext(), "not successfully ", Toast.LENGTH_LONG).show();
            }
        }
    }}
