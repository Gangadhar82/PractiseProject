package com.mmadapps.practiseproject.Signup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.mmadapps.practiseproject.Login.LoginPage;
import com.mmadapps.practiseproject.R;

public class SignupActivity extends AppCompatActivity {
   Button signup,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup= (Button) findViewById(R.id.signupButton);
        login= (Button) findViewById(R.id.LoginButton);

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButton:
                  Intent intentsignup=new Intent(SignupActivity.this,SignupFieldsActivity.class);
                startActivity(intentsignup);
                break;
            case R.id.LoginButton:
                   Intent intentLogin=new Intent(SignupActivity.this,LoginPage.class);
                startActivity(intentLogin);
                break;
        }
    }
}
