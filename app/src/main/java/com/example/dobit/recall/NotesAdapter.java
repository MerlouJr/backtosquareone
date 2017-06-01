package com.example.dobit.recall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dobit on 5/22/2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context context;
    ArrayList<NotesModel> data;
    LayoutInflater inflater;

    public NotesAdapter(Context context, ArrayList<NotesModel> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notes_list, parent, false);
        NotesViewHolder holder = new NotesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        holder.note.setText(data.get(position).note);
        holder.time.setText(data.get(position).date);
        holder.pic.setImageResource(data.get(position).pic);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView note;
        TextView time;

        public NotesViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.ivPic);
            note = (TextView) itemView.findViewById(R.id.tvContent);
            time = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
