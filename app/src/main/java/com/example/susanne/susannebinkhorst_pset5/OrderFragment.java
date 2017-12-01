package com.example.susanne.susannebinkhorst_pset5;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends DialogFragment implements View.OnClickListener, ListView.OnItemLongClickListener{
    RestoDatabase d;
    RestoAdapter restoAdapter;
    ListView list;
    TextView total;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        total = v.findViewById(R.id.total_price);

        d = RestoDatabase.getInstance(getActivity().getApplicationContext());

        list = v.findViewById(R.id.listView);
        list.setOnItemLongClickListener(this);

        Button cancel = v.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        Button place_order = v.findViewById(R.id.place_order);
        place_order.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:{
                d.clear();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.place_order:{
                Order();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    public void Order(){
        d.clear();
        String url = "https://resto.mprog.nl/order";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String minutes = response.getString("preparation_time");
                            Toast.makeText(getContext(),"Estimated waiting time is " + minutes + " minutes", Toast.LENGTH_LONG).show();
                        } catch (JSONException e){
                            Toast.makeText(getContext(),"that didn't work", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor item = (Cursor) list.getItemAtPosition(i);
        Integer name_index = item.getColumnIndex("name");
        String name = item.getString(name_index);
        System.out.println(name);
        d.delete(name);

        Cursor order = d.selectAll();
        Float total_price = d.selectSum();
        total.setText("Total price:             $"+total_price);

        restoAdapter = new RestoAdapter(getActivity(), order);
        list.setAdapter(restoAdapter);

        return true;
    }

    @Override
    public void onViewStateRestored(Bundle inState) {
        super.onViewStateRestored(inState);

        Cursor order = d.selectAll();
        restoAdapter = new RestoAdapter(getActivity(), order);
        list.setAdapter(restoAdapter);

        Float total_price = d.selectSum();
        total.setText("Total price:             $"+total_price);
    }

}
