package pt.isel.g11.hagreve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Gomes on 22-Mar-15.
 */
public class FilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


    }

    /*public void onBackPressed(){
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }*/

    public void saveFilter(View view){
        Intent intent = getIntent();
        //intent.putExtra();
        setResult(RESULT_OK,intent);
        finish();
    }
}
