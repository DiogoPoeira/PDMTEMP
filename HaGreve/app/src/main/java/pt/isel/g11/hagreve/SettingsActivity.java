package pt.isel.g11.hagreve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Diogo on 20-03-2015.
 */
public class SettingsActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        EditText edt = (EditText) findViewById(R.id.uriEdit);
        edt.setText(intent.getStringExtra("uri"));
    }

    public void onBackPressed(){
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    public void saveSettings(View view){
        Intent intent = getIntent();
        intent.putExtra("uri",((EditText) findViewById(R.id.uriEdit)).getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
