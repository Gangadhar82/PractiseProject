package com.mmadapps.practiseproject.Signup;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import com.mmadapps.practiseproject.Login.LoginPage;
import com.mmadapps.practiseproject.R;

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

public class SignupFieldsActivity extends AppCompatActivity {
    EditText editname,editemail,editpass,editphone;
    Button next;
    String result1,result;
    InputStream inputStream;
    String posturl="http://192.168.30.123/User/Register";
    String Errormsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_fields);
        editname= (EditText) findViewById(R.id.nameSignup);
        editemail= (EditText) findViewById(R.id.emailSignup);
        editpass= (EditText) findViewById(R.id.passwordSignup);
        editphone= (EditText) findViewById(R.id.phonenumberSignup);
        next= (Button) findViewById(R.id.nextbutton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginPageAuthentication().execute();

            }
        });


    }
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
            String Firstname,email,password,phonenumber;
            Firstname=editname.getText().toString();
            email=editemail.getText().toString();
            password=editpass.getText().toString();
            phonenumber=editphone.getText().toString();

            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(posturl);
            String json = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("FacebookId", "");
                jsonObject.accumulate("GoogleId", "");
                jsonObject.accumulate("AuthenticationType", "1");
                jsonObject.accumulate("FirstName", Firstname);
                jsonObject.accumulate("Email", email);
                jsonObject.accumulate("Password", password);
                jsonObject.accumulate("PhoneNumber", phonenumber);
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
                    result1 = convertInputStreamToString(inputStream);
                    Log.e("check_result", result1);
                    // {"CreateSearchDataResult":{"Advertisement":null,"ErrorMessage":null,
                    // "Message":null,"Result":false,"SearchContent":null,"UserProfile":null}}
                } else {
                    Log.e("check_result", "null");
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            try {
                JSONObject jsonObject1 = new JSONObject(result1);
                JSONObject retrive = jsonObject1.getJSONObject("ExceptionObject");
                Errormsg=retrive.getString("ErrorMessage");
                result = jsonObject1.getString("ResponseObject");
                if(result.equalsIgnoreCase("true"))
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
            if (result) {
                {
                    Toast.makeText(getApplicationContext(), "successfully ", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(SignupFieldsActivity.this,LoginPage.class);
                    startActivity(intent);
                }

            } else {
                Toast.makeText(getApplicationContext(), Errormsg+"", Toast.LENGTH_LONG).show();
            }
        }
    }
}
/*
{
"FacebookId":"541654346",
        "GoogleId":"",
        "AuthenticationType":2,
        "FirstName":"Manas",
        "Email":"manasranjan@gmail.com",
        "Password":"",
        "PhoneNumber":""
        }*/
