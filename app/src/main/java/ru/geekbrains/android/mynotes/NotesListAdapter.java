package ru.geekbrains.android.mynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> {

    private ArrayList<MyNote> notes;
    private OnItemClickListener onItemClickListener;

    public NotesListAdapter(ArrayList<MyNote> notes) {
        this.notes = notes;
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
        holder.getTitle().setText(note.getTitle());
        holder.getTitleLayout().setHelperText("Created at: " + note.getCreate_at());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextInputLayout titleLayout;
        private TextInputEditText title;

        public ViewHolder(@NonNull View item) {
            super(item);
            titleLayout = item.findViewById(R.id.card_text_input_layout);
            title = item.findViewById(R.id.card_text_input_edit_text);
            titleLayout.setOnClickListener((view) -> {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        public TextInputLayout getTitleLayout() { return titleLayout; }
        public TextInputEditText getTitle() { return title; }
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
