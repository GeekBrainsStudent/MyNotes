package ru.geekbrains.android.mynotes;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

    private final String COLLECTION_NAME = "Notes";
    private final ArrayList<MyNote> notes;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    public Data() { notes = new ArrayList<>(); }

    public Data(ArrayList<MyNote> data) {
        this.notes = data;
    }

    public ArrayList<MyNote> getNotes() {
        return notes;
    }

    public MyNote getNote(int pos) {return notes.get(pos); }

    public void insert(MyNote note) { notes.add(note); }

    public void delete(int pos) { notes.remove(pos); }

    public int size() {
        return notes.size();
    }

    public int getPosition(MyNote note) { return notes.indexOf(note); }
}