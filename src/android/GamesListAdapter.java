package com.rjfun.cordova.mopub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * GamesListAdapter is Adapter for Show More Games
 * its basic funda to Create is Showing NAtive Ads between the View
 */
public class GamesListAdapter extends RecyclerView.Adapter<GamesListAdapter.ViewHolder> {
    private List<GameListGetterSetter> values;
    private Context mContext;

    // TODO: 2019-05-09 Create ViewHoder For binding Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtgameName, txtclick;
        ImageView imggameIcon;
        LinearLayout mainLayout;
        public View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            mainLayout = v.findViewById(R.id.gamemainlayout);
            txtgameName = v.findViewById(R.id.gamename);
            txtclick = v.findViewById(R.id.gameclick);
            imggameIcon = v.findViewById(R.id.gameicon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GamesListAdapter(Context mContext, List<GameListGetterSetter> myDataset) {
        Log.d("GamesListAdapter ", "" + myDataset.size());
        this.mContext = mContext;
        values = myDataset;
    }

    /**
     * binding its xml and return its view
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public GamesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.gamelist, parent, false);

        return new ViewHolder(v);
    }

    /**
     * it will bind every view on position on click on View It Will Redirect its Platstore Link
     * @param holder
     * @param position
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        // TODO: 2019-05-09 GameListGetterSetter is Model Class to get specific Value and set on Particular Textview or Image View

        GameListGetterSetter gameListGetterSetter = values.get(position);
        final String name = gameListGetterSetter.getGame_name();
        final String imgIcon = gameListGetterSetter.getGame_icon();
        holder.txtgameName.setText(name);
        Picasso.with(mContext).load(imgIcon).into(holder.imggameIcon);

        holder.txtclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GamesListAdapter@@", "click " + gameListGetterSetter.getGame_url() + " Pos ");
                Uri uri = Uri.parse(gameListGetterSetter.getGame_url()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}