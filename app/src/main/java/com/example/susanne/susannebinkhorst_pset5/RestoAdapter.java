package com.example.susanne.susannebinkhorst_pset5;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.susanne.susannebinkhorst_pset5.R;

/**
 * Created by Susanne on 30-11-2017.
 */

public class RestoAdapter extends ResourceCursorAdapter {


    public RestoAdapter(Context context, Cursor cursor){
        super(context, R.layout.row_resto, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView item = view.findViewById(R.id.item);
        TextView amount = view.findViewById(R.id.amount);
        TextView price = view.findViewById(R.id.price);

        Integer index_item = cursor.getColumnIndex("name");
        Integer index_amount = cursor.getColumnIndex("amount");
        Integer index_price = cursor.getColumnIndex("price");

        String value_item = cursor.getString(index_item);
        Integer value_amount = cursor.getInt(index_amount);
        Float value_price = cursor.getFloat(index_price);

        Float total_price = value_amount*value_price;

        item.setText(value_item);
        amount.setText("" + value_amount + "x");
        price.setText("$" + total_price);

    }
}

