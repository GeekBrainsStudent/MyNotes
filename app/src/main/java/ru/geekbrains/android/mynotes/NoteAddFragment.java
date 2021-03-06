package ru.geekbrains.android.mynotes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class NoteAddFragment extends Fragment {

    private ConstraintLayout root;
    private AddNote addNote;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addNote = (AddNote) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = (ConstraintLayout) view;
        setButtonsListeners();
    }

    private void setButtonsListeners() {
        MaterialButton save = root.findViewById(R.id.btn_save);
        save.setOnClickListener((v) -> {
            String title = ((TextInputEditText)root.findViewById(R.id.title)).getText().toString();
            String describe = ((TextInputEditText)root.findViewById(R.id.describe)).getText().toString();
            MyNote newNote = new MyNote(title, describe);
            addNote.add(newNote);
        });
        MaterialButton cancel = root.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener((v) -> {
            addNote.cancel();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addNote = null;
    }

    public interface AddNote {
        void add(MyNote newNote);
        default void cancel() {};
    }
}

