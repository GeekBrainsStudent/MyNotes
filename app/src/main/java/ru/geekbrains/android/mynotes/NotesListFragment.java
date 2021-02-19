package ru.geekbrains.android.mynotes;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = (LinearLayout) view;
        ArrayList<MyNote> notes = (ArrayList<MyNote>) this.getArguments().getSerializable(KEY_NOTES_LIST);
        if(notes != null) {
            for(MyNote note : notes) {
                TextInputLayout tvLayout = createTextInputLayout(note);
                setOnClickListener(tvLayout, note);
                root.addView(tvLayout);
            }
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
                            .replace(R.id.fragment_container, NoteFragment.newInstance(note))
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
