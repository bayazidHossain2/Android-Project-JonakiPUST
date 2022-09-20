package com.example.jonakipust.Adapters;

import static android.content.ContentValues.TAG;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jonakipust.Adapters.Post.PostAdapter;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.DonerInfoActivity;
import com.example.jonakipust.Fragments.Nav_Fragment.PostFragment;
import com.example.jonakipust.Model.DonationHistory.DonationHistoryModel;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserSelf;
import com.example.jonakipust.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.viewHolder>{
    Context context;
    ArrayList<DonationHistoryModel> list;

    public HistoryAdapter(Context context,ArrayList<DonationHistoryModel> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donation_history_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MainDBHelper dbHelper = new MainDBHelper(context);
        DonationHistoryModel model = list.get(position);
        UserModel doner = dbHelper.getUserByUID(model.getDonerUid());
        if(doner == null){
            Log.d(TAG,"NOT found "+model.getDonerUid()+" User. -----------------");
            Toast.makeText(context,"User not found Uid : "+model.getDonerUid(),Toast.LENGTH_SHORT).show();
            return;
        }
        Picasso.get().load(doner.getProfile()).placeholder(R.drawable.ic_user).into(holder.donerProfile);
        holder.donerName.setText(doner.getName());
        holder.donerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DonerInfoActivity.class);
                intent.putExtra("uid", model.getDonerUid());
                context.startActivity(intent);
            }
        });
        holder.donationDate.setText(model.getDonationDate());
        holder.donationInfo.setText(model.getShortDisc());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.more);
                popupMenu.inflate(R.menu.menu_comment);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.comment_menu_delete:
                                if(UserSelf.getUserSelf().getUserModel().getUid().equals(doner.getUid())){
                                    dbHelper.deleteHistory(model.getUid());
                                    try{
                                        Thread.sleep(2000);
                                        //PostFragment.resetPosts();
                                    }catch (Exception ex){
                                        Log.d(TAG,ex.getMessage());
                                    }
                                    Toast.makeText(context, "Donation History delete success.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "You are not owner of the Donation History.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView donerProfile,more;
        TextView donerName,donationDate,donationInfo;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            donerProfile = itemView.findViewById(R.id.iv_user_doner_profile);
            more = itemView.findViewById(R.id.iv_user_doner_more);
            donerName = itemView.findViewById(R.id.tv_user_doner_name);
            donationDate = itemView.findViewById(R.id.tv_user_doner_donation_date);
            donationInfo = itemView.findViewById(R.id.tv_user_doner_donation_info);
        }
    }
}
