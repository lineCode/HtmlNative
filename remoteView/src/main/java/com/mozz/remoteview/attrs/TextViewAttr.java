package com.mozz.remoteview.attrs;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class TextViewAttr implements Attr {

    private static TextViewAttr sInstance = new TextViewAttr();

    public static TextViewAttr getInstance() {
        return sInstance;
    }

    @Override
    public void apply(Context context, View v, String params, Object value) {
        TextView textView = (TextView) v;
        if (params.equals("color")) {
            try {
                int color = Color.parseColor(value.toString());
                textView.setTextColor(color);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else if (params.equals("text")) {
            textView.setText(value.toString());
        } else if (params.equals("fontSize") && value instanceof Number) {
            float size = ((Number) value).floatValue();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        } else if (params.equals("lineHeight") && value instanceof Number) {
            float lineHeight = 1.f;
            if (value instanceof Integer) {
                lineHeight = (float) (int) value;
            } else if (value instanceof Float) {
                lineHeight = (float) value;
            }
            textView.setLineSpacing(lineHeight, 0);
        }
    }
}
