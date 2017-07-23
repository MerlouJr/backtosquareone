package com.example.dobit.recall;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by dobit on 7/23/2017.
 */

public class HelveticaRounded extends android.support.v7.widget.AppCompatTextView {
    public HelveticaRounded(Context context) {
        super(context);
        init();
    }

    public HelveticaRounded(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelveticaRounded(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaRoundedLTStd-Bd.otf");
        setTypeface(tf ,1);

    }

}
