package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import com.example.yifan.myapplication.backend.jokeApi.JokeApi;
import com.example.yifan.myapplication.backend.jokeApi.model.JokeBean;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.uchicago.yifan.jokelibrary.JokeDisplayActivity;

import java.io.IOException;

/**
 * Created by Yifan on 5/15/16.
 */
class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static final String LOG_TAG = "EndpointsAsyncTask";
    private static JokeApi jokeApi = null;
    private Context context;
    private ProgressBar mProgressBar;
    private InterstitialAd mInterstitialAd;

    public EndpointsAsyncTask(ProgressBar progressBar, Context context){
        this.mProgressBar = progressBar;
        this.context = context;
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(jokeApi == null) {  // Only do this once
            JokeApi.Builder builder = new JokeApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/");
            // end options for devappserver

            jokeApi = builder.build();
        }

        try {
            return jokeApi.putJoke(new JokeBean()).execute().getJoke();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgressBar != null)
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostExecute(final String result) {
        super.onPostExecute(result);

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-2733823621040973/6477787846");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                }

                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                }
                startJokeActivity(result);
            }

            @Override
            public void onAdClosed() {
                startJokeActivity(result);
            }
        });
        AdRequest ar = new AdRequest
                .Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        try {
            mInterstitialAd.loadAd(ar);
        } catch(java.lang.NoClassDefFoundError error) {
            error.printStackTrace();
            Log.d(LOG_TAG, "ERROR LOADING AD");
        }
    }

    private void startJokeActivity(String mResult) {
        Intent intent = new Intent(context, JokeDisplayActivity.class);
        intent.putExtra(JokeDisplayActivity.JOKE_INTENT, mResult);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
