package pt.isel.g11.hagreve;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Susana on 22-Mar-15.
 */
public class FilterActivity extends Activity {

    private static String TAG = "PDM FilterActivity";

    private static ArrayList<Company> companies;
    private CustomAdapterCompanies adapterCompanies;
    private  ListView list_companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        companies= new ArrayList<Company>();
        adapterCompanies = new CustomAdapterCompanies(this, companies);
        getCompanies();
        list_companies = (ListView) findViewById(R.id.listCompanies);
        list_companies.setAdapter(adapterCompanies);
    }

    private void getCompanies() {
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL("http://hagreve.pt/api/v2/companies");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()
                            )
                    );

                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line).append("\n");
                    }
                    conn.disconnect();
                    return response.toString();
                } catch (IOException e) {
                    Log.e(TAG + "getCompanies-doInBackground", e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void onPostExecute(String resp){
                try {
                    parseJSONCompanies(resp);
                } catch (JSONException e) {
                    Log.e(TAG + "getCompanies-onPostExecute", e.getMessage());
                    throw new RuntimeException(e);
                }
                adapterCompanies.notifyDataSetChanged();
            }
        }.execute();
    }

    private void parseJSONCompanies(String json) throws JSONException {
        Log.d(TAG, "parseJSONCompanies");
        companies.clear();
        JSONArray jsonArr = new JSONArray(json);
        for(int i = 0; i< jsonArr.length(); ++i){
            JSONObject obj = jsonArr.getJSONObject(i);
            companies.add(new Company(Integer.parseInt(obj.getString("id")), obj.getString("name")));
        }
    }

    /*public void onBackPressed(){
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }*/

    public void saveFilter(View view){
        //Intent intent = getIntent();
        //intent.putExtra();
        //setResult(RESULT_OK,intent);
        //finish();
    }
}
