package com.amirshiati.quotes.helper;

import android.app.Activity;
import android.content.Context;
import android.widget.CheckBox;

import com.amirshiati.quotes.R;

public class LikeAction {
    private Context context;
    private Activity activity;

    public LikeAction(long id, Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        changeLikeIcon();
    }

    private void changeLikeIcon() {
        CheckBox likeBox = (CheckBox) activity.findViewById(R.id.like_btn);
        likeBox.setChecked(true);
    }

}
