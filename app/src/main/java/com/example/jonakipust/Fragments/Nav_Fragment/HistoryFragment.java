package com.example.jonakipust.Fragments.Nav_Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonakipust.Adapters.HistoryAdapter;
import com.example.jonakipust.Adapters.Post.PostAdapter;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.Model.DonationHistory.DonationHistoryModel;
import com.example.jonakipust.R;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    RecyclerView history;
    ArrayList<DonationHistoryModel> historyList;
    static FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainDBHelper dbHelper = new MainDBHelper(view.getContext());

        history = view.findViewById(R.id.rv_history);
        historyList = dbHelper.getDonationHistoryList("");
        if(historyList == null){
            historyList = new ArrayList<>();
        }
        HistoryAdapter historyAdapter = new HistoryAdapter(view.getContext(),historyList);
        history.setAdapter(historyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        history.setLayoutManager(layoutManager);
        fragmentTransaction = getParentFragmentManager().beginTransaction();
    }

    public static void resetHistory(){
        fragmentTransaction.replace(R.id.container,new HistoryFragment());
        fragmentTransaction.commit();
    }
}