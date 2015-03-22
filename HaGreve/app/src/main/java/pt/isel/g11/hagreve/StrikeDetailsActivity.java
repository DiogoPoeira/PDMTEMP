package pt.isel.g11.hagreve;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by megs on 21/03/2015.
 */

public class StrikeDetailsActivity extends ActionBarActivity {

    private Strike s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strike_details);
        Intent i = getIntent();
        //Strike s = (Strike)i.getSerializableExtra("detail");
        int p = i.getIntExtra("detail", 0);
        s = MainActivity.getStrike(p);
        TextView tvCompany = (TextView)findViewById(R.id.companyDetails),
                 tvStartDate = (TextView)findViewById(R.id.datesDetails),
                 tvDesc = (TextView)findViewById(R.id.descDetails),
                 tvAllDay = (TextView)findViewById(R.id.allDayDetails);
        Button bSourceLink = (Button)findViewById(R.id.sourceButton);
        ImageView ivLogo = (ImageView)findViewById(R.id.company_logo);
        tvCompany.setText(s.getCompany());
        tvStartDate.setText(s.getDates());
        tvDesc.setText(s.getDescription());
        tvAllDay.setText(s.getIsParcial());
        bSourceLink.setText(R.string.source);
        int id = getResources().getIdentifier((s.getCompany()+"logo").toLowerCase().replaceAll("\\s+",""), "drawable", getPackageName());
        ivLogo.setImageResource(id);
    }

    public void openSource(View view){
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(s.getSourceLink()));
        startActivity(it);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_strike_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            addCalendarEntry();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addCalendarEntry() {

        Calendar beginTime = Calendar.getInstance(), endTime = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        String bDate = s.getStartDate(), eDate = s.getEndDate();
        String[] cd = bDate.split(" ");
        String[] d = cd[0].split("-");
        String[] h = cd[1].split(":");
        beginTime.set(Integer.parseInt(d[0]), Integer.parseInt(d[1])-1, Integer.parseInt(d[2]),
                      Integer.parseInt(h[0]), Integer.parseInt(h[1]), Integer.parseInt(h[2]));
        cd = eDate.split(" ");
        d = cd[0].split("-");
        h = cd[1].split(":");
        endTime.set(Integer.parseInt(d[0]), Integer.parseInt(d[1])-1, Integer.parseInt(d[2]),
                    Integer.parseInt(h[0]), Integer.parseInt(h[1]), Integer.parseInt(h[2]));
        intent.putExtra("beginTime", beginTime.getTimeInMillis());
        intent.putExtra("endTime", endTime.getTimeInMillis());
        intent.putExtra("title", s.getCompany());
        intent.putExtra("allDay", s.isParcial());
        startActivity(intent);

    }
}
