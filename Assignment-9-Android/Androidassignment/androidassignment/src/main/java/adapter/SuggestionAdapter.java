package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.vishal.andoridassignment.R;

import java.util.ArrayList;
import java.util.List;

import aynctask.StockSearch;
import data.Autocomplete;


public class SuggestionAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> suggestions;
    private List<Autocomplete> autocompleteList;


    public SuggestionAdapter(Activity context, String nameFilter) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        this.context = context;
        suggestions = new ArrayList<String>();
        autocompleteList = new ArrayList<Autocomplete>();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.stcoksearchview, parent, false);
        }
        String str = getItem(position);
        if(str!=null) {
            String[] arr = str.split(":");
            ((TextView) view.findViewById(R.id.textView)).setText(arr[0]);
            ((TextView) view.findViewById(R.id.textView2)).setText(arr[1]);
        }
        return view;
    }

    @Override
    public String getItem(int index) {
        return suggestions.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new Filter.FilterResults();
                if (constraint != null) {
                    suggestions.clear();
                    autocompleteList.clear();
                    try {
                        autocompleteList = new StockSearch().execute(constraint.toString()).get();
                        for(Autocomplete auto : autocompleteList){
                            suggestions.add(auto.getSymbol()+":"+ auto.getName() + " "+ "("+auto.getExchange()+")");
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        };
        return myFilter;
    }

}