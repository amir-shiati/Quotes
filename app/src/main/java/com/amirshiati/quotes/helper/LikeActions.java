package com.amirshiati.quotes.helper;

import android.app.Activity;
import android.content.Context;
import android.transition.Fade;;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amirshiati.quotes.R;
import com.amirshiati.quotes.models.Quotes;

public class LikeActions {
    private Context context;
    private Activity activity;

    private TextView likeCounter;

    public LikeActions(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        likeCounter = activity.findViewById(R.id.like_counter);
    }

    public void addLike() {
        addToLikeCounter();
        changeLikeIcon();
    }

    public void setLikeCounter(Quotes quotes) {
        likeCounter.setVisibility(View.INVISIBLE);
        likeCounter.setText(String.valueOf(quotes.getLikes()));
        TransitionManager.beginDelayedTransition((RelativeLayout) activity.findViewById(R.id.btns_container), new Fade());
        likeCounter.setVisibility(View.VISIBLE);
    }

    private void changeLikeIcon() {
        CheckBox likeBox = (CheckBox) activity.findViewById(R.id.like_btn);
        likeBox.setChecked(true);
    }

    private void addToLikeCounter() {
        likeCounter.setVisibility(View.INVISIBLE);
        likeCounter.setText(String.valueOf(Integer.parseInt(likeCounter.getText().toString()) + 1));
        TransitionManager.beginDelayedTransition((RelativeLayout) activity.findViewById(R.id.btns_container), new Fade());
        likeCounter.setVisibility(View.VISIBLE);
    }

}
