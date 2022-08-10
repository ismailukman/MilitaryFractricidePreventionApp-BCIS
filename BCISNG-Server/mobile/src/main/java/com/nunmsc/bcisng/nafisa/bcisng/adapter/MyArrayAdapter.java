package com.nunmsc.bcisng.nafisa.bcisng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.nunmsc.bcisng.nafisa.bcisng.R;

/**
 * Created by Ismaila Lukman on 2/19/2018.
 */

public class MyArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MyArrayAdapter(Context context, String[] values) {
        super(context, R.layout.activity_help, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_help, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(values[position]);

        // Change icon based on name
        String s = values[position];

        System.out.println(s);

        if (s.equals("WindowsMobile")) {
            imageView.setImageResource(R.drawable.idea);
        } else if (s.equals("iOS")) {
            imageView.setImageResource(R.drawable.idea);
        } else if (s.equals("Blackberry")) {
            imageView.setImageResource(R.drawable.idea);
        } else {
            imageView.setImageResource(R.drawable.idea);
        }

        return rowView;
    }
}
