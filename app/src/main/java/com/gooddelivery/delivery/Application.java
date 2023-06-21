package com.gooddelivery.delivery;

import android.content.Context;
import androidx.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.gooddelivery.delivery.helper.LocaleUtils;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Iterator;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;


public class Application extends android.app.Application {

    public static final String TAG = Application.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static Application mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        //super.attachBaseContext(base);
        super.attachBaseContext(LocaleUtils.onAttach(base, "es"));
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

//        Stetho.initializeWithDefaults(this);

        mInstance = this;
        MultiDex.install(this);
    }

    public static synchronized Application getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void cancelRequestInQueue(String tag) {
        getRequestQueue().cancelAll(tag);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the no_user tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public static String trimMessage(String json) {
        String trimmedString = "";

        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONArray value = jsonObject.getJSONArray(key);
                    for (int i = 0, size = value.length(); i < size; i++) {
                        Log.e("Errors in Form", "" + value.getString(i));
                        trimmedString += value.getString(i);
                        if (i < size - 1) {
                            trimmedString += '\n';
                        }
                    }
                } catch (JSONException e) {

                    trimmedString += jsonObject.optString(key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Log.e("Trimmed", "" + trimmedString);

        return trimmedString;
    }

    public static NumberFormat getNumberFormat() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String currencyCode = SharedHelper.getKey(mInstance, "currency_code", "USD");
        numberFormat.setCurrency(Currency.getInstance(currencyCode));
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat;
    }

}
