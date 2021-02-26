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
    private CallBack callBack;

    public static NotesListFragment newInstance(ArrayList<MyNote> notes) {
        NotesListFragment fragment = new NotesListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_NOTES_LIST, notes);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callBack = (CallBack) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_notes, container, false);
        setHasOptionsMenu(true);
        return view;
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

        adapter.setOnItemClickListener((v, pos) -> {
            if(callBack != null) {
                callBack.onItemClick(pos);
            }
        });
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

    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }
    public interface CallBack {
        void onItemClick(int pos);

    }
}
