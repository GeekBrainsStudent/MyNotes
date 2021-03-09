package ru.geekbrains.android.mynotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListFragment extends Fragment implements
        NoteAddFragment.AddNote,
        NoteFragment.SaveEditNote,
        Data.NotifyDataChanged {

    private static final String KEY_DATA = "key_data";
    private LinearLayout root;
    private Data data;
    private RecyclerView recyclerView;
    private NotesListAdapter adapter;
    private NotesListAdapter.OnItemClickListener onItemClickListener;

    public static NotesListFragment newInstance(Data data) {
        NotesListFragment fragment = new NotesListFragment();
        Bundle bundle = new Bundle();
        data.setNotifyDataChanged(fragment);
        bundle.putSerializable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onItemClickListener = (NotesListAdapter.OnItemClickListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args == null) {
            data = (savedInstanceState == null) ?
                    new Data() : (Data) savedInstanceState.getSerializable(KEY_DATA);
        } else {
            data = (Data) getArguments().getSerializable(KEY_DATA);
        }
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
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotesListAdapter(data.getNotes(), this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);
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
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete:
                int pos = adapter.getItemPosition();
                data.delete(pos);
                adapter.notifyItemRemoved(pos);
                break;
            default: break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_DATA, data);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onItemClickListener = null;
    }

    @Override
    public void add(MyNote newNote) {
        data.insert(newNote);
        int pos = data.getPosition(newNote);
        adapter.notifyItemInserted(pos);
        recyclerView.scrollToPosition(pos);
    }

    @Override
    public void save(MyNote updatedNote) {
        int pos = data.getPosition(updatedNote);
        adapter.notifyItemChanged(pos);
        recyclerView.scrollToPosition(pos);
    }

    @Override
    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }
}
