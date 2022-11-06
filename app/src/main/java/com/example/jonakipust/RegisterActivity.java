package com.example.jonakipust;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonakipust.Database.FirebaseHelper;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.Model.LoginInfo;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserSelf;

import java.util.Date;


public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText profileURL,name,password,phone,numberOfDonation,lastDonationDate;
    EditText weight,heightfit,heightInch,studentID,curAddress,parAddress;
    TextView haveAccount, helpMessage;
    Spinner bloodGroupSpi;
    Button registor;
    String sBloodGroup,uid;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profileURL = findViewById(R.id.et_user_profile);
        name = findViewById(R.id.et_user_name);
        password = findViewById(R.id.et_login_password);
        bloodGroupSpi = findViewById(R.id.sp_blood_group);
        phone = findViewById(R.id.et_phone);
        numberOfDonation = findViewById(R.id.et_number_of_donation);
        lastDonationDate = findViewById(R.id.et_last_donation_date);
        weight = findViewById(R.id.et_weight);
        heightfit = findViewById(R.id.et_height_fit);
        heightInch = findViewById(R.id.et_height_inch);
        studentID = findViewById(R.id.et_login_student_id);
        curAddress = findViewById(R.id.et_cur_address);
        parAddress = findViewById(R.id.et_par_address);
        haveAccount = findViewById(R.id.tv_have_account);
        helpMessage = findViewById(R.id.tv_help_message);
        registor = findViewById(R.id.btn_register);
        sBloodGroup = "";
        MainDBHelper dbHelper = new MainDBHelper(RegisterActivity.this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegisterActivity.this,
                R.array.blood_group,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpi.setAdapter(adapter);
        bloodGroupSpi.setOnItemSelectedListener(RegisterActivity.this);

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        uid = getIntent().getStringExtra("uid");
        if(!uid.equals("")){
            UserModel user = dbHelper.getUserByUID(uid);
            studentID.setText(user.getStudentId()+"");
            studentID.setVisibility(View.INVISIBLE);
            password.setText(getSharedPreferences("Login",MODE_PRIVATE).getString("Psw",""));
            String[] purl = user.getProfile().split("=");
            profileURL.setText("https://drive.google.com/file/d/" + purl[2] + "/view?usp=sharing");
            name.setText(user.getName());
            phone.setText(user.getPhone());
            numberOfDonation.setText(user.getNumberOfDonation()+"");
            lastDonationDate.setText(user.getLastDonationDate());
            weight.setText(user.getWeight()+"");
            heightfit.setText(user.getHeightFit());
            heightInch.setText(user.getHeightInch());
            curAddress.setText(user.getCurrentAddress());
            parAddress.setText(user.getParmanentAddress());
            registor.setText("Update");
            this.sBloodGroup = user.getBloodGroup();
        }
        registor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(studentID.getText().toString().length()!=6){
                    helpMessage.setText("Invalid Student ID.");
                    //Toast.makeText(RegisterActivity.this, "Invalid Student ID.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(password.getText().toString().length()<6){
                    helpMessage.setText("Password must >= 6 character.");
                    //Toast.makeText(RegisterActivity.this, "Password must >= 6 character.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(name.getText().toString().isEmpty()){
                    helpMessage.setText("Name not found.");
                    //Toast.makeText(RegisterActivity.this, "Name not found.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(sBloodGroup.equals("")){
                    helpMessage.setText("Blood group not selected.");
                    //Toast.makeText(RegisterActivity.this, "Blood group not selected.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(phone.getText().toString().length()!=11||!phone.getText().toString().startsWith("01")){
                    helpMessage.setText("Invalid Phone number.");
                    //Toast.makeText(RegisterActivity.this, "Invalid Phone number.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!profileURL.getText().toString().isEmpty()){
                    String url = profileURL.getText().toString();
                    String[] id=url.split("/");
                    if(id.length > 5) {
                        String imageLink = "https://drive.google.com/uc?export=view&id=" + id[5];
                        profileURL.setText(imageLink);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Invalid Image URL.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    profileURL.setText("https://drive.google.com/uc?export=view&id=default");
                }
                if(!FirebaseHelper.isConnected(RegisterActivity.this)){
                    helpMessage.setText("You are not online. Try again");
                    return;
                }
                if(dbHelper.getLoginInfo(studentID.getText().toString())!=null && uid.equals("")){
                    helpMessage.setText("This student registered before.");
                    return;
                }
                if(lastDonationDate.getText().toString().equals("")){
                    lastDonationDate.setText("01/01/2022");
                }else{
                    //if(lastDonationDate.getText().toString().length()!=10)
                }

                if(weight.getText().toString().equals("")){
                    weight.setText("58");
                }
                if(heightfit.getText().toString().equals("")){
                    heightfit.setText("5");
                }
                if(heightInch.getText().toString().equals("")){
                    heightInch.setText("4");
                }

                if(uid.equals("")){
                    uid = String.valueOf(Long.MAX_VALUE-new Date().getTime());
                }else{
                    dbHelper.deleteUser(uid);
                    Toast.makeText(RegisterActivity.this, "Deleted previous information.    ", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(2000);
                        //Toast.makeText(RegisterActivity.this, "Sleep finished", Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int height = Integer.parseInt(heightfit.getText().toString())*12+Integer.parseInt(heightInch.getText().toString());

                UserModel user = new UserModel(uid,profileURL.getText().toString(),name.getText().toString(),
                        sBloodGroup,lastDonationDate.getText().toString(),Integer.parseInt(numberOfDonation.
                        getText().toString()),phone.getText().toString(),"Never",Float.parseFloat(weight.getText().toString()),
                        height,Integer.parseInt(studentID.getText().toString()),curAddress.getText().toString(),
                        parAddress.getText().toString(),new StringBuilder());

                boolean parSuccess =  dbHelper.insertUser(user,false);

                LoginInfo loginInfo = new LoginInfo(user.getStudentId()+"",password.getText().toString(),user.getUid());
                dbHelper.insertLoginInfo(loginInfo,false);

                if(parSuccess){
                    Toast.makeText(RegisterActivity.this, "Data insert success.", Toast.LENGTH_SHORT).show();

                    SharedPreferences sp = getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("uid",user.getUid());
                    editor.putString("Psw",password.getText().toString());
                    editor.commit();

                    UserSelf mySelf = UserSelf.getUserSelf(user);
                    //FirebaseHelper.register(user,userAddInfo,password.getText().toString());
                    progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setTitle("Register");
                    progressDialog.setMessage("Wait few moment to Register.");
                    progressDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //your code here
                            Toast.makeText(RegisterActivity.this, "Register success.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    }, 5000);
                }else{
                    Toast.makeText(RegisterActivity.this, "Data insert Fail.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                sBloodGroup = "";
                break;
            case 1:
                sBloodGroup = "A+";
                break;
            case 2:
                sBloodGroup = "A-";
                break;
            case 3:
                sBloodGroup = "B+";
                break;
            case 4:
                sBloodGroup = "B-";
                break;
            case 5:
                sBloodGroup = "O+";
                break;
            case 6:
                sBloodGroup = "O-";
                break;
            case 7:
                sBloodGroup = "AB+";
                break;
            case 8:
                sBloodGroup = "AB-";
                break;
            default:
                Toast.makeText(RegisterActivity.this, "Default. ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(RegisterActivity.this, "Nothing selected.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}