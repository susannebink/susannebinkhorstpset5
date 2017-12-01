package com.example.susanne.susannebinkhorst_pset5;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Susanne on 30-11-2017.
 */

public class ItemAdapter extends ResourceCursorAdapter {
    Context c;

    public ItemAdapter(Context context, Cursor cursor){
        super(context, R.layout.item_layout, cursor, 0);
        c = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ImageView image = view.findViewById(R.id.dish_image);
        TextView item = view.findViewById(R.id.item);
        TextView price = view.findViewById(R.id.price);

        Integer index_item = cursor.getColumnIndex("name");
        Integer index_price = cursor.getColumnIndex("price");
        Integer index_url = cursor.getColumnIndex("image_url");

        RequestQueue queue = Volley.newRequestQueue(c);
        String url = cursor.getString(index_url);

        String value_item = cursor.getString(index_item);
        Float value_price = cursor.getFloat(index_price);


        item.setText(value_item);
        price.setText("$" + value_price);

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                image.setImageBitmap(response);
            }
        }, 0, 0, null, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("kutzooi");
            }
        }
        );
        queue.add(imageRequest);
    }

}
