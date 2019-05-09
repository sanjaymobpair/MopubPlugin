package com.rjfun.cordova.mopub;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;
import com.mopub.volley.AuthFailureError;
import com.mopub.volley.DefaultRetryPolicy;
import com.mopub.volley.NetworkResponse;
import com.mopub.volley.NoConnectionError;
import com.mopub.volley.Request;
import com.mopub.volley.RequestQueue;
import com.mopub.volley.Response;
import com.mopub.volley.TimeoutError;
import com.mopub.volley.VolleyError;
import com.mopub.volley.toolbox.StringRequest;
import com.mopub.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * GamesRecycler is Activity Where More Games List Will be Showing also Implement Native Ads Integration here
 */
public class GamesRecycler extends Activity {
    private String TAG = GamesRecycler.class.getName();
    private RecyclerView recyclerView;
    private GamesListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String adUnitId;
    MoPubRecyclerAdapter myMoPubAdapter;
    GameListGetterSetter gameListGetterSetter;
    ArrayList<GameListGetterSetter> arrayListgameListGetterSetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_recycler);

        // TODO: 2019-05-09 init arrayListgameListGetterSetters list Of Data
        arrayListgameListGetterSetters = new ArrayList<>();

        // TODO: 2019-05-09 get intent String Data for NAtive Unit Id
        if (getIntent().getExtras() != null) {

            // TODO: 2019-05-09 NAtive ID
            adUnitId = getIntent().getStringExtra("nativeAdUnit");
        } else {
            Log.d(TAG, " Please add Native Ads UnitId...");
        }
        Log.d("GameRecyclerView@@ ", "" + adUnitId);

        // TODO: 2019-05-09 callApi with send Api Url
        callApi("https://tr.adsx.bid/more_games/gae_list.php");

        // TODO: 2019-05-09 Binding Recycler View
        recyclerView = findViewById(R.id.gamelist);
    }

    /**
     * IF myMoPubAdapter is not null than destroy its Adapter
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myMoPubAdapter != null) {
            myMoPubAdapter.destroy();
        }
    }


    // TODO: 2019-05-09 Getting Api Data Using Volley Library
    // TODO: 2019-05-09 Learn Volley Library Url "https://www.geeksforgeeks.org/volley-library-in-android/"
    public void callApi(String url) {

        // TODO: 2019-05-09 get android id
        private String android_id = Secure.getString(getContext().getContentResolver(),
                Secure.ANDROID_ID);
        StringRequest stringRequest = new
                StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null) {
                                    try {

                                        // TODO: 2019-05-09 setJson Data using gameListGetterSetter method and last store that mdoel in arraylist
                                        JSONArray jsonArray = new JSONArray(response);
                                        Log.d(TAG, "ARRRAY " + jsonArray.length() + " DATA ");
                                        for (int i = 0; i <= jsonArray.length(); i++) {
                                            gameListGetterSetter = new GameListGetterSetter();
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            Log.d(TAG, "ARRRAY " + jsonObject.getString("game_url"));
                                            gameListGetterSetter.setId(jsonObject.getInt("id"));
                                            gameListGetterSetter.setGame_icon(jsonObject.getString("game_icon"));
                                            gameListGetterSetter.setGame_name(jsonObject.getString("game_name"));
                                            gameListGetterSetter.setGame_url(jsonObject.getString("game_url"));
                                            arrayListgameListGetterSetters.add(gameListGetterSetter);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    recyclerView.setHasFixedSize(true);
                                    // use a linear layout manager
                                    layoutManager = new LinearLayoutManager(GamesRecycler.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    mAdapter = new GamesListAdapter(GamesRecycler.this, arrayListgameListGetterSetters);

                                    // TODO: 2019-05-09 setAdapter
                                    recyclerView.setAdapter(mAdapter);

                                    // TODO: 2019-05-09 init myMoPubAdapter adapter with Passing mAdapter also u can set title,imageId Privacy in image info etc.

                                    // TODO: 2019-05-09 U Can Check MoPub Native Ads Document here "https://developers.mopub.com/publishers/android/native/"
                                    myMoPubAdapter = new MoPubRecyclerAdapter(GamesRecycler.this, mAdapter);
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
                                    Log.d(TAG, "MAINURL " + arrayListgameListGetterSetters.get(1).getGame_url());
                                }
                            }
                        },

                        /**
                         * it will call when any error occured during calling of url
                         */
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "MAINURL " + "error");

                                /**
                                 * here is some statuscode when error occured
                                 */
                                NetworkResponse networkResponse = error.networkResponse;
                                String errorMessage = "Unknown error";
                                if (networkResponse == null) {
                                    if (error.getClass().equals(TimeoutError.class)) {
                                        errorMessage = "Request timeout";
                                    } else if (error.getClass().equals(NoConnectionError.class)) {
                                        errorMessage = "Failed to connect server";
                                    }
                                    Log.d(TAG, "Error msg1 " + errorMessage);
                                } else {
                                    String result = new String(networkResponse.data);
                                    try {
                                        JSONObject response = new JSONObject(result);
                                        String status = response.getString("status");
                                        String message = response.getString("message");

                                        Log.e("Error Status", status);
                                        Log.e("Error Message", message);

                                        if (networkResponse.statusCode == 404) {
                                            errorMessage = "Resource not found";
                                        } else if (networkResponse.statusCode == 401) {
                                            errorMessage = message + " Please login again";
                                        } else if (networkResponse.statusCode == 400) {
                                            errorMessage = message + " Check your inputs";
                                        } else if (networkResponse.statusCode == 500) {
                                            errorMessage = message + " Something is getting wrong";
                                        }
                                        Log.d(TAG, "Error msg " + errorMessage);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                error.printStackTrace();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> stringMap = new HashMap<>();
                        stringMap.put("deviceId", android_id);
                        return stringMap;
                    }
                };

        // TODO: 2019-05-09 set VolleyRequeste
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
}
