package com.amirshiati.quotes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.amirshiati.quotes.consts.Consts;
import com.amirshiati.quotes.helper.RequestQueueSingletone;
import com.amirshiati.quotes.helper.HandGestures;
import com.amirshiati.quotes.models.Quotes;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllQuotesActivity extends AppCompatActivity {
    private String TAG = "AllQuotesActivity = ";
    private List<Quotes> allQuotes = new ArrayList<>();

    private int quoteToShow = 0;

    private TextSwitcher quoteTextView;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quotes);

        //main container background used for hand gestures and gradiant animations
        relativeLayout = (RelativeLayout) findViewById(R.id.main_container_background);
        quoteTextView = (TextSwitcher) findViewById(R.id.quote_text_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();

        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);

        quoteTextView.setInAnimation(this, R.anim.slide_up);
        quoteTextView.setOutAnimation(this, R.anim.slid_down);

        animationDrawable.start();
        getAllQuotes();

    }

    private void updateQuoteContainer() {
        quoteTextView.setText(allQuotes.get(quoteToShow).getQuote());
    }

    private boolean updateQuotesContainer(boolean swipedUp) {
        if (swipedUp && quoteToShow < (allQuotes.size() - 1)) {
            quoteToShow++;
            return true;
        }

        if (!swipedUp && quoteToShow > 0) {
            quoteToShow--;
            return true;
        }

        return false;

    }

    public void getAllQuotes() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Consts.getAllQuotesURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        JSONObject writerObject = object.getJSONObject("writer");
                        allQuotes.add(new Quotes(object.getLong("id"), object.getString("quote"),
                                object.getLong("likes"), object.getString("subject"), writerObject.getString("firstname"), writerObject.getString("lastname")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setContainerSwipGestures();
                quoteTextView.setCurrentText(allQuotes.get(0).getQuote());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());
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
            public Priority getPriority() {
                return Priority.HIGH;
            }
        };

        jsonArrayRequest.setRetryPolicy(new

                DefaultRetryPolicy(
                Consts.requestTimeOutMilliSecondes,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingletone.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void setContainerSwipGestures() {
        //swipe gestures
        relativeLayout.setOnTouchListener(new HandGestures(AllQuotesActivity.this) {
            public void onSwipeTop() {
                if (updateQuotesContainer(true))
                    updateQuoteContainer();
            }

            public void onSwipeRight() {
                Toast.makeText(AllQuotesActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(AllQuotesActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                if (updateQuotesContainer(false))
                    updateQuoteContainer();
            }
        });
    }
}
