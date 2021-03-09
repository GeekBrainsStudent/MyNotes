package ru.geekbrains.android.mynotes;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    public void insert(MyNote newNote) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(TITLE, newNote.getTitle());
        hashMap.put(DESCRIBE, newNote.getDescribe());
        hashMap.put(CREATED_AT, newNote.getCreate_at());
        fireStore.collection(COLLECTION_NAME)
                .document(newNote.getCreate_at().toString())
                .set(hashMap)
                .addOnSuccessListener((av) -> {
                    notes.add(newNote);
                    notifyDataChanged.rowInserted(getPosition(newNote));
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", "Exception: " + e);
                });
    }

    public void update(MyNote updatedNote) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(TITLE, updatedNote.getTitle());
        hashMap.put(DESCRIBE, updatedNote.getDescribe());
        hashMap.put(CREATED_AT, updatedNote.getCreate_at());
        fireStore.collection(COLLECTION_NAME)
                .document(updatedNote.getCreate_at().toString())
                .set(hashMap)
                .addOnSuccessListener((av) -> {
                    notifyDataChanged.rowUpdated(getPosition(updatedNote));
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", "Exception: " + e);
                });
    }

    public void delete(int pos) {
        MyNote deleteNote = notes.get(pos);
        fireStore.collection(COLLECTION_NAME)
                .document(deleteNote.getCreate_at().toString())
                .delete()
                .addOnSuccessListener((av) -> {
                    notes.remove(pos);
                    notifyDataChanged.rowDeleted(pos);
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", "Exception: " + e);
                });
    }

    public int size() {
        return notes.size();
    }

    public int getPosition(MyNote note) { return notes.indexOf(note); }

    public interface NotifyDataChanged {
        void dataChanged();
        void rowInserted(int pos);
        void rowUpdated(int pos);
        void rowDeleted(int pos);
    }

    public void setNotifyDataChanged(NotifyDataChanged notifyDataChanged) {
        this.notifyDataChanged = notifyDataChanged;
    }
}