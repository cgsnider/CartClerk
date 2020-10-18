package com.hackgt7.portablecheckout.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

public class NotificationsFragment extends Fragment {

    private View root;

    private ArrayList<String> nameList;
    private ArrayAdapter<String> nameAdapter;
    private ListView nameView;

    private ArrayList<String> priceList;
    private ArrayAdapter<String> priceAdapter;
    private ListView priceView;

    private ArrayList<String> quanList;
    private ArrayAdapter<String> quanAdapter;
    private ListView quanView;

    private NotificationsViewModel notificationsViewModel;
    private Date date;
    private ArrayList<CartItem> items;

    public double getTotal() {
        double total = 0.0;
        for (CartItem ea: items) {
            total += ea.price * ea.quantity;
        }
        return total;
    }

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

        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        getAndBuildCartJSON();
        nameList = new ArrayList<>();
        nameView = root.findViewById(R.id.name_list);
        nameAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1);
        nameView.setAdapter(nameAdapter);
        setUpListViewListener(nameView);

        priceList = new ArrayList<>();
        priceView = root.findViewById(R.id.price_list);
        priceAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1);
        priceView.setAdapter(priceAdapter);
        setUpListViewListener(priceView);

        quanList = new ArrayList<>();
        quanView = root.findViewById(R.id.quantity_list);
        quanAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1);
        quanView.setAdapter(quanAdapter);
        setUpListViewListener(quanView);
        if (items == null) {
            items = new ArrayList<>();
            items.add(new CartItem("empty cart", 0, 0));
        }
        for(int i = 0; i < items.size(); i++) {
            this.addItem(this.getView(), "" + items.get(i).quantity, quanAdapter);
        }
        for(int i = 0; i < items.size(); i++) {
            this.addItem(this.getView(), items.get(i).name, nameAdapter);
        }
        for(int i = 0; i < items.size(); i++) {
            this.addItem(this.getView(), "" + items.get(i).price, priceAdapter);
        }

        return root;
    }

    String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime()).concat(" GMT");
    }



    private void setUpListViewListener(ListView lv) {
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getContext();
                Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show();

                nameAdapter.remove(nameList.get(i));
                priceAdapter.remove(priceList.get(i));
                quanAdapter.remove(quanList.get(i));

                nameList.remove(i);
                priceList.remove(i);
                quanList.remove(i);

                nameAdapter.notifyDataSetChanged();
                priceAdapter.notifyDataSetChanged();
                quanAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    private void addItem(View view, String str, ArrayAdapter adapter) {
        if (!(str.equals(""))) {
            adapter.add(str);
            adapter.notifyDataSetChanged();
        }
    }
//    /**
//     * Called when a view has been clicked.
//     *
//     * @param v The view that was clicked.
//     */
//    @Override
//    public void onClick(View v) {
//
//    }

    public void getAndBuildCartJSON() {
        Response response = null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://gateway-staging.ncrcloud.com/emerald/selling-service/v1/carts/rdfvFVWJq0euHPrwO9PThQ/items")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "AccessKey 48ea19ed04ad4ae7811f57f307faed31:6hkgiY8mJKNUk8ZziHArIX6jYrEdvjsSqOM55ME/PQ8IahsmP3gNwV/onLWbDQvP/w6FiCDzzFgvkkp3JdAc2A==")
                .addHeader("nep-organization", "553888f84fdb4ce481dfea21e3202930")
                .addHeader("nep-enterprise-unit", "fe2c3bf9a4194797a234159c1cd34797")
                .addHeader("Date", "Sun, 18 Oct 2020 01:56:30 GMT")
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