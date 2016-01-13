package com.mmadapps.practiseproject.Login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mmadapps.practiseproject.Flight.PostMethod;
import com.mmadapps.practiseproject.Login.Beans.LoginBeans;
import com.mmadapps.practiseproject.R;
import com.mmadapps.practiseproject.Utils.Helper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class LoginPage extends AppCompatActivity {
    ArrayList<LoginBeans> beanArrayList;
    String posturl="http://192.168.30.123/User/Authenticate";
    InputStream inputStream;
    String result,result1;
    EditText nameText,phoneText;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callDatabase();
        initializeView();
    }

    private void callDatabase() {
        try {
            Helper mHelper = new Helper(LoginPage.this);
            mHelper.createDataBase();
            mHelper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeView() {
        nameText= (EditText) findViewById(R.id.textView2);
        phoneText= (EditText) findViewById(R.id.textView3);
        submit= (Button) findViewById(R.id.button);
        setValues();
    }

    private void setValues() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginPageAuthentication().execute();
            }
        });

    }

    /**-------------------------------LoginPage-------------------------------------------------------------*/
    /**-------------------------------------------------------------------------------------------------------------*/

    public class LoginPageAuthentication extends AsyncTask<Void, Void, Boolean> {
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

            Boolean isValid=false;
            String nm,ph;
            nm=nameText.getText().toString();
            ph=phoneText.getText().toString();

            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(posturl);
            String json = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("AuthenticationId", "541654346");
                jsonObject.accumulate("AuthenticationType", "`1");
                jsonObject.accumulate("Email", "");
                jsonObject.accumulate("Password", nm);
                jsonObject.accumulate("PhoneNumber", ph);
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
                    // {"CreateSearchDataResult":{"Advertisement":null,"ErrorMessage":null,
                    // "Message":null,"Result":false,"SearchContent":null,"UserProfile":null}}
                } else {
                    Log.e("check_result", "null");
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            try {
                JSONObject jsonObject1 = new JSONObject(result);
                result1 = jsonObject1.getString("IsError");
                if(result1.equalsIgnoreCase("false"))
                {
                    isValid= true;
                }
                else {
                    isValid= false;

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return isValid;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result == true) {

                {
                    Toast.makeText(getApplicationContext(), "successfully ", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(LoginPage.this,PostMethod.class);
                    startActivity(intent);

                }


            } else {
                Toast.makeText(getApplicationContext(), "not successfully ", Toast.LENGTH_LONG).show();
            }
        }
    }}
