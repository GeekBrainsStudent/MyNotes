package ru.geekbrains.android.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NotesListFragment.CallBack {

    ArrayList<MyNote> notes = new Data().getData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = initToolBar();
        initDrawer(toolbar);
        setListenersNavigationMenu();

        setFragments(notes);
    }

    private void setFragments(ArrayList<MyNote> notes) {

        FragmentManager fm = getSupportFragmentManager();
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        Fragment fragment = fm.findFragmentById(R.id.fragment_list);
        if(fragment == null) {
            fm.beginTransaction()
                    .replace(R.id.fragment_list, NotesListFragment.newInstance(notes))
                    .commit();
        } else if(isLandscape) {
            if(fragment instanceof NoteFragment) {
                fm.beginTransaction()
                        .remove(fragment)
                        .replace(R.id.fragment_list, NotesListFragment.newInstance(notes))
                        .commit();
            }
        }

        fragment = fm.findFragmentById(R.id.fragment_content);

        if (!isLandscape) {
            if(fragment != null) {
                fm.beginTransaction()
                        .remove(fragment)
                        .commit();
            }

        } else {
            if(fragment == null) {
                fm.beginTransaction()
                        .replace(R.id.fragment_content, NoteFragment.newInstance(notes.get(0)))
                        .commit();
            }

        }
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setListenersDarkMode() {
        SwitchMaterial switchDarkMode = (SwitchMaterial) findViewById(R.id.dark_mode_switch);
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int mode = (b) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(mode);
            }
        });

        MaterialCheckBox systemCheckBox = (MaterialCheckBox) findViewById(R.id.system_mode_checkbox);
        systemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int mode = (b) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(mode);
            }
        });
    }

    private void setListenersNavigationMenu() {
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.system_mode:
                        MaterialCheckBox systemMode = (MaterialCheckBox) item.getActionView().findViewById(R.id.system_mode_checkbox);
                        systemMode.toggle();
                        NavigationMenuItemView customDarkMode = findViewById(R.id.dark_mode);
                        if(systemMode.isChecked())
                            customDarkMode.setVisibility(View.INVISIBLE);
                        else
                            customDarkMode.setVisibility(View.VISIBLE);
                        return false;
                    case R.id.dark_mode:
                        SwitchMaterial darkSwitch = (SwitchMaterial) item.getActionView().findViewById(R.id.dark_mode_switch);
                        darkSwitch.toggle();
                        return false;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private Toolbar initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
        return toolbar;
    }

    @Override
    public void onItemClick(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int fragmentContainer = (isLandscape) ? R.id.fragment_content : R.id.fragment_list;
        fm.beginTransaction()
                .replace(fragmentContainer, NoteFragment.newInstance(notes.get(pos)))
                .addToBackStack(null)
                .commit();
    }
}