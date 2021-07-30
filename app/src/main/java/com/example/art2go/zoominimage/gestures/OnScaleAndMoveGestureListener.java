package com.example.art2go.zoominimage.gestures;

import android.view.ScaleGestureDetector;

public interface OnScaleAndMoveGestureListener {

    void onScaleBegin(ScaleGestureDetector detector);

    void onScaleEnd(ScaleGestureDetector detector, float moveDistanceFromX, float moveDistanceFromY);

    void onScaleAndMove(ScaleGestureDetector detector, float currentScale, float moveDistanceFromX, float moveDistanceFromY);


}