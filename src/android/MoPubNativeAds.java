package com.rjfun.cordova.mopub;

import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class MoPubNativeAds extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equalsIgnoreCase("__showNativeAds")) {
            Log.d("MoPubNativeAds@@ ", "show native ads called");
            Log.d("MoPubNativeAds@@ ", "__showNativeAds : " + args.getString(0));
            Intent intent = new Intent(this.cordova.getActivity(), GamesRecycler.class);
            intent.putExtra("nativeAdUnit", args.getString(0));
            this.cordova.getActivity().startActivity(intent);
        }
        return false;
    }
}
