package com.example.dobit.recall;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by dobit on 7/23/2017.
 */

public class HelveticaWorld extends android.support.v7.widget.AppCompatTextView {
    public HelveticaWorld(Context context) {
        super(context);
        init();
    }

    public HelveticaWorld(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelveticaWorld(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaWorld-Bold.ttf");
        setTypeface(tf ,1);

    }
}
