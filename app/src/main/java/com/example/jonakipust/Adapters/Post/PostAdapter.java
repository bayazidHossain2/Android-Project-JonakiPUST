package com.example.jonakipust.Adapters.Post;

import static android.content.ContentValues.TAG;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jonakipust.Database.FirebaseHelper;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.DonerInfoActivity;
import com.example.jonakipust.Fragments.Nav_Fragment.PostFragment;
import com.example.jonakipust.Model.Post.Comment.CommentModel;
import com.example.jonakipust.Model.Post.PostModel;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserSelf;
import com.example.jonakipust.R;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{

    Context context;
    ArrayList<PostModel> posts;

    public PostAdapter(Context context,ArrayList<PostModel> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_sample_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PostModel postModel = posts.get(position);
        MainDBHelper dbHelper = new MainDBHelper(context);
        UserModel writer = dbHelper.getUserByUID(postModel.getPostWriterUID());

        Picasso.get().load(writer.getProfile()).placeholder(R.drawable.
                ic_user).into(holder.post_writer_profile);
        holder.post_writer_name.setText(writer.getName());
        holder.post_writer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DonerInfoActivity.class);
                intent.putExtra("uid", writer.getUid());
                context.startActivity(intent);
            }
        });
        holder.post_writing_time.setText(postModel.getPostWritingTime());
        holder.post.setText(postModel.getPost());
        if(postModel.getManaged()==1){
            holder.managed.setText(R.string.manage);
        }else {
            holder.managed.setText(R.string.notManage);
        }
        ArrayList<CommentModel> cList = new ArrayList<>();
        if(!postModel.getCommentUID().equals("")) {
            String[] commentUIDs = postModel.getCommentUID().split(" ");
            Log.d(TAG, "Comment uids are : "+postModel.getCommentUID());
            int lastInd = commentUIDs.length - 1;
            for (; lastInd >= 0; lastInd--) {
                CommentModel newComment = dbHelper.getCommentByUID(commentUIDs[lastInd].trim());
                if (newComment != null) {
                    cList.add(newComment);
                }
            }
        }
        Log.d(TAG, "On comment "+postModel.getUid()+" adapter size is : "+cList.size());
//        cList.add(new CommentModel("01","99","08/05/2022 12:43 pm",
//                "Test Comment."));

        CommentAdapter commentAdapter = new CommentAdapter(context,cList);
        holder.rv_comment.setAdapter(commentAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.rv_comment.setLayoutManager(layoutManager);
        //holder.rv_comment.setNestedScrollingEnabled(false);
        holder.post_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.post_more);
                popupMenu.inflate(R.menu.menu_post);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.menu_post_managed:
                                if(UserSelf.getUserSelf().getUserModel().getUid().equals(writer.getUid())&&postModel.getManaged()==0){
                                    postModel.setManaged(1);
                                    dbHelper.updatePost(postModel);
                                    PostFragment.resetPosts();
                                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("copyText",postModel.getUid());
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(context, "Post managed changed success. And post code copied." +
                                            " Use post code to create donation history.", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(context, "You are not owner of the post.", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            case R.id.menu_post_edit:
                                Toast.makeText(context, "Post edit clicked", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_post_delete:
                                if(UserSelf.getUserSelf().getUserModel().getUid().equals(writer.getUid())){
                                    dbHelper.deletePost(postModel.getUid());
                                    try{
                                        Thread.sleep(2000);
                                        PostFragment.resetPosts();
                                    }catch (Exception ex){
                                        Log.d(TAG,ex.getMessage());
                                    }
                                    Toast.makeText(context, "Post delete success.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "You are not owner of the post.", Toast.LENGTH_SHORT).show();
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

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,postModel.getPost()+"\nFrom: Jonaki PUST \n by: "+writer.getName());
                context.startActivity(intent);
            }
        });

        holder.send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(FirebaseHelper.isConnected(context)) {
                    String sComment = holder.write_comment.getText().toString();
                    if (sComment.equals("")) {
                        Toast.makeText(view.getContext(), "Write Comment first.", Toast.LENGTH_SHORT).show();
                    } else {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                        CommentModel newComment = new CommentModel(String.valueOf(new Date().getTime()), UserSelf.getUserSelf().
                                getUserModel().getUid(), dateTimeFormatter.format(LocalDateTime.now()), sComment, postModel.getUid());
                        boolean success = dbHelper.insertComment(newComment, false);
                        postModel.addComment(newComment.getUid());
                        boolean addToPost = dbHelper.updatePost(postModel);
                        if (success && addToPost) {
                            Toast.makeText(view.getContext(), "Comment wirte success.", Toast.LENGTH_SHORT).show();
                            holder.write_comment.setText("");
                            PostFragment.resetPosts();
                        } else {
                            Toast.makeText(view.getContext(), "Comment insert fail.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(context,"You are not online. Connect online first.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        CircleImageView post_writer_profile;
        TextView post_writer_name;
        TextView post_writing_time;
        TextView post;
        TextView managed;
        TextView share;
        EditText write_comment;
        Button send;
        RecyclerView rv_comment;
        ImageView post_more;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            post_writer_profile = itemView.findViewById(R.id.iv_postWriter);
            post_writer_name = itemView.findViewById(R.id.tv_postWriterName);
            post_writing_time = itemView.findViewById(R.id.tv_postingTime);
            post = itemView.findViewById(R.id.tv_post);
            managed = itemView.findViewById(R.id.tv_managed);
            share = itemView.findViewById(R.id.tv_share);
            write_comment = itemView.findViewById(R.id.et_comment);
            send = itemView.findViewById(R.id.btn_commentSend);
            rv_comment = itemView.findViewById(R.id.rv_comments);
            post_more = itemView.findViewById(R.id.iv_post_more);
        }
    }
}
