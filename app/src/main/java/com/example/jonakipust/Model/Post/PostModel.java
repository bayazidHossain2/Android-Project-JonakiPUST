package com.example.jonakipust.Model.Post;

import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;

public class PostModel {
    String uid;
    private String postWriterUID;
    private String postWritingTime;
    private String post;
    private int managed;
    private String commentUID;

    public PostModel(){}

    public PostModel(String uid, String postWriterUID, String postWritingTime, String post, int managed, String commentUID) {
        this.uid = uid;
        this.postWriterUID = postWriterUID;
        this.postWritingTime = postWritingTime;
        this.post = post;
        this.managed = managed;
        this.commentUID = commentUID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostWriterUID() {
        return postWriterUID;
    }

    public void setPostWriterUID(String postWriterUID) {
        this.postWriterUID = postWriterUID;
    }

    public String getPostWritingTime() {
        return postWritingTime;
    }

    public void setPostWritingTime(String postWritingTime) {
        this.postWritingTime = postWritingTime;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getManaged() {
        return managed;
    }

    public void setManaged(int managed) {
        this.managed = managed;
    }

    public String getCommentUID() {
        return commentUID;
    }

    public void setCommentUIDList(String commentUID) {
        this.commentUID = commentUID;
    }

    public void addComment(String commentUID){
        StringBuilder stringBuilder = new StringBuilder(this.commentUID);
        stringBuilder.append(commentUID).append(" ");
        this.commentUID = stringBuilder.toString();
    }

    public void removeComment(String commentUID){
        StringBuilder newComments = new StringBuilder();
        String[] oldComments = getCommentUID().split(" ");
        for(String comment : oldComments){
            if(!comment.equals(commentUID)){
                newComments.append(comment).append(" ");
            }
        }
        this.commentUID = newComments.toString();
    }
}
