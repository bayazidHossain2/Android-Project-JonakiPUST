package com.example.jonakipust.Fragments.Nav_Fragment;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jonakipust.Adapters.Post.PostAdapter;
import com.example.jonakipust.Database.FirebaseHelper;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.Model.Post.Comment.CommentModel;
import com.example.jonakipust.Model.Post.PostModel;
import com.example.jonakipust.Model.UserSelf;
import com.example.jonakipust.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostFragment extends Fragment {

    RecyclerView posts;
    ArrayList<PostModel> postList;
    CircleImageView postWriterProfile;
    EditText postText;
    Button postBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    static FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainDBHelper dbHelper = new MainDBHelper(view.getContext());

        postText = view.findViewById(R.id.et_post_write);
        postBtn = view.findViewById(R.id.btn_post);
        postWriterProfile = view.findViewById(R.id.civ_post_user_profile_image);
        posts = view.findViewById(R.id.rv_posts);
        firebaseDatabase = FirebaseDatabase.getInstance();
        fragmentTransaction = getParentFragmentManager().beginTransaction();

        Picasso.get().load(UserSelf.getUserSelf().getUserModel().getProfile()).placeholder(R.drawable.ic_user).into(postWriterProfile);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(FirebaseHelper.isConnected(view.getContext())) {
                    String sPost = postText.getText().toString();
                    if (sPost.equals("")) {
                        Toast.makeText(view.getContext(), "Write post first.", Toast.LENGTH_SHORT).show();
                    } else {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                        PostModel post = new PostModel(String.valueOf(Long.MAX_VALUE-new Date().getTime()),
                                UserSelf.getUserSelf().getUserModel().getUid(),
                                dateTimeFormatter.format(LocalDateTime.now()), sPost, 0, "");
                        boolean success = dbHelper.insertPost(post,false);
                        if (success) {
                            Toast.makeText(view.getContext(), "Post wirte success.", Toast.LENGTH_SHORT).show();
                            postText.setText("");
                            resetPosts();
                        } else {
                            Toast.makeText(view.getContext(), "Post insert fail.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getContext(),"You are not online.",Toast.LENGTH_LONG);
                }
            }
        });

        postList = dbHelper.getPostList();
        if(postList == null){
            postList = new ArrayList<>();
        }
        PostAdapter postAdapter = new PostAdapter(view.getContext(),postList);
        posts.setAdapter(postAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        posts.setLayoutManager(layoutManager);
    }

    public static void resetPosts(){
        fragmentTransaction.replace(R.id.container,new PostFragment());
        fragmentTransaction.commit();
    }
}