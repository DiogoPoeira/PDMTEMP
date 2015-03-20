package pt.isel.a36238.hagreve;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends ActionBarActivity {

    private static String response = null;
    private String uri;
    private SharedPreferences sp;
    private static final int SETTINGS = 0;
    private Filter f;
    private TextView txtMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("pt.isel.a36238.hagreve",MODE_PRIVATE);
        uri = sp.getString("uri", "http://hagreve.com/api/v2/strikes");
        f = new Filter(sp.getStringSet("filters",new TreeSet<String>()));

        setContentView(R.layout.activity_main);
        txtMsg = (TextView) findViewById(R.id.serverResponse);
        if(response==null){
            refreshStrikes();
        }
        else
            txtMsg.setText(response);

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
        sp.edit().putStringSet("filters",f.getStringSet());
    }

    private void openFilters() {

    }

    private void openSettings() {
        Intent intent = new Intent(this,SettingsActivity.class);
        intent.putExtra("uri",uri);
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
                    JSONArray strikeArray = new JSONArray(response.toString());
                    String[] strikes = new String[strikeArray.length()];
                    for(int i = 0;i<strikes.length;i++){
                        strikes[i] = strikeArray.getJSONObject(i).toString();
                    }
                    return response.toString();
                } catch (IOException e) {
                    Log.e("PDM",e.getMessage());
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    Log.e("PDM",e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void onPostExecute(String resp){
                txtMsg.setText((response=resp));
            }
        }.execute();
    }
}
