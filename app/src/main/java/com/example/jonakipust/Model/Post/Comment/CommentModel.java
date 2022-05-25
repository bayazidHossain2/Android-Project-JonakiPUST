package com.example.jonakipust.Model.Post.Comment;

public class CommentModel {
    private String uid;
    private String commentWriterUID;
    private String commentWritingTime;
    private String comment;
    private String postUID;

    public CommentModel(){}

    public CommentModel(String uid, String commentWriterUID, String commentWritingTime, String comment,String postUID) {
        this.uid = uid;
        this.commentWriterUID = commentWriterUID;
        this.commentWritingTime = commentWritingTime;
        this.comment = comment;
        this.postUID = postUID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCommentWriterUID() {
        return commentWriterUID;
    }

    public void setCommentWriterUID(String commentWriterUID) {
        this.commentWriterUID = commentWriterUID;
    }

    public String getCommentWritingTime() {
        return commentWritingTime;
    }

    public void setCommentWritingTime(String commentWritingTime) {
        this.commentWritingTime = commentWritingTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostUID() {
        return postUID;
    }

    public void setPostUID(String postUID) {
        this.postUID = postUID;
    }
}
