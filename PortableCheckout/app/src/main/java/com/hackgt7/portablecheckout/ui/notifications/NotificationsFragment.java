package com.hackgt7.portablecheckout.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hackgt7.portablecheckout.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationsFragment extends Fragment implements View.OnClickListener {

    private Button getCart;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        getCart = (Button) root.findViewById(R.id.get_cart_button);
        getCart.setOnClickListener(this::onClick);
        return root;
    }



    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Response response = null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://gateway-staging.ncrcloud.com/emerald/selling-service/v1/carts/rdfvFVWJq0euHPrwO9PThQ")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "AccessKey 48ea19ed04ad4ae7811f57f307faed31:Uq2eXP1TMymj2gGrSdqc5gT3IJfDWHFVkIit7VFHENz5kJzmJhmIPTCx71W+KBzJ4w3O3T3elNekIrkV/7C9lg==")
                .addHeader("nep-organization", "553888f84fdb4ce481dfea21e3202930")
                .addHeader("nep-enterprise-unit", "fe2c3bf9a4194797a234159c1cd34797")
                .addHeader("Date", "Sat, 17 Oct 2020 16:23:05 GMT")
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            try {
                Log.d("MAIN", response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}