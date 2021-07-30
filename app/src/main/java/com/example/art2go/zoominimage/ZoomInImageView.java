package com.example.art2go.zoominimage;

import android.content.Context;
import android.util.AttributeSet;

/**
 * A zoomable ImageView for Android, can be used in AdapterView and Recyclerview.
 * */
public class ZoomInImageView extends androidx.appcompat.widget.AppCompatImageView  {


    ZoomInImageViewAttacher  attacher;

    public ZoomInImageView(Context context) {
        this(context,null);

    }

    public ZoomInImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }


    public ZoomInImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        attacher  = new ZoomInImageViewAttacher(this);




    }






}