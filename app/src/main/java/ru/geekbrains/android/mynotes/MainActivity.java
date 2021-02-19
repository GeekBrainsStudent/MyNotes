package ru.geekbrains.android.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<MyNote> notes = new Data().getData();

        FragmentManager fm = getSupportFragmentManager();
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if(isLandscape) {
            Fragment fragment_list = fm.findFragmentById(R.id.fragment_list);

            if(fragment_list == null) {
                fm.beginTransaction()
                        .add(R.id.fragment_list, NotesListFragment.newInstance(notes))
                        .add(R.id.fragment_content, NoteFragment.newInstance(notes.get(0)))
                        .commit();
            }
        } else {
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);

            if(fragment == null) {
                fm.beginTransaction()
                        .add(R.id.fragment_container, NotesListFragment.newInstance(notes))
                        .commit();
            }
        }


    }
}