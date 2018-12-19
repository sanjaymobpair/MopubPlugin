package com.rjfun.cordova.mopub;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;

import java.util.ArrayList;
import java.util.List;

public class GamesRecycler extends Activity {
    private String TAG = GamesRecycler.class.getName();
    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String adUnitId;
    MoPubRecyclerAdapter myMoPubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_recycler);
        if (getIntent().getExtras() != null) {
            adUnitId = getIntent().getStringExtra("nativeAdUnit");
        } else {
            Log.d(TAG, " Please add Native Ads UnitId...");
        }
        Log.d("GameRecyclerView@@ ", "" + adUnitId);
        recyclerView = findViewById(R.id.gamelist);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            input.add("Test" + i);
        }// define an adapter
        mAdapter = new ListAdapter(input);
        recyclerView.setAdapter(mAdapter);

        myMoPubAdapter = new MoPubRecyclerAdapter(this, mAdapter);
        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.nativeadsxml)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .mainImageId(R.id.native_main_image_id)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
        myMoPubAdapter.registerAdRenderer(moPubStaticNativeAdRenderer);

        // Set up the RecyclerView and start loading ads
        recyclerView.setAdapter(myMoPubAdapter);
        myMoPubAdapter.loadAds(adUnitId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myMoPubAdapter.destroy();
    }
}
