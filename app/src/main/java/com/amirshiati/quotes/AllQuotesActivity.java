package com.amirshiati.quotes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.RelativeLayout;

import com.amirshiati.quotes.consts.Consts;
import com.amirshiati.quotes.helper.RequestQueueSingletone;
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

    //for hand gestures
    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quotes);

        //main container background used for hand gestures and gradiant animations
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_container_background);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();

        // get the gesture detector
//        mDetector = new GestureDetector(this, new MyGestureListener());

        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);

        animationDrawable.start();
        getAllQuotes();

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
}
