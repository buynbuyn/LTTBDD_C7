package com.example.chuong_7;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.chuong_7.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).setAnchorView(R.id.fab).show()
        );

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_na,
                R.id.nav_kn,  // Nhóm menu KN
                R.id.nav_sm,  // Nhóm menu SM
                R.id.nav_camera,
                R.id.nav_video,
                R.id.nav_audio
        ).setOpenableLayout(drawer).build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Ẩn menu con lúc khởi động
        Menu menu = navigationView.getMenu();
        hideAllSubmenus(menu);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_na) {
                toggleKNMenuItems(menu);
                return true;
            } else if (id == R.id.nav_ma) {
                toggleSMMenuItems(menu);
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) drawer.closeDrawer(GravityCompat.START);
            return handled;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    // ===== Menu Group Helpers =====

    private boolean isKNMenuItem(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.nav_kn || id == R.id.nav_sm || id == R.id.nav_http;
    }

    private void toggleKNMenuItems(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (isKNMenuItem(item)) {
                item.setVisible(!item.isVisible());
            }
        }
    }

    private boolean isSMMenuItem(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.nav_lt || id == R.id.nav_camera || id == R.id.nav_video || id == R.id.nav_audio;
    }

    private void toggleSMMenuItems(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (isSMMenuItem(item)) {
                item.setVisible(!item.isVisible());
            }
        }
    }

    private void hideAllSubmenus(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (isKNMenuItem(item) || isSMMenuItem(item)) {
                item.setVisible(false);
            }
        }
    }
}