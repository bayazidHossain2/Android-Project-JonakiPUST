package com.example.jonakipust;

import static android.content.ContentValues.TAG;

import static com.example.jonakipust.R.string.try_login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonakipust.Database.FirebaseHelper;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.Model.LoginInfo;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserSelf;

public class LoginActivity extends AppCompatActivity {

    EditText studentiD, password,security;
    TextView register,helpMessage;
    Button loginBTN;
    MainDBHelper dbHelper;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseHelper.loadUserInfo(LoginActivity.this);
            }
        }).start();

        //FirebaseHelper.reset(LoginActivity.this, false);
        //FirebaseHelper.downloadAllData(LoginActivity.this);

        //ActavitionChecker.getInstance().start();
        SharedPreferences sp = getSharedPreferences("Login",MODE_PRIVATE);
        if(sp != null && sp.getString("uid",null) != null){
            String uid = sp.getString("uid",null);
            MainDBHelper helper = new MainDBHelper(LoginActivity.this);

            UserModel userModel = helper.getUserByUID(uid);
            Log.d(TAG,"You are in share preference.*******************");
            //sp = null;
            if(userModel != null){
                UserSelf userSelf = UserSelf.getUserSelf(userModel);
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Wait few moment to load data.");
                progressDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        finish();
                        startActivity(intent);
                    }
                }, 3000);

            }else{
                Log.d(TAG,"ID not found.");
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("uid",null);
                editor.putString("Psw",null);
                editor.commit();

                Intent intentL = new Intent(LoginActivity.this,LoginActivity.class);
                startActivity(intentL);
                LoginActivity.this.finish();
            }

        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FirebaseHelper.loadLoginInfo(LoginActivity.this);
                }
            }).start();
            FirebaseHelper.getSecurityCode("Login");
            dbHelper = new MainDBHelper(LoginActivity.this);

            studentiD = findViewById(R.id.et_login_student_id);
            //studentiD.setText("190140");
            password = findViewById(R.id.et_login_password);
            //password.setText("mysupersafe");
            register = findViewById(R.id.tv_register_first);
            helpMessage = findViewById(R.id.tv_login_help_message);
            loginBTN = findViewById(R.id.btn_login);
            security = findViewById(R.id.et_login_security_code);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    intent.putExtra("uid","");
                    startActivity(intent);
                    finish();
                }
            });

            loginBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(LoginActivity.this,"login clicked",Toast.LENGTH_SHORT).show();
                    //Log.d(TAG,"login clicked.");

                    if(!FirebaseHelper.isConnected(LoginActivity.this)){
                        helpMessage.setText("Your are not online. Check your connection and try again.");
                        return;
                    }
                    if(!FirebaseHelper.alldownloaded){
                        helpMessage.setText("Wait few moment for download.");
                        return;
                    }
                    if(FirebaseHelper.loginInfoDownloaded){
                        LoginInfo loginInfo = dbHelper.getLoginInfo(studentiD.getText().toString());
                        if(loginInfo == null){
                            helpMessage.setText("You are not resistor yet.");
                            return;
                        }
                        if(password.getText().toString().equals(loginInfo.getPassword())){
                            if(FirebaseHelper.securityCode == null){
                                helpMessage.setText("Wait for download security code.");
                                return;
                            }
                            String secq = "L"+security.getText().toString();
                            if(!FirebaseHelper.securityCode.equals(secq)){
                                helpMessage.setText("Invalid code.Get right code from jonaki admin.");
                                return;
                            }
                            //Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();
                            Log.d(TAG,"valid id ----------------- : "+loginInfo.getUID());
                            progressDialog = new ProgressDialog(LoginActivity.this);
                            progressDialog.setTitle("Login");
                            progressDialog.setMessage("Wait few moment to load data.");
                            progressDialog.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    //your code here
                                    if(!FirebaseHelper.alldownloaded){
                                        helpMessage.setText("Wait few  moments and try again.");
                                        progressDialog.dismiss();
                                    }else {
                                        UserModel user = dbHelper.getUserByUID(loginInfo.getUID());

                                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("uid", user.getUid());
                                        editor.putString("Psw", password.getText().toString());
                                        editor.commit();

                                        UserSelf mySelf = UserSelf.getUserSelf(user);

                                        Toast.makeText(LoginActivity.this, "Start Activity", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                        finish();
                                    }
                                }
                            }, 5000);
                        }else{
                            Log.d(TAG,"not match : "+loginInfo.getPassword()+" "+loginInfo.getStudentID()+
                                    " and "+studentiD.getText().toString()+" and p :"+password.getText().toString());

                            helpMessage.setText("Invalid ID or password.");
                        }

                    }else{
                        helpMessage.setText("Waite for download data");
                    }
                }
            });
        }
    }
}