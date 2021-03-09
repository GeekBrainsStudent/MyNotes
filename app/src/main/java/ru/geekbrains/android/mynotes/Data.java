package ru.geekbrains.android.mynotes;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Data implements Serializable {

    private final String COLLECTION_NAME = "Notes";
    private final String TITLE = "title";
    private final String DESCRIBE = "describe";
    private final String CREATED_AT = "created_at";

    private final ArrayList<MyNote> notes;
    private FirebaseFirestore fireStore;
    private NotifyDataChanged notifyDataChanged;

    public Data() {
        notes = new ArrayList<>();
        fireStore = FirebaseFirestore.getInstance();
        setNotesFromFire();
    }

    private void setNotesFromFire() {
        fireStore.collection(COLLECTION_NAME)
                .orderBy(CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener((task) -> {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            String title = (String) doc.get(TITLE);
                            String describe = (String) doc.get(DESCRIBE);
                            Date created_at = doc.getTimestamp(CREATED_AT).toDate();
                            notes.add(new MyNote(title,describe,created_at));
                        }
                        // Сообщаем адаптеру что данные загружены
                        if(notifyDataChanged != null)
                            notifyDataChanged.dataChanged();
                    } else {
                        Log.e("TAG", "Exception: " + task.getException());
                    }
                });
    }

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

    public interface NotifyDataChanged {
        void dataChanged();
    }

    public void setNotifyDataChanged(NotifyDataChanged notifyDataChanged) {
        this.notifyDataChanged = notifyDataChanged;
    }
}