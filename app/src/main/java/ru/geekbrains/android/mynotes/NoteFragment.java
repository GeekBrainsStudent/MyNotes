package ru.geekbrains.android.mynotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class NoteFragment extends Fragment {

    private final static String KEY_NOTE = "key_note";
    private LinearLayout root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = (LinearLayout) view;
        MyNote note = (MyNote) this.getArguments().getSerializable(KEY_NOTE);
        if(note != null) {
            setNote(note);
        }
    }

    public static NoteFragment newInstance(MyNote note) {
        NoteFragment fragment = new NoteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_NOTE, note);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setNote(MyNote note) {
        String title = note.getTitle();
        String describe = note.getDescribe();
        Date date = note.getCreate_at();

        TextInputLayout titleLayout = root.findViewById(R.id.title_layout);
        titleLayout.setHelperText("created at: " + date);
        TextInputEditText titleView = root.findViewById(R.id.title);
        titleView.setText(title);
        TextInputEditText describeView = root.findViewById(R.id.describe);
        describeView.setText(describe);
    }
}
