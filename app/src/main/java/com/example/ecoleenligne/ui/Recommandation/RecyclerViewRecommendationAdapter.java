package com.example.ecoleenligne.ui.Recommandation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;

import static android.content.ContentValues.TAG;


public class RecyclerViewRecommendationAdapter extends RecyclerView.Adapter<RecyclerViewRecommendationAdapter.ViewHolder> {
    private String[]itemsData;
    private Context mContext;
    private static final String TAG = "RecyclerViewRecommendat";
    public RecyclerViewRecommendationAdapter(Context context,String[] itemsData) {
        this.itemsData = itemsData;
        this.mContext=context;
        Log.d(TAG, "RecyclerViewRecommendationAdapter: I'm in");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: here");
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recomandation_recycler_view,parent,false);
        RecyclerViewRecommendationAdapter.ViewHolder holder=new RecyclerViewRecommendationAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Log.d(TAG, "onBindViewHolder: je marche");
        String [] arr = itemsData[position].split(" ");
        String Titre=""+arr[0]+" "+arr[1]+" "+arr[2]+" "+arr[3];
        Log.d(TAG, "onBindViewHolder: "+Titre);
        viewHolder.txtViewTitle.setText(Titre);
        viewHolder.description.setText(itemsData[position]);


    }
    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+itemsData.length);
        return itemsData.length;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public TextView description;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle =  itemLayoutView.findViewById(R.id.RecommandationTitle);
            description =  itemLayoutView.findViewById(R.id.RecommandationDescription);
        }
    }



}