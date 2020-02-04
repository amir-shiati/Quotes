package com.amirshiati.quotes.helper;

import android.app.Activity;
import android.content.Context;
import android.transition.Fade;;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amirshiati.quotes.R;
import com.amirshiati.quotes.consts.Consts;
import com.amirshiati.quotes.models.Quotes;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LikeActions {
    private String TAG = "LikeActions: ";
    public ArrayList<Long> likes = new ArrayList<>();

    private Context context;
    private Activity activity;

    private TextView likeCounter;
    private CheckBox likeBox;

    private TinyDB tinyDB;

    public LikeActions(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        tinyDB = new TinyDB(context);
        tinyDB.clear();
        getLikesList();
        likeCounter = activity.findViewById(R.id.like_counter);
        likeBox = (CheckBox) activity.findViewById(R.id.like_btn);
    }

    public void addLike(Quotes likedQuote) {
        if (likedQuote.isLiked())
            return;
        addToLikeCounter();
        putLike(likedQuote);
        addToLikeLists(likedQuote.getId());
        likedQuote.setLiked(true);
        likedQuote.setLikes(likedQuote.getLikes() + 1);
        likeBox.setChecked(true);
    }

    public void undoLike(Quotes quote) {
        quote.setLiked(false);
        quote.setLikes(quote.getLikes() - 1);
    }

    public void setLikeCounter(Quotes quote) {
        likeCounter.setVisibility(View.INVISIBLE);
        likeCounter.setText(String.valueOf(quote.getLikes()));
        TransitionManager.beginDelayedTransition((RelativeLayout) activity.findViewById(R.id.btns_container), new Fade());
        likeCounter.setVisibility(View.VISIBLE);
        likeBox.setChecked(quote.isLiked());
    }

    private void addToLikeCounter() {
        likeCounter.setVisibility(View.INVISIBLE);
        likeCounter.setText(String.valueOf(Integer.parseInt(likeCounter.getText().toString()) + 1));
        TransitionManager.beginDelayedTransition((RelativeLayout) activity.findViewById(R.id.btns_container), new Fade());
        likeCounter.setVisibility(View.VISIBLE);
    }

    private void getLikesList() {
        likes = tinyDB.getListLong(Consts.likeListDBName);
    }

    private void addToLikeLists(long toAdd) {
        likes.add(toAdd);
        tinyDB.putListLong(Consts.likeListDBName, likes);
    }

    private void putLike(final Quotes quote) {
        String url = Consts.addLikeURL + quote.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    int result = response.getInt("roweffected");
                    if (result == 0)
                        didntLike(quote);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());
                didntLike(quote);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", String.format("Basic %s", Base64.encodeToString(String.format("%s:%s", Consts.userName, Consts.passWord).getBytes(), Base64.DEFAULT)));
                params.put("username", Consts.userName);
                params.put("password", Consts.passWord);
                return params;
            }

            @Override
            public Request.Priority getPriority() {
                return Request.Priority.HIGH;
            }
        };
        jsonObjectRequest.setRetryPolicy(new
                DefaultRetryPolicy(
                Consts.requestTimeOutMilliSecondes,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingletone.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    private void didntLike(Quotes quote) {
        Toast.makeText(context, Consts.likeErr_mgs, Toast.LENGTH_SHORT).show();
        quote.setLiked(false);
        quote.setLikes(quote.getLikes() - 1);
    }
}

