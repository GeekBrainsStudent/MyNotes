package ru.geekbrains.android.mynotes;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class NoteFragment extends Fragment {

    private final static String KEY_NOTE = "ru.geekbrains.android.mynotes.key.note";
    private ConstraintLayout root;
    private MyNote note;
    private SaveEditNote saveEditNote;

    public static NoteFragment newInstance(MyNote note) {
        NoteFragment fragment = new NoteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_NOTE, note);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        saveEditNote = (SaveEditNote) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = (ConstraintLayout) view;
        note = (MyNote) this.getArguments().getSerializable(KEY_NOTE);
        if(note != null) {
            setNote(note);
        }
        MaterialButton save = root.findViewById(R.id.note_edit_save);
        save.setOnClickListener((v) -> {
            saveClick();
        });
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

    private void saveClick() {
        Editable editable = ((TextInputEditText) root.findViewById(R.id.title)).getText();
        String title = (editable == null) ? "" : editable.toString();
        editable = ((TextInputEditText) root.findViewById(R.id.describe)).getText();
        String describe = (editable == null) ? "" : editable.toString();
        note.setTitle(title);
        note.setDescribe(describe);
        saveEditNote.save(note);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_note_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.share:
//                TODO поделиться
            case R.id.add_image:
//                TODO загрузить изображение
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveEditNote = null;
    }

    @FunctionalInterface
    public interface SaveEditNote {
        void save(MyNote updatedNote);
    }
}
