package com.example.jonakipust.Database;

import static android.content.ContentValues.TAG;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.jonakipust.Model.LoginInfo;
import com.example.jonakipust.Model.Post.PostModel;
import com.example.jonakipust.Model.UserAdditionalInfo;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {
    private static final FirebaseDatabase database = getInstance();
    private static DatabaseReference myRef;
    public static boolean alldownloaded;
    public static boolean loginInfoDownloaded;
    public static MainDBHelper dbHelper;


    public static boolean isConnected(Context context) {
        myRef = database.getReference().child("message");
       // myRef.setValue("Hello bb");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "-----------------Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

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
                Log.d(TAG,"number of loginInfo is : "+snapshot.getChildrenCount());
                for(DataSnapshot ds : snapshot.getChildren()){
                    LoginInfo loginInfo = ds.getValue(LoginInfo.class);
                    assert loginInfo != null;
                    dbHelper.insertLoginInfo(loginInfo,true);
                    Log.d(TAG,"------------info are : "+loginInfo.getUID()+"  "+loginInfo.getStudentID()+"  "+loginInfo.getPassword());
                }
                Log.d(TAG,"finished login info -----------------");
                loginInfoDownloaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"NOT found login info -----------------");
            }
        });
    }

    public static void downloadAllData(Context context){
        alldownloaded = false;
        dbHelper = new MainDBHelper(context);
        myRef = database.getReference().child("Datas").child("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"geting User size is : "+snapshot.getChildrenCount());
                for(DataSnapshot ds : snapshot.getChildren()){
                    UserModel user = ds.getValue(UserModel.class);
                    dbHelper.insertUser(user,true);
                    Log.d(TAG,"inserted user to db : "+user.getName());
                }
                Log.d(TAG,"finished user-----------------");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"NOT found login info -----------------");
            }
        });

        myRef = database.getReference().child("Datas").child("AdditionalInfo");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"geting addi size is : "+snapshot.getChildrenCount());
                for(DataSnapshot ds : snapshot.getChildren()){
                    UserAdditionalInfo userAddi = ds.getValue(UserAdditionalInfo.class);
                    dbHelper.insertAdditionalInfo(userAddi,true);
                    Log.d(TAG,"inserted addi to db : "+userAddi.getStudentId());
                }
                Log.d(TAG,"finished addi-----------------");
                alldownloaded = true;
                Toast.makeText(context, "All data downloaded success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"NOT found login info -----------------");
            }
        });
    }

    public static void register(UserModel user, UserAdditionalInfo
            additionalInfo, String psw){

        myRef = database.getReference().child("LOGIN").child(additionalInfo.getStudentId()+"");
        myRef.setValue(new LoginInfo(user.getUid(),psw,additionalInfo.getStudentId()+""));

        myRef = database.getReference().child("Datas").child("User").child(user.getUid());
        myRef.setValue(user);

        myRef = database.getReference().child("Datas").child("AdditionalInfo").child(user.getUid());
        myRef.setValue(additionalInfo);
    }

    public static void post(PostModel post){
        myRef = database.getReference().child("Datas").child("Post").child(post.getUid());
        myRef.setValue(post);
    }


}
