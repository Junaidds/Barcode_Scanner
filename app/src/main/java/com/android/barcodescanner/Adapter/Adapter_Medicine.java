package com.android.barcodescanner.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.barcodescanner.Model.Model_Items;
import com.android.barcodescanner.R;

import java.util.List;

public class Adapter_Medicine extends BaseAdapter {
    private Activity activity;
    private List<Model_Items> model_items;
    LayoutInflater layoutInflater;

    public Adapter_Medicine(Activity activity, List<Model_Items> mdl){
        this.activity = activity;
        this.model_items = mdl;
    }
    @Override
    public int getCount() {
        return model_items.size();
    }

    @Override
    public Object getItem(int i) {
        return model_items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.item_history, null);

        TextView txt_name_product = convertView.findViewById(R.id.txt_product_name);

        final Model_Items model = model_items.get(i);

        txt_name_product.setText(model.getName());


        return convertView;
    }
}
