package com.hackgt7.portablecheckout.ui.notifications;

import android.content.Context;
import android.os.Bundle;

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

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {

//    private Button getCart;
    private NotificationsViewModel notificationsViewModel;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        notificationsViewModel =
//                new ViewModelProvider(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);

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

        for(int i = 0; i < quanList.size(); i++) {
            this.addItem(this.getView(), quanList.get(i), quanAdapter);
        }
        for(int i = 0; i < nameList.size(); i++) {
            this.addItem(this.getView(), nameList.get(i), nameAdapter);
        }
        for(int i = 0; i < priceList.size(); i++) {
            this.addItem(this.getView(), priceList.get(i), priceAdapter);
        }

        return root;
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


}