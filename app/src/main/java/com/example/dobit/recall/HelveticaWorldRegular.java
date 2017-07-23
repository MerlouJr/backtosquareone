package com.example.dobit.recall;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by dobit on 7/23/2017.
 */

public class HelveticaWorldRegular extends android.support.v7.widget.AppCompatTextView {
    public HelveticaWorldRegular(Context context) {
        super(context);
        init();
    }

    public HelveticaWorldRegular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelveticaWorldRegular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaWorld-Regular.ttf");
        setTypeface(tf ,1);

    }
}
