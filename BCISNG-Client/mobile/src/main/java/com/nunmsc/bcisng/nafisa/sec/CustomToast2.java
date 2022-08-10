package com.nunmsc.bcisng.nafisa.sec;

/**
 * Created by Ismaila Lukman on 9/24/2017.
 */


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast2 {

// Custom Toast Method
public void Show_Toast(Context context, View view, String error) {

    // Layout Inflater for inflating custom view
    LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // inflate the layout over view
    View layout = inflater.inflate(R.layout.custom_toast2, (ViewGroup) view.findViewById(R.id.toast_root2));

    // Get TextView id and set error
    TextView text = (TextView) layout.findViewById(R.id.toast_error2);
    text.setText(error);

    Toast toast = new Toast(context);// Get Toast Context
    toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
    // Toast
    // gravity
    // and
    // Fill
    // Horizoontal

    toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
    toast.setView(layout); // Set Custom View over toast

    toast.show();// Finally show toast
}

}
