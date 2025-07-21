package com.example.it404.Flashcard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it404.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class PublicDeckAdapter extends RecyclerView.Adapter<PublicDeckAdapter.ViewHolder> implements Filterable {

    private PublicDeckListener publicDeckListener;

    public static boolean publicDeckSearch = false;
    private List<DeckModel> mTubeList;
    private List<DeckModel> mTubeListFiltered;

    public PublicDeckAdapter(PublicDeckListener publicDeckListener, List<DeckModel> tubeList) {
        this.publicDeckListener = publicDeckListener;
        mTubeList = tubeList;
        mTubeListFiltered = tubeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_deck_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DeckModel model = mTubeListFiltered.get(position);
        holder.title.setText(model.Title);
        holder.courseCode.setText(model.CourseCode);
        holder.author.setText("Created By: "+model.getAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                publicDeckListener.PublicDeckClick(holder.getAdapterPosition(), model);

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
                        if(publicDeckSearch == false) {
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
        private final TextView author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.PublicFCTitle);
            courseCode = itemView.findViewById(R.id.PublicFCCourseCode);
            author = itemView.findViewById(R.id.author);
        }
    }


    public interface PublicDeckListener{
        void PublicDeckClick(int position, DeckModel model);
    }

}
