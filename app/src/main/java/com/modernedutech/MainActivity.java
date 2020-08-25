package com.modernedutech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.modernedutech.Adapter.ViewPagerAdapter;
import com.modernedutech.Fragments.LiveClass;
import com.modernedutech.Fragments.RecordedClass;
import com.modernedutech.Fragments.Staff;
import com.modernedutech.LoginSet.LogsActivity;
import com.modernedutech.Settings.ProfileEditActivity;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Navigation Drawer
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    TabLayout tabLayout;
    ViewPager viewPager;

    FirebaseUser user;
    DatabaseReference reference;

    ImageView ProfileView;

    public static final int READ_CONTACTS_REQUEST = 1;
    public static final int STORAGE_REQUEST = 2;

    boolean permitted;
    TextView warning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        warning = findViewById(R.id.notPermitted);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Navigation Drawer
        drawerLayout = findViewById(R.id.new_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_courses).setVisible(false);
        nav_Menu.findItem(R.id.nav_approve).setVisible(false);

        setUpviewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user == null) {
            Intent i = new Intent(getApplicationContext(), LogsActivity.class);
            startActivity(i);
        }else{
            requestPermission();
            reference = FirebaseDatabase.getInstance().getReference("Users").child("Info").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String Name = snapshot.child("username").getValue().toString();
                    String Image = snapshot.child("imageUrl").getValue().toString();
                    String Designation = snapshot.child("Designation").getValue().toString();

                    navigationView = findViewById(R.id.nav_view);
                    Menu nav_Menu = navigationView.getMenu();
                    if(Name.isEmpty() &&Image.isEmpty()){

                        nav_Menu.findItem(R.id.nav_edit).setVisible(true);

                    }else {
                        ProfileView =findViewById(R.id.ProfileView);
                        if ( ProfileView !=null) {
                            Glide.with(MainActivity.this).load(Image).circleCrop().into(ProfileView);
                        }

                        nav_Menu.findItem(R.id.nav_edit).setVisible(false);
                    }
                    if (Designation.equals("Principal")){
                        nav_Menu.findItem(R.id.nav_courses).setVisible(true);
                        nav_Menu.findItem(R.id.nav_approve).setVisible(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if (!permitted) {
                warning.setVisibility(View.VISIBLE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



            }else{
                warning.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }

    }

    //Navigation Drawer
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_aboutus) {

        }
        if (id == R.id.nav_edit) {

            Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
            startActivity(intent);

        }
        if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(getApplicationContext(),LogsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //Bottom TabLayout
    public void setUpviewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LiveClass(), "Live");
        adapter.addFragment(new RecordedClass(), "Classes");
        adapter.addFragment(new Staff(), "Tutor");
        adapter.addFragment(new Staff(), "Learner");

        viewPager.setAdapter(adapter);
    }

    private void requestPermission() {

        if (isStoragePermissionGranted() && requestContactPermission()) {
            permitted = true;
        }

    }

    public boolean requestContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST);
                }

            } else {
                return true;

            }
            return false;
        } else {
            return true;
        }

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Storage access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to storage. Otherwise you wont able to set Profile Pic");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
                }
            } else {
                return true;
            }
            return false;
        } else {
            return true;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    permitted = false;
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case STORAGE_REQUEST:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    //resume tasks needing this permission
                } else {
                    permitted = false;
                    Toast.makeText(this, "You have disabled a Storage Permission", Toast.LENGTH_LONG).show();
                }

                }
                return;
            }
        }

}