package com.example.jonakipust.Fragments.Tab_Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jonakipust.Adapters.Tab.DonersAdapter;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.R;

import java.util.ArrayList;

public class DonerFragment extends Fragment {
    private String bloodGroup;
    private TextView tv;
    RecyclerView doners;

    public DonerFragment(String bloodGroup){
        this.bloodGroup = bloodGroup;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tv = view.findViewById(R.id.textView);

        doners = view.findViewById(R.id.rv_doners);
        MainDBHelper dbHelper = new MainDBHelper(view.getContext());

        ArrayList<UserModel> donerList = dbHelper.getUsersByBloodGroup(bloodGroup);
        if(donerList == null){
            tv.setText("You are in : "+bloodGroup+" recycler view is null.");
        }else {
            DonersAdapter donersAdapter = new DonersAdapter(view.getContext(), donerList);
            doners.setAdapter(donersAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            doners.setLayoutManager(layoutManager);
        }
    }
}