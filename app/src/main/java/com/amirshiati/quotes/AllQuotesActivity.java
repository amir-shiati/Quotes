package com.amirshiati.quotes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.amirshiati.quotes.consts.Consts;
import com.amirshiati.quotes.consts.Randoms;
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
import java.util.stream.Collectors;

public class AllQuotesActivity extends AppCompatActivity {
    private String TAG = "AllQuotesActivity = ";
    private List<Quotes> allQuotes = new ArrayList<>();
    private List<Quotes> quotesToShow = new ArrayList<>();

    private int quoteToShow = 0;

    private TextSwitcher quoteTextView;
    private TextSwitcher writerTextView;
    private TextSwitcher quoteTagTextView;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quotes);

        getSupportActionBar().hide();
        //set Randoms
        Randoms.setAllColors(this);
        Randoms.setTypeFaces(this);

        //main container background used for hand gestures and gradiant animations
        relativeLayout = (RelativeLayout) findViewById(R.id.main_container_background);
        RelativeLayout bgRelativeLayout = (RelativeLayout) findViewById(R.id.bg_gradiant_container);
        quoteTextView = (TextSwitcher) findViewById(R.id.quote_text_view);
        writerTextView = (TextSwitcher) findViewById(R.id.writer_text_view);
        quoteTagTextView = (TextSwitcher) findViewById(R.id.tag_of_quote_text_view);

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        AnimationDrawable bgAnimationDrawable = (AnimationDrawable) bgRelativeLayout.getBackground();
        bgAnimationDrawable.setEnterFadeDuration(5000);
        bgAnimationDrawable.setExitFadeDuration(10000);

        quoteTextView.setInAnimation(this, R.anim.slide_up);
        quoteTextView.setOutAnimation(this, R.anim.slid_down);
        writerTextView.setInAnimation(this, R.anim.slide_up);
        writerTextView.setOutAnimation(this, R.anim.slid_down);
        quoteTagTextView.setInAnimation(this, R.anim.slide_up);
        quoteTagTextView.setOutAnimation(this, R.anim.slid_down);

        animationDrawable.start();
        bgAnimationDrawable.start();
        getAllQuotes();

        quoteTagTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quotesToShow = filterQuotesByTag();
                quoteToShow = 0;
                updateQuoteContainer();
            }
        });

    }

    private List<Quotes> filterQuotesByTag() {
        List<Quotes> result = new ArrayList<>();
        for (Quotes quotes : allQuotes) {
            if (quotes.getSubject().equals(quotesToShow.get(quoteToShow).getSubject()))
                result.add(quotes);
        }
        return result;
    }

    private void setQuotesToShow(List<Quotes> toShow) {
        quotesToShow = toShow;
    }

    private void updateQuoteContainer() {
        Quotes currentQuote = quotesToShow.get(quoteToShow);
        quoteTextView.setText(Html.fromHtml(currentQuote.toString()));
        TextView currentView = (TextView) quoteTextView.getCurrentView();
        currentView.setTypeface(currentQuote.getAssignedTypeFace());
        writerTextView.setText(Html.fromHtml(currentQuote.firstAndLastName()));
        quoteTagTextView.setText(Html.fromHtml(currentQuote.getSubject()));
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
                setQuotesToShow(allQuotes);
                updateQuoteContainer();
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

    private boolean updateQuotesContainer(boolean swipedUp) {
        if (swipedUp && quoteToShow < (quotesToShow.size() - 1)) {
            quoteToShow++;
            return true;
        }

        if (!swipedUp && quoteToShow > 0) {
            quoteToShow--;
            return true;
        }

        return false;

    }

}
