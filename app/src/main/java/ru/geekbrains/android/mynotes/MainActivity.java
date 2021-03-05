package ru.geekbrains.android.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity implements
        NotesListAdapter.OnItemClickListener {

    private static final String TAG_LIST = "ru.geekbrains.android.mymotes.tag.list";
    private static final String TAG_NOTE = "ru.geekbrains.android.mymotes.tag.note";

    private final String APP_PREFERENCES = "ru.geekbrains.android.mymotes.settings";
    private final String APP_PREFERENCES_DARK_MODE = "ru.geekbrains.android.mymotes.settings.dark_mode";
    private final String KEY_DATA = "ru.geekbrains.android.mymotes.key.data";

    private Data data;
    private SharedPreferences pref;
    private boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = (savedInstanceState != null) ? (Data) savedInstanceState.getSerializable(KEY_DATA) : new Data();
        isLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        checkDarkMode();

        Toolbar toolbar = initToolBar();
        initDrawer(toolbar);

        setNotesListFragment();
        setListenersNavigationMenu();
        setListenersDarkSwitch();
    }

    private void checkDarkMode() {
        SwitchMaterial darkSwitch = (SwitchMaterial) ((NavigationView) findViewById(R.id.nav_view))
                .getMenu()
                .findItem(R.id.dark_mode)
                .getActionView()
                .findViewById(R.id.dark_mode_switch);

        if(pref.contains(APP_PREFERENCES_DARK_MODE)) {
            boolean toggle = pref.getBoolean(APP_PREFERENCES_DARK_MODE, false);
            int mode = (toggle) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            AppCompatDelegate.setDefaultNightMode(mode);
            darkSwitch.setChecked(toggle);
        }
    }

    private Toolbar initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setNotesListFragment() {

        FragmentManager fm = getSupportFragmentManager();


        Fragment fragment = fm.findFragmentById(R.id.fragment_list);
        if(fragment == null) {
            fm.beginTransaction()
                    .replace(R.id.fragment_list, NotesListFragment.newInstance(data), TAG_LIST)
                    .commit();
        } else if(isLandscape) {
            if(!fragment.getTag().equals(TAG_LIST)) {
                fm.beginTransaction()
                        .remove(fragment)
                        .replace(R.id.fragment_list, NotesListFragment.newInstance(data), TAG_LIST)
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
                Fragment frag = NoteFragment.newInstance(data.getNotes().get(0));
                fm.beginTransaction()
                        .replace(R.id.fragment_content, frag, TAG_NOTE)
                        .commit();
            }
        }
    }

    private void setListenersNavigationMenu() {
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
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

    private void setListenersDarkSwitch() {
        NavigationView navView = findViewById(R.id.nav_view);
        SwitchMaterial switchDarkMode = (SwitchMaterial) navView.getMenu().findItem(R.id.dark_mode).getActionView().findViewById(R.id.dark_mode_switch);
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int mode = (b) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                pref.edit().putBoolean(APP_PREFERENCES_DARK_MODE, b).apply();
                AppCompatDelegate.setDefaultNightMode(mode);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean res = super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            default:
                break;
        }
        return res;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_DATA, data);
    }

    @Override
    public void onItemClick(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        int fragmentContainer = (isLandscape) ? R.id.fragment_content : R.id.fragment_list;
        fm.beginTransaction()
                .replace(fragmentContainer, NoteFragment.newInstance(data.getNote(pos)), TAG_NOTE)
                .addToBackStack(null)
                .commit();
    }
}