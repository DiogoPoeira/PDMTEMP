package pt.isel.g11.hagreve;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    private String uri;
    private SharedPreferences sp;
    private static final int SETTINGS = 0;
    private ArrayAdapter<Strike> adapter;
    private ListView listView;
    private static Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("pt.isel.a36238.hagreve",MODE_PRIVATE);
        uri = sp.getString("uri", "http://hagreve.com/api/v2/strikes");
        res = getResources();
        strikes = new ArrayList<Strike>();
        adapter = new CustomAdapter(this,strikes);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        if(strikes.isEmpty()){
            refreshStrikes();
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

    private void openFilters() {

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

    private void parseJSON(String json) throws JSONException {
        strikes.clear();
        JSONArray jsonArr = new JSONArray(json);
        for(int i = 0; i< jsonArr.length(); ++i){
            JSONObject obj = jsonArr.getJSONObject(i);
            strikes.add(new Strike(obj.getString("description"),
                    obj.getString("end_date"),
                    obj.getString("source_link"),
                    obj.getBoolean("all_day"),
                    obj.getString("start_date"),
                    obj.getBoolean("canceled"),
                    obj.getJSONObject("company").getString("name"))
            );
        }
    }

    public static Strike getStrike(int i) {
        return strikes.get(i);
    }

    public  static String getStringFromResources(int id){
        return res.getString(id);
    }
}
