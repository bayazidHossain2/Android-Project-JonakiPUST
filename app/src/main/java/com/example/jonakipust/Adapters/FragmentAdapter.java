package com.example.jonakipust.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jonakipust.Fragments.Tab_Fragment.DonerFragment;

public class FragmentAdapter extends FragmentStateAdapter{

    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new DonerFragment("All");
            case 1:
                return new DonerFragment("A+");
            case 2:
                return new DonerFragment("A-");
            case 3:
                return new DonerFragment("B+");
            case 4:
                return new DonerFragment("B-");
            case 5:
                return new DonerFragment("O+");
            case 6:
                return new DonerFragment("O-");
            case 7:
                return new DonerFragment("AB+");
            case 8:
                return new DonerFragment("AB-");
            default:
                return new DonerFragment("All");
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }
}