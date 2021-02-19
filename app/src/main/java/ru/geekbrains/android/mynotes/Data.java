package ru.geekbrains.android.mynotes;

import java.util.ArrayList;
import java.util.Arrays;

public class Data {

    private final ArrayList<MyNote> notes;

    public Data() { notes = new ArrayList<>(); initData(); }

    public Data(ArrayList<MyNote> data) {
        this.notes = data;
    }

    public ArrayList<MyNote> getData() {
        return notes;
    }

    public void addToData(MyNote... notes) { this.notes.addAll(Arrays.asList(notes)); }

    public void removeFromData(MyNote note) { notes.remove(note); }

    private void initData() {
        for(int i = 0; i < 10; i++) {
            notes.add(new MyNote("title" + i, "describe" + i));
        }
    }
}
