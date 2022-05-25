package com.example.jonakipust.Database;

import static android.content.ContentValues.TAG;
import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.jonakipust.Model.DonationHistory.DonationHistoryModel;
import com.example.jonakipust.Model.LoginInfo;
import com.example.jonakipust.Model.Post.Comment.CommentModel;
import com.example.jonakipust.Model.Post.PostModel;
import com.example.jonakipust.Model.UserAdditionalInfo;
import com.example.jonakipust.Model.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Stack;

public class MainDBHelper extends SQLiteOpenHelper {

    static final String NAME = "JonakiPustDB";
    static final int VERSION = 9;
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
                        "phone text)"

        );

        sqLiteDatabase.execSQL(
                "create table UserAdditionalInfo" +
                        "(uid text primary key," +
                        "weight float," +
                        "height int," +
                        "studentID int," +
                        "curAddress text," +
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
                        "postUID text," +
                        "shortDesc text," +
                        "online int)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Users");
        sqLiteDatabase.execSQL("drop table if exists UserAdditionalInfo");
        sqLiteDatabase.execSQL("drop table if exists LoginInfo");
        sqLiteDatabase.execSQL("drop table if exists Posts");
        sqLiteDatabase.execSQL("drop table if exists Comments");
        sqLiteDatabase.execSQL("drop table if exists DonationHistory");

        onCreate(sqLiteDatabase);
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
                    cursor.getString(3),cursor.getString(4), cursor.getInt(5),cursor.getString(6));
        }else{
            user = new UserModel();
        }
        cursor.close();
        database.close();
        return user;
    }

    public ArrayList<UserModel> getUsersByBloodGroup(String bloodGroup){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor;
        if(bloodGroup.equals("All")){
            cursor = database.rawQuery("select * from Users", null);
        }else {
            cursor = database.rawQuery("select * from Users where bloodGroup='" + bloodGroup+"'", null);
        }

        ArrayList<UserModel> userList;

        if(cursor.moveToFirst()){
            userList = new ArrayList<>();
            do{
                userList.add(new UserModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),cursor.getString(4), cursor.getInt(5),cursor.getString(6)));
            }while(cursor.moveToNext());
        }else{
            userList = null;
        }
        cursor.close();
        database.close();
        
        return userList;
    }

    public boolean insertAdditionalInfo(UserAdditionalInfo userAdditionalInfo,boolean isDownloaded){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid",userAdditionalInfo.getUid());
        values.put("weight",userAdditionalInfo.getWeight());
        values.put("height",userAdditionalInfo.getHeight());
        values.put("studentID",userAdditionalInfo.getStudentId());
        values.put("curAddress",userAdditionalInfo.getCurrentAddress());
        values.put("parAddress",userAdditionalInfo.getParmanentAddress());
        values.put("donationHistoryUID",userAdditionalInfo.getDonationHistoryUID());


        database.delete("UserAdditionalInfo","uid="+userAdditionalInfo.getUid(),null);

        long pushed = database.insert("UserAdditionalInfo",null,values);

        if(pushed<=0){
            return false;
        }else{
            if(!isDownloaded) {
                myRef = firebaseDatabase.getReference().child("Datas").child("AdditionalInfo").child(userAdditionalInfo.getUid());
                myRef.setValue(userAdditionalInfo);
            }
            return true;
        }
    }

    public void deleteAdditionalInfo(String uid){
        SQLiteDatabase database = getWritableDatabase();
        database.delete("UserAdditionalInfo","uid="+uid,null);
        firebaseDatabase.getReference().child("Datas").child("AdditionalInfo").child(uid).removeValue();
    }

    public UserAdditionalInfo getUserAdditionalInfoByUID(String uid){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from UserAdditionalInfo where uid="+uid,null);
        UserAdditionalInfo userAdditionalInfo;
        if(cursor.moveToFirst()){
            userAdditionalInfo = new UserAdditionalInfo(cursor.getString(0),cursor.getFloat(1),cursor.getInt(2),
                    cursor.getInt(3),cursor.getString(4), cursor.getString(5),new StringBuilder(cursor.getString(6)));
        }else{
            userAdditionalInfo = new UserAdditionalInfo();
        }
        cursor.close();
        database.close();
        return userAdditionalInfo;
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
                myRef = firebaseDatabase.getReference().child("Datas").child("Comments").child(commentModel.
                        getPostUID()).child(commentModel.getUid());
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
        SQLiteDatabase database = getWritableDatabase();
        if(uid.isEmpty()){
            uid = "101";
        }
        Cursor cursor = database.rawQuery("select * from Comments where uid="+uid,null);
        CommentModel comment;
        if(cursor.moveToFirst()){
            comment = new CommentModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4));
        }else{
            comment = new CommentModel();
        }
        cursor.close();
        database.close();
        return comment;
    }

    public boolean insertDonationHistory(DonationHistoryModel donationHistory){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid",donationHistory.getUid());
        values.put("donerUID",donationHistory.getDonerUid());
        values.put("postUID",donationHistory.getPostUid());
        values.put("shortDesc",donationHistory.getShortDisc());
        values.put("online",0);

        long pushed = database.insert("DonationHistory",null,values);

        if(pushed<=0){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<DonationHistoryModel> getDonationHistoryList(){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from DonationHistory",null);
        ArrayList<DonationHistoryModel> historyList;

        if(cursor.moveToFirst()){
            historyList = new ArrayList<>();
            while(cursor.moveToNext()){
                historyList.add(new DonationHistoryModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3)));
            }
        }else{
            historyList = null;
        }
        cursor.close();
        database.close();
        return historyList;
    }
}
