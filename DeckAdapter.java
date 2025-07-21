package com.example.it404.Flashcard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it404.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.ViewHolder> implements Filterable {

    private DeckListener deckListener;

    public static boolean deckSearch = false;
    private List<DeckModel> mTubeList;
    private List<DeckModel> mTubeListFiltered;

    public DeckAdapter(DeckListener deckListener, List<DeckModel> tubeList) {
        this.deckListener = deckListener;
        mTubeList = tubeList;
        mTubeListFiltered = tubeList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flash_card_main_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DeckModel model = mTubeListFiltered.get(position);
        holder.title.setText(model.Title);
        holder.courseCode.setText(model.CourseCode);
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.alert);
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("You cannot undo this action.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deckListener.DeckDelete(holder.getAdapterPosition(), model);
                        mTubeListFiltered.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deckListener.DeckClick(holder.getAdapterPosition(), model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTubeListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String pattern = charSequence.toString().toLowerCase();
                if(pattern.isEmpty()){
                    mTubeListFiltered = mTubeList;
                } else {
                    List<DeckModel> filteredList = new ArrayList<>();
                    for(DeckModel tube: mTubeList){
                        if(deckSearch == false) {
                            if (tube.CourseCode.toLowerCase().contains(pattern) || tube.CourseCode.toLowerCase().contains(pattern)) {
                                filteredList.add(tube);
                            }
                            else if(tube.Title.toLowerCase().contains(pattern) || tube.Title.toLowerCase().contains(pattern)){
                                filteredList.add(tube);
                            }
                        }
                    }
                    mTubeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTubeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTubeListFiltered = (ArrayList<DeckModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView courseCode;
        private final ImageView deleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.FCTitle);
            courseCode = itemView.findViewById(R.id.FCCourseCode);
            deleteIcon = itemView.findViewById(R.id.FCDeleteIcon);
        }
    }


    public interface DeckListener{
        void DeckClick(int position, DeckModel model);
        void DeckDelete(int position, DeckModel deckModel);
    }

}
