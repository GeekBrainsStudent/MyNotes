package ru.geekbrains.android.mynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.io.Serializable;
import java.util.ArrayList;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> implements Serializable {

    private final ArrayList<MyNote> notes;
    private OnItemClickListener onItemClickListener;
    private final Fragment fragment;
    private int itemPosition;

    public NotesListAdapter(ArrayList<MyNote> notes, Fragment fragment) {
        this.notes = notes;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyNote note = notes.get(position);
        holder.getTitleView().setText(note.getTitle());
        holder.getDateView().setText("Created at: " + note.getCreate_at());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView titleView;
        private final MaterialTextView dateView;

        public ViewHolder(@NonNull View item) {
            super(item);
            titleView = item.findViewById(R.id.card_view_title);
            dateView = item.findViewById(R.id.card_view_date);
            item.setOnClickListener((view) -> {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });

            if(fragment != null) {
                fragment.registerForContextMenu(item);
            }
            item.setOnLongClickListener((v) -> {
                itemPosition = getLayoutPosition();
                item.showContextMenu();
                return true;
            });
        }

        public MaterialTextView getTitleView() { return titleView; }
        public MaterialTextView getDateView() { return dateView; }
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public int getItemPosition() { return itemPosition; }
}
