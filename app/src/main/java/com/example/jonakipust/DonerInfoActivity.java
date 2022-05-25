package com.example.jonakipust;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.Model.UserAdditionalInfo;
import com.example.jonakipust.Model.UserModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonerInfoActivity extends AppCompatActivity {

    CircleImageView donerProfile;
    TextView donerName,bloodGroup,lastDonationDate,numberOfDonation,phone,bmi,weight,height,studentID,parAddress,curAddress;
    ImageButton call;
    RecyclerView donationHistoryRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doner_info);

        donerProfile = findViewById(R.id.civ_doner_profile_image);
        donerName = findViewById(R.id.tv_doner_user_name);
        bloodGroup = findViewById(R.id.tv_doner_blood_group);
        lastDonationDate = findViewById(R.id.tv_doner_last_donation_date);
        numberOfDonation = findViewById(R.id.tv_doner_number_of_donation);
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
        UserAdditionalInfo userAddInfo = dbHelper.getUserAdditionalInfoByUID(donerUID);


        Picasso.get().load(user.getProfile()).placeholder(R.drawable.ic_user).into(donerProfile);
        donerName.setText(user.getName());
        bloodGroup.setText(user.getBloodGroup());
        lastDonationDate.setText(user.getLastDonationDate());
        numberOfDonation.setText(user.getNumberOfDonation()+" Times");
        phone.setText(user.getPhone());
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Calling : "+phone.getText().toString(), Toast.LENGTH_SHORT).show();
                if(ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.CALL_PHONE},
                            Activity.RESULT_FIRST_USER);
                }else{
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone.getText().toString()));
                    startActivity(intent);
                }
            }
        });
        bmi.setText(userAddInfo.getBmi());
        weight.setText(userAddInfo.getWeight()+"");
        height.setText(userAddInfo.getHeightString());
        studentID.setText(userAddInfo.getStudentId()+"");
        parAddress.setText(userAddInfo.getParmanentAddress());
        curAddress.setText(userAddInfo.getCurrentAddress());

    }
}