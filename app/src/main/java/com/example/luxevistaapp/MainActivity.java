package com.example.luxevistaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.luxevistaapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        setupToolbarAndDrawer();
        setupViewPager();

        // ✅ Fixed: Use binding directly without ".content"
        binding.loginButton.setOnClickListener(v -> login());

        binding.registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void setupToolbarAndDrawer() {
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_accommodation) {
                startActivity(new Intent(MainActivity.this, AccommodationActivity.class));
            } else if (id == R.id.nav_dining) {
                startActivity(new Intent(MainActivity.this, DiningActivity.class));
            } else if (id == R.id.nav_wellness) {
                startActivity(new Intent(MainActivity.this, WellnessActivity.class));
            } else if (id == R.id.nav_contact) {
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
            }

            binding.drawerLayout.closeDrawers();
            return true;
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Foods"); break;
                case 1: tab.setText("Dining"); break;
                case 2: tab.setText("Amenities"); break;
                default: tab.setText("Tab"); break;
            }
        }).attach();
    }

    private void login() {
        String email = binding.emailInput.getText().toString();
        String password = binding.passwordInput.getText().toString();

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
                editor.putInt("userID", userID);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
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
