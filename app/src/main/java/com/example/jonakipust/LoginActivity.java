package com.example.jonakipust;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonakipust.Database.FirebaseHelper;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.Model.LoginInfo;
import com.example.jonakipust.Model.Threads.ActavitionChecker;
import com.example.jonakipust.Model.UserAdditionalInfo;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserSelf;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText studentiD, password;
    TextView register,helpMessage;
    Button loginBTN;
    MainDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseHelper.loadLoginInfo(LoginActivity.this);
        FirebaseHelper.downloadAllData(LoginActivity.this);

        //ActavitionChecker.getInstance().start();
        SharedPreferences sp = getSharedPreferences("Login",MODE_PRIVATE);
        if(sp != null && sp.getString("uid",null) != null){
            String uid = sp.getString("uid",null);
            MainDBHelper helper = new MainDBHelper(LoginActivity.this);

            UserModel userModel = helper.getUserByUID(uid);

            if(userModel != null){
                UserSelf userSelf = UserSelf.getUserSelf(userModel,helper.getUserAdditionalInfoByUID(userModel.getUid()));
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Log.d(TAG,"ID not found.");
            }

        }else {
            dbHelper = new MainDBHelper(LoginActivity.this);

            studentiD = findViewById(R.id.et_login_student_id);
            studentiD.setText("190140");
            password = findViewById(R.id.et_login_password);
            password.setText("mysupersafe");
            register = findViewById(R.id.tv_register_first);
            helpMessage = findViewById(R.id.tv_login_help_message);
            loginBTN = findViewById(R.id.btn_login);

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
                    Log.d(TAG,"login clicked.");
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
                            Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();
                            Log.d(TAG,"valid id ----------------- : "+loginInfo.getUID());

                            int time=0;
                            while(!FirebaseHelper.alldownloaded && time <= 3) {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }time++;
                            }
                            if(!FirebaseHelper.alldownloaded){
                                helpMessage.setText("Wait few  moments and try again.");
                                return;
                            }

                            UserModel user = dbHelper.getUserByUID(loginInfo.getUID());
                            UserAdditionalInfo userAddInfo = dbHelper.getUserAdditionalInfoByUID(loginInfo.getUID());

                            SharedPreferences sp = getSharedPreferences("Login",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("uid",user.getUid());
                            editor.putString("Psw",password.getText().toString());
                            editor.commit();

                            UserSelf mySelf = UserSelf.getUserSelf(user,userAddInfo);

                            Toast.makeText(LoginActivity.this, "Start Activity", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Log.d(TAG,"not match : "+loginInfo.getPassword()+" "+loginInfo.getStudentID()+
                                    " and "+studentiD.getText().toString());
                        }
                        helpMessage.setText("Invalid ID or password.");
                    }else{
                        helpMessage.setText("Waite for download data");
                    }
                }
            });
        }
    }
}