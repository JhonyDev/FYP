package com.example.fyp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChoicesRVA extends RecyclerView.Adapter<ChoicesRVA.ViewHolder> implements Info {

    private Context context;
    private List<Choice> choiceList;
    private ChoiceInteractionListener choiceInteractionListener;

    public ChoicesRVA(Context context) {
        this.context = context;
        this.choiceList = new ArrayList<>();
        this.choiceInteractionListener = (ChoiceInteractionListener) context;
    }

    public void setChoiceList(List<Choice> choiceList) {
        this.choiceList = choiceList;
        notifyDataSetChanged();
        Log.i(TAG, "setChoiceList: " + choiceList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_choices, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;
        h.ivChoice.setImageDrawable(context.getDrawable(choiceList.get(position).getResourceId()));
    }

    @Override
    public int getItemCount() {
        return choiceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivChoice;
        View rootView;

        ViewHolder(@NonNull View v) {
            super(v);
            this.ivChoice = v.findViewById(R.id.iv_choice);
            this.rootView = v;
            this.rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == rootView) {
                choiceInteractionListener.onChoiceClicked(choiceList.get(getAdapterPosition()));
            }
        }
    }

    interface ChoiceInteractionListener {
        void onChoiceClicked(Choice choice);
    }
}
