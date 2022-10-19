package com.example.jonakipust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jonakipust.Database.FirebaseHelper;
import com.example.jonakipust.Fragments.Nav_Fragment.HistoryFragment;
import com.example.jonakipust.Fragments.Nav_Fragment.HomeFragment;
import com.example.jonakipust.Fragments.Nav_Fragment.PostFragment;
import com.example.jonakipust.Fragments.Nav_Fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    Handler handler;
    public static int frag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,new UserFragment());
        fragmentTransaction.commit();
        frag=0;

        nav = findViewById(R.id.bottom_nav);
        toolbar = findViewById(R.id.castom_incluted_toolbar);
        setSupportActionBar(toolbar);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragmentTransaction.replace(R.id.container,new HomeFragment());
                        fragmentTransaction.commit();
                        frag=0;
                        break;
                    case R.id.nav_post:
                        fragmentTransaction.replace(R.id.container,new PostFragment());
                        fragmentTransaction.commit();
                        frag=1;
                        break;
                    case R.id.nav_history:
                        fragmentTransaction.replace(R.id.container,new HistoryFragment());
                        fragmentTransaction.commit();
                        frag=2;
                        break;
                    case R.id.nav_user:
                        fragmentTransaction.replace(R.id.container,new UserFragment());
                        fragmentTransaction.commit();
                        frag=3;
                        break;
                }return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_about:
                Toast.makeText(MainActivity.this, "About Clicked.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.post_load:
                Toast.makeText(MainActivity.this, "Load Post clicked", Toast.LENGTH_SHORT).show();
                FirebaseHelper.loadPost(MainActivity.this);
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Post Loading");
                progressDialog.setMessage("Wait few moment to load posts.");
                progressDialog.show();
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //your code here
                        if(FirebaseHelper.alldownloaded){
                            PostFragment.resetPosts();
                            Toast.makeText(MainActivity.this, "All post and comment loaded.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                }, 10000);
                break;
            case R.id.menu_load_donation:
                Toast.makeText(MainActivity.this, "Donation load clicked.", Toast.LENGTH_SHORT).show();
                FirebaseHelper.loadDonation(MainActivity.this);
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Donation History Loading");
                progressDialog.setMessage("Wait few moment to load donation history.");
                progressDialog.show();
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //your code here
                        if(FirebaseHelper.alldownloaded){
                            HistoryFragment.resetHistory();
                            Toast.makeText(MainActivity.this, "All donation history loaded.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                }, 8000);
                break;
            case R.id.menu_reset:
                Toast.makeText(MainActivity.this, "Reset clicked.", Toast.LENGTH_SHORT).show();
                if(FirebaseHelper.isConnected(MainActivity.this)) {
                    FirebaseHelper.reset(MainActivity.this, true);
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("Reset All Data");
                    progressDialog.setMessage("Wait 10 second to load All Data.");
                    progressDialog.show();
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //your code here
                            if (FirebaseHelper.alldownloaded) {
                                Toast.makeText(MainActivity.this, "Reset Success.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }else{
                                SharedPreferences sp = getSharedPreferences("Login",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("uid",null);
                                editor.putString("Psw",null);
                                editor.commit();

                                Intent intentL = new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(intentL);
                                progressDialog.dismiss();
                                MainActivity.this.finish();
                            }
                        }
                    }, 10000);
                }else{
                    Toast.makeText(MainActivity.this, "Before reset check internet connection.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_logout:
                Toast.makeText(MainActivity.this, "Logout clicked.", Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("Login",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("uid",null);
                editor.putString("Psw",null);
                editor.commit();

                Intent intentL = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intentL);
                MainActivity.this.finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        if(frag!=0) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, new HomeFragment());
            fragmentTransaction.commit();
            frag = 0;
        }else {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(R.drawable.ic_jonaki)
                    .setTitle("Exit")
                    .setMessage("Are you sure to exit")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setNeutralButton("Help", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, "To Exit click yes.", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }
}