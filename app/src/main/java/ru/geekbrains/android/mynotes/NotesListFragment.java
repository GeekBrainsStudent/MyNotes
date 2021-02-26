package ru.geekbrains.android.mynotes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;

public class NotesListFragment extends Fragment {

    private static final String KEY_NOTES_LIST = "key_notes_list";
    private LinearLayout root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_notes, container, false);
        setHasOptionsMenu(true);
        Log.d("TAG", "onCreateView NotesListFragment");
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "onDestroy NotesListFragment");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = (LinearLayout) view;
        ArrayList<MyNote> notes = (ArrayList<MyNote>) this.getArguments().getSerializable(KEY_NOTES_LIST);
        initRecyclerView(notes);
    }

    private void initRecyclerView(ArrayList<MyNote> notes) {
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);

        NotesListAdapter adapter = new NotesListAdapter(notes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_list_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.search :
//                TODO поиск
            case R.id.sort :
//                TODO сортировка
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private TextInputLayout createTextInputLayout(MyNote note) {
        String title = note.getTitle();
        Date created_at = note.getCreate_at();

        TextInputLayout tvLayout = new TextInputLayout(root.getContext());
        tvLayout.setHelperText("created at: " + created_at);

        TextInputEditText tv = new TextInputEditText(root.getContext());
        tv.setText(title);

        tvLayout.addView(tv);

        return tvLayout;
    }

    private void setOnClickListener(TextInputLayout tvLayout, MyNote note) {
        tvLayout.setOnClickListener((v) -> {
            Activity activity = getActivity();
            if(activity != null) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                boolean isLandscape = (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
                if(isLandscape) {
                    NoteFragment fragment = (NoteFragment) fm.findFragmentById(R.id.fragment_content);
                    if(fragment != null) {
                        fragment.setNote(note);
                    } else {
                        fm.beginTransaction()
                                .add(R.id.fragment_content, NoteFragment.newInstance(note))
                                .commit();
                    }
                } else {
                    fm.beginTransaction()
                            .replace(R.id.fragment_list, NoteFragment.newInstance(note))
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }
    
    public static NotesListFragment newInstance(ArrayList<MyNote> notes) {
        NotesListFragment fragment = new NotesListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_NOTES_LIST, notes);
        fragment.setArguments(bundle);
        return fragment;
    }
}
