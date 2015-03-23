package pt.isel.g11.hagreve;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


public class MainActivity extends ActionBarActivity {

    private static ArrayList<Strike> strikes;
    private static ArrayList<Company> companies;

    private String uri;
    private SharedPreferences sp;
    private static final int SETTINGS = 0, FILTER = 1;
    private ArrayAdapter<Strike> adapter;
    private CustomAdapterCompanies adapterCompanies;
    private ListView listView;
    private static Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("pt.isel.a36238.hagreve", MODE_PRIVATE);
        uri = sp.getString("uri", "http://hagreve.com/api/v2/strikes");
        res = getResources();
        strikes = new ArrayList<Strike>();
        companies = new ArrayList<Company>();
        adapter = new CustomAdapter(this,strikes);
        adapterCompanies = new CustomAdapterCompanies(this, companies);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        getCompanies();
        if(strikes.isEmpty()){
            refreshStrikes();

            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(getBaseContext(), StrikeDetailsActivity.class);
                    //Strike s =  adapter.getItem(position);

                    intent.putExtra("detail", position);
                    startActivity(intent);
                }
            };
            listView.setOnItemClickListener(listener);
        }
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshStrikes();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_filters:
                openFilters();
                return true;
            case R.id.action_exit:
                appExit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void  onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case SETTINGS:
                    uri = data.getStringExtra("uri");
                    break;
                case FILTER:
                    //sp.edit().put
                    break;
                default:
                    break;
            }
        }
    }

    private void appExit() {
        this.finish();
    }

    @Override
    public void onStop(){
        super.onStop();
        sp.edit().putString("uri",uri).commit();
    }




  /*  public void openStrikeDetails(View view) {
        Intent intent = new Intent(this, StrikeDetailsActivity.class);
        intent.putExtra("detail", strikes);
        startActivity(intent);
    }*/

    private void openFilters() {
        Intent intent = new Intent(this,FilterActivity.class);
        intent.putExtra("uri", uri);
        startActivity(intent);
        //startActivityForResult(intent, FILTER);
    }

    private void openSettings() {
        Intent intent = new Intent(this,SettingsActivity.class);
        intent.putExtra("uri", uri);
        startActivityForResult(intent, SETTINGS);
    }

    private void refreshStrikes() {
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL(uri);
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
                    Log.e("PDM",e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void onPostExecute(String resp){
                try {
                    parseJSON(resp);
                } catch (JSONException e) {
                    Log.d("main_activity", "onCreate");
                    throw new RuntimeException(e);
                }
                adapter.notifyDataSetChanged();
            }
        }.execute();
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
                    Log.e("PDM",e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void onPostExecute(String resp){
                try {
                    parseJSONCompanies(resp);
                } catch (JSONException e) {
                    Log.d("main_activity", "onCreate");
                    throw new RuntimeException(e);
                }
                adapterCompanies.notifyDataSetChanged();
            }
        }.execute();
    }

    private void parseJSON(String json) throws JSONException {
        strikes.clear();
        JSONArray jsonArr = new JSONArray(json);
        for(int i = 0; i< jsonArr.length(); ++i){
            JSONObject obj = jsonArr.getJSONObject(i);
            strikes.add(new Strike(
                            obj.getString("description"),
                            obj.getString("end_date"),
                            obj.getString("source_link"),
                            obj.getBoolean("all_day"),
                            obj.getString("start_date"),
                            obj.getBoolean("canceled"),
                            obj.getJSONObject("company").getString("name"))
            );
        }
    }

    private void parseJSONCompanies(String json) throws JSONException {
        companies.clear();
        JSONArray jsonArr = new JSONArray(json);
        for(int i = 0; i< jsonArr.length(); ++i){
            JSONObject obj = jsonArr.getJSONObject(i);
            companies.add(new Company(obj.getString("name")));
        }
    }

    public static Strike getStrike(int i) {
        return strikes.get(i);
    }

    public  static String getStringFromResources(int id){
        return res.getString(id);
    }

    public static int getResourceID(String s, Context c){
        return res.getIdentifier((s + "logo").toLowerCase().replaceAll("\\s+", ""), "drawable", c.getPackageName());
    }

}
