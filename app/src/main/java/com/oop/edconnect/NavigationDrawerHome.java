package com.oop.edconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationDrawerHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_home);

        user = FirebaseAuth.getInstance().getCurrentUser();
        Toolbar toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(getApplicationContext(),user.getDisplayName(),Toast.LENGTH_SHORT);
//        Toast.makeText(getApplicationContext(),user.getPhotoUrl().toString(),Toast.LENGTH_SHORT);



        drawer = findViewById(R.id.nav_drawerlayout);
        NavigationView navigationView = findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        // set profile image
        Picasso.get().load(user.getPhotoUrl()).into((CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_profile_photo));


        // setting email and username
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.nav_email_label);
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.nav_username_label);
        email.setText(user.getEmail());
        name.setText(user.getDisplayName());

        // hamburger icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // to open classrooms view at start
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListData()).commit();
            navigationView.setCheckedItem(R.id.nav_classroom);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // for selected items in drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_classroom:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ListData()).commit();
                break;

            case R.id.nav_setting:
                Toast.makeText(getApplicationContext(), "Settings" ,Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;

        }

         drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //
}
