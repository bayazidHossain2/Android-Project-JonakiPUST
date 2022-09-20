package com.example.jonakipust.Database;

import static android.content.ContentValues.TAG;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.jonakipust.Model.DonationHistory.DonationHistoryModel;
import com.example.jonakipust.Model.LoginInfo;
import com.example.jonakipust.Model.Post.Comment.CommentModel;
import com.example.jonakipust.Model.Post.PostModel;
import com.example.jonakipust.Model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {
    private static final FirebaseDatabase database = getInstance();
    private static DatabaseReference myRef;
    public static boolean alldownloaded;
    public static boolean loginInfoDownloaded;
    public static MainDBHelper dbHelper;
    public static String securityCode = null;


    public static boolean isConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if(infos != null){
                for(int i=0;i < infos.length; i++){
                    if(infos[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }return false;
    }

    public static void loadLoginInfo(Context context){
        loginInfoDownloaded = false;
        if(dbHelper == null){
            dbHelper = new MainDBHelper(context);
        }
        myRef = database.getReference().child("LOGIN");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d(TAG,"number of loginInfo is : "+snapshot.getChildrenCount());
                for(DataSnapshot ds : snapshot.getChildren()){
                    LoginInfo loginInfo = ds.getValue(LoginInfo.class);
                    assert loginInfo != null;
                    dbHelper.insertLoginInfo(loginInfo,true);
                    //Log.d(TAG,"------------info are : "+loginInfo.getUID()+"  "+loginInfo.getStudentID()+"  "+loginInfo.getPassword());
                }
                //Log.d(TAG,"finished download login info -----------------");
                loginInfoDownloaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.d(TAG,"NOT found login info -----------------");
            }
        });
    }

    public static void getSecurityCode(String what){
        myRef = database.getReference().child("SecurityCode").child(what);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                securityCode = String.valueOf(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.d(TAG,"NOT found "+what+" security code. -----------------");
            }
        });

    }

    public static void loadUserInfo(Context context){
        if(dbHelper == null){
            dbHelper = new MainDBHelper(context);
        }
        alldownloaded = false;
        myRef = database.getReference().child("Datas").child("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d(TAG,"getting User size is : "+snapshot.getChildrenCount());
                for(DataSnapshot ds : snapshot.getChildren()){
                    UserModel user = ds.getValue(UserModel.class);
                    dbHelper.insertUser(user,true);
                    //Log.d(TAG,"inserted user to db : "+user.getName());
                }
                alldownloaded = true;
                Toast.makeText(context,"All User info update success.",Toast.LENGTH_SHORT).show();
                //Log.d(TAG,"finished user-----------------");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.d(TAG,"NOT found login info -----------------");
            }
        });
    }

    public static void loadPost(Context context){
        dbHelper = new MainDBHelper(context);
        dbHelper.deleteAllPosts();

        myRef = database.getReference().child("Datas").child("Post");
        alldownloaded = false;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d(TAG,"getting Posts size is : "+snapshot.getChildrenCount());
                for(DataSnapshot ds : snapshot.getChildren()){
                    PostModel post = ds.getValue(PostModel.class);
                    dbHelper.insertPost(post,true);
                    //Log.d(TAG,"inserted post to db : "+post.getUid());
                }
                Toast.makeText(context,"Find new Post.",Toast.LENGTH_SHORT).show();
                //Log.d(TAG,"finished post load-----------------");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.d(TAG,"NOT found any post -----------------");
            }
        });

        myRef = database.getReference().child("Datas").child("Comments");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d(TAG,"getting Comments size is : "+snapshot.getChildrenCount());
                for(DataSnapshot ds : snapshot.getChildren()){
                    CommentModel comment = ds.getValue(CommentModel.class);
                    dbHelper.insertComment(comment,true);
                    //Log.d(TAG,"inserted comment to db : "+comment.getUid());
                }
                alldownloaded = true;
                //Log.d(TAG,"finished comment load----------------");
                Toast.makeText(context,"Find new comment.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.d(TAG,"NOT found login info -----------------");
            }
        });
    }

    public static void loadDonation(Context context) {
        dbHelper = new MainDBHelper(context);

        myRef = database.getReference().child("Datas").child("DonationHistory");
        alldownloaded = false;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d(TAG,"getting donation history size is : "+snapshot.getChildrenCount());
                for(DataSnapshot ds : snapshot.getChildren()){
                    DonationHistoryModel history = ds.getValue(DonationHistoryModel.class);
                    dbHelper.insertDonationHistory(history,true);
                    //Log.d(TAG,"inserted history to db : "+history.getUid());
                    alldownloaded = true;
                }
                Toast.makeText(context,"Find new History.",Toast.LENGTH_SHORT).show();
                //Log.d(TAG,"finished user-----------------");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.d(TAG,"NOT found login info -----------------");
            }
        });
    }

    public static void reset(Context context,boolean isDelete){
        if(isDelete) {
            dbHelper = new MainDBHelper(context);
            dbHelper.deleteAll();
        }
        loadUserInfo(context);
    }
}
