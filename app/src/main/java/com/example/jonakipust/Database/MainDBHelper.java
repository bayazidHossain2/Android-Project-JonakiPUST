package com.example.jonakipust.Database;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.jonakipust.LoginActivity;
import com.example.jonakipust.MainActivity;
import com.example.jonakipust.Model.DonationHistory.DonationHistoryModel;
import com.example.jonakipust.Model.LoginInfo;
import com.example.jonakipust.Model.Post.Comment.CommentModel;
import com.example.jonakipust.Model.Post.PostModel;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserShortModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainDBHelper extends SQLiteOpenHelper {

    static final String NAME = "JonakiPustDB";
    static final int VERSION = 23;
    Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    public MainDBHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
        firebaseDatabase = getInstance();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "Create table Users"+
                        "(uid text primary key,"+
                        "profile text," +
                        "name text," +
                        "bloodGroup text," +
                        "lastDonationDate text," +
                        "numberOfDonation int," +
                        "phone text,"+
                        "lastContuct text,"+
                        "weight float,"+
                        "height int,"+
                        "studentID int,"+
                        "curAddress text,"+
                        "parAddress text," +
                        "donationHistoryUID text)"
        );

        sqLiteDatabase.execSQL(
                "create table LoginInfo" +
                        "(studentId text primary key," +
                        "password text," +
                        "uid text)"
        );

        sqLiteDatabase.execSQL(
                "create table Posts" +
                        "(uid text primary key," +
                        "postWriterUID text," +
                        "postWritingTime text," +
                        "post text," +
                        "managed int," +
                        "commentUID text," +
                        "online int)"
        );

        sqLiteDatabase.execSQL(
                "create table Comments" +
                        "(uid text primary key," +
                        "commentWriterUID text," +
                        "commentWritingTime text," +
                        "comment text," +
                        "postUID text," +
                        "online int)"
        );

        sqLiteDatabase.execSQL(
                "create table DonationHistory" +
                        "(uid text primary key," +
                        "donerUID text," +
                        "donationDate text," +
                        "shortDesc text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists Users");
        sqLiteDatabase.execSQL("drop table if exists Posts");
        sqLiteDatabase.execSQL("drop table if exists LoginInfo");
        sqLiteDatabase.execSQL("drop table if exists Comments");
        sqLiteDatabase.execSQL("drop table if exists DonationHistory");

        onCreate(sqLiteDatabase);
    }

    public void deleteAll(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.execSQL("delete from Users");
        sqLiteDatabase.execSQL("delete from LoginInfo");
        sqLiteDatabase.execSQL("delete from Posts");
        sqLiteDatabase.execSQL("delete from Comments");
        sqLiteDatabase.execSQL("delete from DonationHistory");
        SharedPreferences sp = context.getSharedPreferences("Login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("uid",null);
        editor.putString("Psw",null);
        editor.commit();
    }

    public boolean insertUser(UserModel user,boolean isDownloaded){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid",user.getUid());
        values.put("profile",user.getProfile());
        values.put("name",user.getName());
        values.put("bloodGroup",user.getBloodGroup());
        values.put("lastDonationDate",user.getLastDonationDate());
        values.put("numberOfDonation",user.getNumberOfDonation());
        values.put("phone",user.getPhone());
        values.put("lastContuct",user.getLastContact());
        values.put("weight",user.getWeight());
        values.put("height",user.getHeight());
        values.put("studentID",user.getStudentId());
        values.put("curAddress",user.getCurrentAddress());
        values.put("parAddress",user.getParmanentAddress());
        values.put("donationHistoryUID",user.getDonationHistoryUID());

        database.delete("Users","uid="+user.getUid(),null);

        long pushed = database.insert("Users",null,values);

        if(pushed<=0){
            return false;
        }else{
            if(!isDownloaded) {
                myRef = firebaseDatabase.getReference().child("Datas").child("User").child(user.getUid());
                myRef.setValue(user);
            }
            return true;
        }
    }

    public void deleteUser(String uid){
        SQLiteDatabase database = getWritableDatabase();
        database.delete("Users","uid="+uid,null);
        firebaseDatabase.getReference().child("Datas").child("User").child(uid).removeValue();
    }

    public UserModel getUserByUID(String uid){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from Users where uid="+uid,null);
        UserModel user;
        if(cursor.moveToFirst()){
            user = new UserModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4), cursor.getInt(5),cursor.getString(6),
                    cursor.getString(7),cursor.getDouble(8),cursor.getInt(9),cursor.getInt(10),
                    cursor.getString(11),cursor.getString(12),new StringBuilder(cursor.getString(13)));
        }else{
            user = null;
        }
        cursor.close();
        database.close();
        return user;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<UserShortModel> getUsersByBloodGroup(String bloodGroup){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor;
        if(bloodGroup.equals("All")){
            cursor = database.rawQuery("select uid,name,bloodGroup,lastDonationDate,numberOfDonation," +
                    "lastContuct,profile from Users", null);
        }else {
            cursor = database.rawQuery("select uid,name,bloodGroup,lastDonationDate,numberOfDonation," +
                    "lastContuct,profile  from Users where bloodGroup='" + bloodGroup+"'", null);
        }

        ArrayList<UserShortModel> userList,unpeaperdList;

        if(cursor.moveToFirst()){
            userList = new ArrayList<>();
            unpeaperdList = new ArrayList<>();
            do{
                UserShortModel shortModel = new UserShortModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),cursor.getInt(4),cursor.getString(5),cursor.getString(6));
                if(shortModel.isPrepared()){
                    userList.add(shortModel);
                }else{
                    unpeaperdList.add(shortModel);
                }
            }while(cursor.moveToNext());
            userList.addAll(unpeaperdList);
        }else{
            userList = null;
        }
        cursor.close();
        database.close();
        
        return userList;
    }

    public void insertLoginInfo(LoginInfo loginInfo, boolean isDownloaded){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("studentId",loginInfo.getStudentID());
        values.put("password",loginInfo.getPassword());
        values.put("uid",loginInfo.getUID());

        database.delete("LoginInfo","studentId="+loginInfo.getStudentID(),null);
        database.insert("LoginInfo",null,values);
        if(!isDownloaded){
            myRef = firebaseDatabase.getReference().child("LOGIN").child(loginInfo.getStudentID());
            myRef.setValue(loginInfo);
        }
    }

    public LoginInfo getLoginInfo(String studentID){
        LoginInfo loginInfo = null;

        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from LoginInfo where studentId="+studentID,null);
        if(cursor.moveToFirst()){
            loginInfo = new LoginInfo(cursor.getString(0),cursor.getString(1),cursor.getString(2));
        }
        cursor.close();
        database.close();

        return loginInfo;
    }

    public boolean insertPost(PostModel postModel, boolean isDownloaded){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid",postModel.getUid());
        values.put("postWriterUID",postModel.getPostWriterUID());
        values.put("postWritingTime",postModel.getPostWritingTime());
        values.put("post",postModel.getPost());
        values.put("managed",postModel.getManaged());
        values.put("commentUID",postModel.getCommentUID());
        values.put("online",0);

        database.delete("Posts","uid="+postModel.getUid(),null);

        long pushed = database.insert("Posts",null,values);

        if(pushed<=0){
            return false;
        }else{
            if(!isDownloaded) {
                myRef = firebaseDatabase.getReference().child("Datas").child("Post").child(postModel.getUid());
                myRef.setValue(postModel);
            }
            return true;
        }
    }

    public void deletePost(String uid){
        PostModel post = getPostByUID(uid);
        Log.d(TAG,"Deleting post comments are : "+post.getCommentUID());
        String[] commentUIDs = post.getCommentUID().split(" ");
        for(String comment : commentUIDs){
            deleteComment(comment.trim(),"");
            Log.d(TAG,"Delete comment : "+comment);
        }
        SQLiteDatabase database = getWritableDatabase();
        database.delete("Posts","uid="+uid,null);
        firebaseDatabase.getReference().child("Datas").child("Comments").child(post.getUid()).removeValue();
        myRef = firebaseDatabase.getReference().child("Datas").child("Post").child(uid);
        myRef.removeValue();
    }
    public boolean updatePost(PostModel postModel){
        SQLiteDatabase database = getWritableDatabase();
        database.delete("Posts","uid="+postModel.getUid(),null);
        return insertPost(postModel,false);
    }

    public PostModel getPostByUID(String uid){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from Posts where uid="+uid,null);
        PostModel post;
        if(cursor.moveToFirst()){
            post = new PostModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getInt(4), cursor.getString(5));
        }else{
            post = new PostModel();
        }
        cursor.close();
        database.close();
        return post;
    }

    public ArrayList<PostModel> getPostList(){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from Posts",null);
        ArrayList<PostModel> postList;
        postList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                postList.add(new PostModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),cursor.getInt(4), cursor.getString(5)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        database.close();

        ArrayList<PostModel> rPostList = new ArrayList<>();
        int lastPos = postList.size()-1;
        for(;lastPos>=0;lastPos--){
            rPostList.add(postList.get(lastPos));
        }
        return rPostList;
    }
    public void deleteAllPosts(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.execSQL("delete from Posts");
        sqLiteDatabase.execSQL("delete from Comments");
    }

    public boolean insertComment(CommentModel commentModel, boolean isDownloaded){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid",commentModel.getUid());
        values.put("commentWriterUID",commentModel.getCommentWriterUID());
        values.put("commentWritingTime",commentModel.getCommentWritingTime());
        values.put("comment",commentModel.getComment());
        values.put("postUID",commentModel.getPostUID());
        values.put("online",0);

        database.delete("Comments","uid="+commentModel.getUid(),null);
        long pushed = database.insert("Comments",null,values);

        if(pushed<=0){
            return false;
        }else{
            if(!isDownloaded) {
                myRef = firebaseDatabase.getReference().child("Datas").child("Comments").child(commentModel.getUid());
                myRef.setValue(commentModel);
            }
            return true;
        }
    }

    public void deleteComment(String cuid,String puid){
        SQLiteDatabase database = getWritableDatabase();
        if(cuid.isEmpty()){
            cuid = "101";
        }
        database.delete("Comments","uid="+cuid,null);
        if(!puid.isEmpty()){
            firebaseDatabase.getReference().child("Datas").child("Comments").child(puid).child(cuid).removeValue();
        }
        Log.d(TAG,"Deleting comment success : "+cuid);
    }

    public CommentModel getCommentByUID(String uid){

        if(uid.isEmpty()){
            return null;
        }
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from Comments where uid="+uid,null);
        CommentModel comment;
        if(cursor.moveToFirst()){
            comment = new CommentModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4));
        }else{
            comment = null;
        }
        cursor.close();
        database.close();
        return comment;
    }

    public boolean insertDonationHistory(DonationHistoryModel donationHistory, boolean isDownloaded){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid",donationHistory.getUid());
        values.put("donerUID",donationHistory.getDonerUid());
        values.put("donationDate",donationHistory.getDonationDate());
        values.put("shortDesc",donationHistory.getShortDisc());

        database.delete("DonationHistory","uid="+donationHistory.getUid(),null);

        long pushed = database.insert("DonationHistory",null,values);

        if(pushed<=0){
            return false;
        }else{
            if(!isDownloaded) {
                myRef = firebaseDatabase.getReference().child("Datas").child("DonationHistory").
                        child(donationHistory.getUid());
                myRef.setValue(donationHistory);
            }
            return true;
        }
    }

    public void deleteHistory(String uid){
        SQLiteDatabase database = getWritableDatabase();
        if(uid.isEmpty()){
            return;
        }
        database.delete("DonationHistory","uid="+uid,null);
        if(!uid.isEmpty()){
            firebaseDatabase.getReference().child("Datas").child("DonationHistory").child(uid).removeValue();
        }
        Log.d(TAG,"Deleting History success : "+uid);
    }

    public ArrayList<DonationHistoryModel> getDonationHistoryList(String donerUID){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor;
        if(donerUID.equals("")) {
            cursor = database.rawQuery("select * from DonationHistory", null);
        }else{
            cursor = database.rawQuery("select * from DonationHistory where donerUID="+donerUID, null);
        }
        ArrayList<DonationHistoryModel> historyList;
        if(cursor.moveToFirst()){
            historyList = new ArrayList<>();
            do{
                historyList.add(new DonationHistoryModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3)));
            }while(cursor.moveToNext());
            //Collections.sort(historyList);
        }else{
            historyList = null;
        }
        cursor.close();
        database.close();
        return historyList;
    }

}
