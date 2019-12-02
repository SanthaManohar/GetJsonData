package basicandroid.com.getjsondata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button gotoRichestManApp;

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String url = "https://api.myjson.com/bins/18u9di";

    private ProgressDialog pDialog;
    private ListView listView;
    private List<Place> placeList = new ArrayList<>();
    private CustomListAdaptor adaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gotoRichestManApp = (Button) findViewById(R.id.richestmanlink);

        gotoRichestManApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Use package name which we want to check
                boolean isAppInstalled = appInstalledOrNot("androidtraining.com.richestman");

                if(isAppInstalled) {
                    //This intent will help you to launch if the package is already installed
                    Intent gotoRichestManAppIntent = getPackageManager().getLaunchIntentForPackage("androidtraining.com.richestman");
                    startActivity(gotoRichestManAppIntent);

                } else {
                    // Do whatever we want to do if application not installed
                    // For example, Redirect to play store

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=androidtraining.com.richestman&hl=en_IN")));
                }


            }
        });

        listView = (ListView)findViewById(R.id.list_view_id);
        adaptor = new CustomListAdaptor(this,placeList);
        listView.setAdapter(adaptor);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,response.toString());
                hidePDialog();

                try {
                    JSONArray keralaPlaceArray = response.getJSONArray("kerala");

                    for(int i=0;i<keralaPlaceArray.length();i++){

                        JSONObject object = keralaPlaceArray.getJSONObject(i);

                        Place place = new Place();

                        place.setThumbnailUrl(object.getString("image"));
                        place.setPlace(object.getString("place"));
                        place.setDescription(object.getString("description"));
                        place.setBesttime(object.getString("besttime"));
                        place.setAirport(object.getString("airport"));
                        place.setRailwaystation(object.getString("railwaystation"));

                        placeList.add(place);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adaptor.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error:"+error.getMessage());
                hidePDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(request);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {

        if(pDialog != null){
            pDialog.dismiss();
            pDialog = null;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.feedback) {


            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"msanthakumar@hitasoft.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Feedbacks of GetJsonData App");
            i.putExtra(Intent.EXTRA_TEXT   , "body of email");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }



}