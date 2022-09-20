package com.example.jonakipust.Adapters.Post;

import static android.content.ContentValues.TAG;

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

import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.DonerInfoActivity;
import com.example.jonakipust.Fragments.Nav_Fragment.PostFragment;
import com.example.jonakipust.Model.Post.Comment.CommentModel;
import com.example.jonakipust.Model.Post.PostModel;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserSelf;
import com.example.jonakipust.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder>{

    Context context;
    ArrayList<CommentModel> comments;

    public CommentAdapter(Context context,ArrayList<CommentModel> list){
        this.context = context;
        comments = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_sample_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CommentModel commentModel = comments.get(position);
        MainDBHelper dbHelper = new MainDBHelper(context);
        UserModel commentWriter = dbHelper.getUserByUID(commentModel.getCommentWriterUID());
        if(commentWriter == null) {
            Log.d(TAG, "Comment adapter ----->>>> "+commentModel.getComment());
            Log.d(TAG, "Comment adapter ----->>>> "+commentModel.getCommentWriterUID());
            return ;
        }
        Picasso.get().load(commentWriter.getProfile()).placeholder(R.drawable.ic_user).into(holder.comment_writer_profile);
        holder.comment_writer_name.setText(commentWriter.getName());
        holder.comment_writer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DonerInfoActivity.class);
                intent.putExtra("uid", commentWriter.getUid());
                context.startActivity(intent);
            }
        });
        holder.comment_writing_time.setText(commentModel.getCommentWritingTime());
        holder.comment.setText(commentModel.getComment());
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.menu);
                popupMenu.inflate(R.menu.menu_comment);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.comment_menu_delete:
                                PostModel post = dbHelper.getPostByUID(commentModel.getPostUID());
                                if(UserSelf.getUserSelf().getUserModel().getUid().equals(commentWriter.getUid()) ||
                                        UserSelf.getUserSelf().getUserModel().getUid().equals(post.getPostWriterUID())){

                                    //Toast.makeText(context, "Try to delete.", Toast.LENGTH_SHORT).show();
                                    post.removeComment(commentModel.getUid());
                                    //Toast.makeText(context, "Comments are : "+post.getCommentUID(), Toast.LENGTH_LONG).show();
                                    boolean success = dbHelper.updatePost(post);
                                    if(success){
                                        Toast.makeText(context, "Comment delete Success", Toast.LENGTH_SHORT).show();
                                        dbHelper.deleteComment(commentModel.getUid(),commentModel.getPostUID());
                                        PostFragment.resetPosts();
                                    }else{
                                        Toast.makeText(context, "Comment delete Fail.", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(context, "You are not owner of the comment.", Toast.LENGTH_LONG).show();
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
        return comments.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        CircleImageView comment_writer_profile;
        TextView comment_writer_name;
        TextView comment_writing_time;
        TextView comment;
        ImageView menu;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            comment_writer_profile = itemView.findViewById(R.id.iv_comment_writer_profile);
            comment_writer_name = itemView.findViewById(R.id.tv_comment_writer_name);
            comment_writing_time = itemView.findViewById(R.id.tv_comment_time);
            comment = itemView.findViewById(R.id.tv_comment);
            menu = itemView.findViewById(R.id.iv_comment_menu);
        }
    }
}
