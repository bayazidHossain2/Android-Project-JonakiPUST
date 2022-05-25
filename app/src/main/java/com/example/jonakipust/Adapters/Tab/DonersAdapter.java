package com.example.jonakipust.Adapters.Tab;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jonakipust.DonerInfoActivity;
import com.example.jonakipust.Model.UserAdditionalInfo;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonersAdapter extends RecyclerView.Adapter<DonersAdapter.viewHolder>{

    Context context;
    ArrayList<UserModel> donerList;

    public DonersAdapter(Context context,ArrayList<UserModel> donerList){
        this.context = context;
        this.donerList = donerList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(context).inflate(R.layout.doner_sample_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        UserModel donerModel = donerList.get(position);

        Picasso.get().load(donerModel.getProfile()).placeholder(R.drawable.ic_user).into(holder.donerProfile);
        holder.donerName.setText(donerModel.getName());
        holder.donerBloodGroup.setText(donerModel.getBloodGroup());
        holder.lastDonationDate.setText(donerModel.getLastDonationDate());
        holder.numberOfDonation.setText(donerModel.getNumberOfDonation()+" th");
        holder.donerPhoneNumber.setText(donerModel.getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DonerInfoActivity.class);
                intent.putExtra("uid",donerModel.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donerList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        CircleImageView donerProfile;
        TextView donerName, donerBloodGroup, lastDonationDate, numberOfDonation, donerPhoneNumber;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            donerProfile = itemView.findViewById(R.id.profile_image);
            donerName = itemView.findViewById(R.id.tv_donerName);
            donerBloodGroup = itemView.findViewById(R.id.tv_bloodGroup);
            lastDonationDate = itemView.findViewById(R.id.tv_lastDonateDate);
            numberOfDonation = itemView.findViewById(R.id.tv_numberOfDonation);
            donerPhoneNumber = itemView.findViewById(R.id.tv_phone);
        }
    }
}
