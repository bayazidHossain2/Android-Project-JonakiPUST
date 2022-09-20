package com.example.jonakipust;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonakipust.Adapters.HistoryAdapter;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.Model.DonationHistory.DonationHistoryModel;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserSelf;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonerInfoActivity extends AppCompatActivity {

    CircleImageView donerProfile;
    TextView donerName,bloodGroup,lastDonationDate,numberOfDonation,lastContuct,phone,bmi,
            weight,height,studentID,parAddress,curAddress;
    ImageButton call;
    RecyclerView donationHistoryRV;
    ArrayList<DonationHistoryModel> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doner_info);

        donerProfile = findViewById(R.id.civ_doner_profile_image);
        donerName = findViewById(R.id.tv_doner_user_name);
        bloodGroup = findViewById(R.id.tv_doner_blood_group);
        lastDonationDate = findViewById(R.id.tv_doner_last_donation_date);
        numberOfDonation = findViewById(R.id.tv_doner_number_of_donation);
        lastContuct = findViewById(R.id.tv_doner_last_contact_date);
        phone = findViewById(R.id.tv_doner_user_phone);
        call = findViewById(R.id.btn_call_doner);
        bmi = findViewById(R.id.tv_doner_bmi);
        weight = findViewById(R.id.tv_doner_weight);
        height = findViewById(R.id.tv_doner_height);
        studentID = findViewById(R.id.tv_doner_student_id);
        parAddress = findViewById(R.id.tv_doner_parmanent_address);
        curAddress = findViewById(R.id.tv_doner_current_address);
        donationHistoryRV = findViewById(R.id.rv_doner_donation_history);

        String donerUID = getIntent().getStringExtra("uid");

        MainDBHelper dbHelper = new MainDBHelper(DonerInfoActivity.this);

        UserModel user = dbHelper.getUserByUID(donerUID);


        Picasso.get().load(user.getProfile()).placeholder(R.drawable.ic_user).into(donerProfile);
        donerName.setText(user.getName());
        bloodGroup.setText(user.getBloodGroup());
        lastDonationDate.setText(user.getLastDonationDate());
        numberOfDonation.setText(user.getNumberOfDonation()+" Times");
        lastContuct.setText(user.getLastContact());
        phone.setText(user.getPhone());
        call.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Calling : "+phone.getText().toString(), Toast.LENGTH_SHORT).show();
                if(ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.
                                    permission.CALL_PHONE}, Activity.RESULT_FIRST_USER);

                }else{
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    user.setLastContact(dateTimeFormatter.format(LocalDateTime.now()));
                    lastContuct.setText(user.getLastContact());
                    Toast.makeText(DonerInfoActivity.this,"Last call : "+user.getLastContact()
                            ,Toast.LENGTH_SHORT).show();
                    dbHelper.insertUser(user,false);

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone.getText().toString()));
                    startActivity(intent);
                }
            }
        });
        bmi.setText(user.getBmi());
        weight.setText(user.getWeight()+"");
        height.setText(user.getHeightString());
        studentID.setText(user.getStudentId()+"");
        parAddress.setText(user.getParmanentAddress());
        curAddress.setText(user.getCurrentAddress());


        historyList = dbHelper.getDonationHistoryList(user.getUid());
        if(historyList == null){
            historyList = new ArrayList<>();
        }
        HistoryAdapter historyAdapter = new HistoryAdapter(DonerInfoActivity.this,historyList);
        donationHistoryRV.setAdapter(historyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DonerInfoActivity.this);
        donationHistoryRV.setLayoutManager(layoutManager);
    }
}