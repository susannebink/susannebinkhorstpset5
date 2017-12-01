package com.example.susanne.susannebinkhorst_pset5;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {

    JsonObjectRequest request;
    JSONArray items;
    RestoDatabase database;
    String category;
    ItemAdapter itemAdapter;
    DishesDatabase dishesDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = this.getArguments();
        category = arguments.getString("category");

        dishesDatabase = DishesDatabase.getInstance(getActivity().getApplicationContext());
        dishesDatabase.onDrop();

        Cursor c = dishesDatabase.selectAll();
        itemAdapter = new ItemAdapter(getContext(), c);
        this.setListAdapter(itemAdapter);

        database = RestoDatabase.getInstance(getActivity().getApplicationContext());

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://resto.mprog.nl/menu";

        // Request a string response from the provided URL.
        request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            items = response.getJSONArray("items");

                            for (int i = 0; i < items.length(); i++) {

                                JSONObject item = items.getJSONObject(i);
                                String dish = item.getString("category");
                                if (dish.equals(category))
                                {
                                    dishesDatabase.addItem(item.getString("name"),
                                            item.getString("description"),
                                            category, item.getString("image_url"),
                                            Float.valueOf(item.getString("price")));
                                }
                            }


                            Cursor c = dishesDatabase.selectAll();
                            itemAdapter = new ItemAdapter(getContext(), c);
                            setListAdapter(itemAdapter);

                        } catch (JSONException e){
                            System.out.println("That didn't work");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work");
            }
        });
        queue.add(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onListItemClick(final ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        Cursor cursor = (Cursor) l.getItemAtPosition(position);

        Integer index_item = cursor.getColumnIndex("name");
        Integer index_price = cursor.getColumnIndex("price");

        String name = cursor.getString(index_item);
        Float price = cursor.getFloat(index_price);

        database.addItem(name, price);
        Toast.makeText(this.getContext(), name + " added to your order", Toast.LENGTH_SHORT).show();
    }

}
