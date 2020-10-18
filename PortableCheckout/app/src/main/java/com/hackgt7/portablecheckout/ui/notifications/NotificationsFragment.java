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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationsFragment extends Fragment implements View.OnClickListener {

    private Button getCart;
    private NotificationsViewModel notificationsViewModel;
    private Date date;
    private ArrayList<CartItem> items;

    private class CartItem {
        String name;
        double quantity;
        double price;

        CartItem(String name, double quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
    }

    public ArrayList<CartItem> generateItemsFromJSON(String response) throws JSONException {
        JSONObject reader = new JSONObject(response);
        JSONArray itemList = reader.getJSONArray("pageContent");
        ArrayList<CartItem> cart = new ArrayList<>();
        for (int i = 0; i < itemList.length(); i++) {
            JSONObject currObj = itemList.getJSONObject(i);
            String name = currObj.get("description").toString();
            JSONObject quantity = (JSONObject) currObj.get("quantity");
            double count = quantity.getDouble("value");
            JSONObject price = (JSONObject) currObj.get("price");
            double unitPrice = price.getDouble("unitPrice");
            CartItem ci = new CartItem(name, count, unitPrice);
            cart.add(ci);
        }
        for (CartItem ea : cart) {
            Log.d("item", ea.name);
        }
        return cart;
    }

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

    String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime()).concat(" GMT");
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
                .url("https://gateway-staging.ncrcloud.com/emerald/selling-service/v1/carts/rdfvFVWJq0euHPrwO9PThQ/items")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "AccessKey 48ea19ed04ad4ae7811f57f307faed31:Dl4rdJg+8B01grsR6vV2ZBVswoFp6Gpwq1tLMZaIb3Q7PeAexhfT4zyJslq4V/Dv1X3MrMuz5oxAs1X69Yqs3Q==")
                .addHeader("nep-organization", "553888f84fdb4ce481dfea21e3202930")
                .addHeader("nep-enterprise-unit", "fe2c3bf9a4194797a234159c1cd34797")
                .addHeader("Date", "Sun, 18 Oct 2020 00:59:13 GMT")
                .build();
        Log.d("MAIN", getServerTime());
        Log.d("MAIN", "Sat, 17 Oct 2020 23:09:53 GMT");
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            try {
                String resp = response.body().string();
                items = generateItemsFromJSON(resp);
                Log.d("MAIN", resp);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}