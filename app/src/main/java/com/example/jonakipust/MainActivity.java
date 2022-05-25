package com.example.jonakipust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jonakipust.Database.FirebaseHelper;
import com.example.jonakipust.Fragments.Nav_Fragment.HistoryFragment;
import com.example.jonakipust.Fragments.Nav_Fragment.HomeFragment;
import com.example.jonakipust.Fragments.Nav_Fragment.PostFragment;
import com.example.jonakipust.Fragments.Nav_Fragment.UserFragment;
import com.example.jonakipust.Model.UserAdditionalInfo;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserSelf;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,new UserFragment());
        fragmentTransaction.commit();

        FirebaseHelper.isConnected(MainActivity.this);

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
                        break;
                    case R.id.nav_post:
                        fragmentTransaction.replace(R.id.container,new PostFragment());
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_history:
                        fragmentTransaction.replace(R.id.container,new HistoryFragment());
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_user:
                        fragmentTransaction.replace(R.id.container,new UserFragment());
                        fragmentTransaction.commit();
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
            case R.id.menu_help:
                Toast.makeText(MainActivity.this, "Help Clicked.", Toast.LENGTH_SHORT).show();
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
}