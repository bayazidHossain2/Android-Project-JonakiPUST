package com.example.jonakipust.Fragments.Nav_Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonakipust.Adapters.HistoryAdapter;
import com.example.jonakipust.CreateHistoryActivity;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.MainActivity;
import com.example.jonakipust.Model.DonationHistory.DonationHistoryModel;
import com.example.jonakipust.Model.UserSelf;
import com.example.jonakipust.R;
import com.example.jonakipust.RegisterActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

    CircleImageView userProfile;
    FloatingActionButton profileEdit;
    TextView bloodGroup,lastDonationDate,userName,numberOfDonation,phoneNumber;
    TextView userBMI,userWeight,userHeight,userStudentID,curAddress,parAddress,addHistory;
    RecyclerView userDonationHistory;
    UserSelf mySelf;
    ArrayList<DonationHistoryModel> historyList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userProfile = view.findViewById(R.id.civ_user_profile_image);
        profileEdit = view.findViewById(R.id.fab_edit_profile);
        bloodGroup = view.findViewById(R.id.tv_user_blood_group);
        lastDonationDate = view.findViewById(R.id.tv_user_last_donation_date);
        userName = view.findViewById(R.id.tv_user_name);
        numberOfDonation = view.findViewById(R.id.tv_user_number_of_donation);
        phoneNumber = view.findViewById(R.id.tv_user_phone);
        userBMI = view.findViewById(R.id.tv_user_bmi);
        userWeight = view.findViewById(R.id.tv_user_weight);
        userHeight = view.findViewById(R.id.tv_user_height);
        userStudentID = view.findViewById(R.id.tv_user_student_id);
        curAddress = view.findViewById(R.id.tv_user_current_address);
        parAddress = view.findViewById(R.id.tv_user_parmanent_address);
        userDonationHistory = view.findViewById(R.id.rv_user_donation_history);
        addHistory = view.findViewById(R.id.tv_user_add_donation_history);
        mySelf = UserSelf.getUserSelf();

        Picasso.get().load(mySelf.getUserModel().getProfile()).placeholder(R.drawable.ic_user).into(userProfile);
//        Toast.makeText(view.getContext(),"profile : "+mySelf.getUserModel().getProfile(),Toast.LENGTH_LONG).show();
//        //this is the original Google Drive link to the image
//        String s=UserSelf.getUserSelf().getUserModel().getProfile();
//
//        //you have to get the part of the link 0B9nFwumYtUw9Q05WNlhlM2lqNzQ
//        String[] p=s.split("/");
//        //Create the new image link
//        String imageLink="https://drive.google.com/uc?export=view&id="+p[5];
//        //ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        //Picasso.with(YourActivity.this).load(imageLink).into(imageView);
//        Picasso.get().load(imageLink).into(userProfile);
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Edit profile clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                intent.putExtra("uid",UserSelf.getUserSelf().getUserModel().getUid());
                startActivity(intent);
            }
        });
        bloodGroup.setText(mySelf.getUserModel().getBloodGroup());
        lastDonationDate.setText(mySelf.getUserModel().getLastDonationDate());
        userName.setText(mySelf.getUserModel().getName());
        numberOfDonation.setText(mySelf.getUserModel().getNumberOfDonation()+" times");
        phoneNumber.setText(mySelf.getUserModel().getPhone());
        userBMI.setText(mySelf.getUserModel().getBmi());
        userWeight.setText(mySelf.getUserModel().getWeight()+" KG");
        userHeight.setText(mySelf.getUserModel().getHeightString());
        userStudentID.setText(mySelf.getUserModel().getStudentId()+"");
        curAddress.setText(mySelf.getUserModel().getCurrentAddress());
        parAddress.setText(mySelf.getUserModel().getParmanentAddress());
        addHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Add history clicked.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), CreateHistoryActivity.class);
                startActivity(intent);
            }
        });
        MainDBHelper dbHelper = new MainDBHelper(view.getContext());

        userDonationHistory = view.findViewById(R.id.rv_user_donation_history);
        historyList = dbHelper.getDonationHistoryList(UserSelf.getUserSelf().getUserModel().getUid());
        if(historyList == null){
            historyList = new ArrayList<>();
        }
        HistoryAdapter historyAdapter = new HistoryAdapter(view.getContext(),historyList);
        userDonationHistory.setAdapter(historyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        userDonationHistory.setLayoutManager(layoutManager);
    }
}