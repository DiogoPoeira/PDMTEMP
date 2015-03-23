package pt.isel.g11.hagreve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Susana on 22-Mar-15.
 */
public class CustomAdapterCompanies extends BaseAdapter {

    private Context context;
    private ArrayList<Company> companies;
    private LayoutInflater inflater;

    public CustomAdapterCompanies(Context context, ArrayList<Company> companies) {
        this.companies = companies;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final int idx = i;
        View newView = view;
        if (newView == null) {
            newView = inflater.inflate(R.layout.filter_row, parent, false);
        }
        Company company = (Company)getItem(i);

        ((TextView) newView.findViewById(R.id.company_name)).setText(company.getName());

        CheckBox cb = (CheckBox) newView.findViewById(R.id.company_checkBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               companies.get(idx).setSelected(isChecked);
            }
        });
        cb.setTag(i);
        cb.setChecked(company.isSelected());
        return newView;
    }

    @Override
    public int getCount() {
        return companies.size();
    }

    @Override
    public Object getItem(int arg0) {
        return companies.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }



}
