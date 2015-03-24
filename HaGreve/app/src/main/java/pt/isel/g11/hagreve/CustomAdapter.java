package pt.isel.g11.hagreve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Diogo on 21-03-2015.
 */
public class CustomAdapter extends ArrayAdapter<Strike> {

    private Context context;
    private String allDay,parcial;
    public CustomAdapter(Context context, List<Strike> strikes) {
        super(context, R.layout.list_row, R.id.company, strikes);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_row,parent,false);
        Strike s = MainActivity.getStrike(position);
        TextView company = (TextView) row.findViewById(R.id.company),
                 desc = (TextView) row.findViewById(R.id.desc),
                 dates = (TextView) row.findViewById(R.id.dates);


        company.setText(s.getCompany());
        desc.setText(s.getDescription());
        dates.setText(s.getDates());


        return row;
    }
}
