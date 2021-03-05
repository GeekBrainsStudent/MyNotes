package ru.geekbrains.android.mynotes;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

    private final ArrayList<MyNote> notes;

    public Data() { notes = new ArrayList<>(); }

    public Data(ArrayList<MyNote> data) {
        this.notes = data;
    }

    public ArrayList<MyNote> getNotes() {
        return notes;
    }

    public MyNote getNote(int pos) {return notes.get(pos); }

    public void update(MyNote note) {
        int pos = note.getId();
        MyNote updatedNote = notes.get(pos);
        updatedNote.setTitle(note.getTitle());
        updatedNote.setDescribe(note.getDescribe());
    }

    public int insert(MyNote note) {
        this.notes.add(note);
        int id = this.notes.lastIndexOf(note);
        note.setId(id);
        return id;
    }
}
