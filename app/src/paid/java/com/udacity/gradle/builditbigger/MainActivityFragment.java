package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_main, container, false);

        Button buttonX = (Button)root.findViewById(R.id.tell_button);
// Register the onClick listener with the implementation above
        buttonX.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tellJoke(root);
            }
        });

        return root;
    }

    public void tellJoke(View view){

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        EndpointsAsyncTask task = new EndpointsAsyncTask(progressBar, getContext());
        task.execute();
    }

}
