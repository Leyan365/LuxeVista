package com.example.luxevistaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;

public class MainActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(v -> login());

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_accommodation) {
                Intent intent = new Intent(MainActivity.this, AccommodationActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_dining) {
                Intent intent = new Intent(MainActivity.this, DiningActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_wellness) {
                Intent intent = new Intent(MainActivity.this, WellnessActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_contact) {
                Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }

            drawerLayout.closeDrawers();
            return true;
        });


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Foods");
                        break;
                    case 1:
                        tab.setText("Dining");
                        break;
                    case 2:
                        tab.setText("Amenities");
                        break;
                    default:
                        tab.setText("Tab");
                        break;
                }
            }
        }).attach();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_accommodation) {
            Intent intent = new Intent(MainActivity.this, AccommodationActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.nav_dining) {
            Intent intent = new Intent(MainActivity.this, DiningActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.nav_wellness) {
            Intent intent = new Intent(MainActivity.this, WellnessActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.nav_contact) {
            Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void login() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        Log.d("Login", "Email: " + email + ", Password: " + password);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            int userID = dbHelper.validateUser(email, password);

            if (userID != -1) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();

                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("userID", userID); // Save the userID
                editor.apply(); // Commit the changes

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("LoginError", "Error during login", e);
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_LONG).show();
        }
    }
}
