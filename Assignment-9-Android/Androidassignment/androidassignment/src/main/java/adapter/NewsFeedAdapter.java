package adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vishal.andoridassignment.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.NewsFeedData;

/**
 * Created by Vishal on 01-May-16.
 */
public class NewsFeedAdapter extends ArrayAdapter<NewsFeedData> {


    public NewsFeedAdapter(Context context, ArrayList<NewsFeedData> dataList) {
        super(context, 0, dataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NewsFeedData data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news, parent, false);
        }
        // Lookup view for data population
        TextView textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
        TextView textViewPublisher = (TextView) convertView.findViewById(R.id.textViewPublisher);
        TextView textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
        TextView textViewDescription = (TextView) convertView.findViewById(R.id.textViewDescription);

        textViewTitle.setClickable(true);
        textViewTitle.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='" + data.getUrl() + "'>" + data.getTitle() + "</a>";
        textViewTitle.setText(Html.fromHtml(text));
        textViewTitle.setLinkTextColor(Color.BLACK);

        textViewPublisher.setText("Publisher :" + data.getPublisher());

        try {
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date dt = df1.parse(data.getDate());
            DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
            String d = df2.format(dt);
            textViewDate.setText("Date :" + d);
        } catch (Exception e1) {
            textViewDate.setText("Date :" + data.getDate());
        }



        textViewDescription.setText(data.getDescription());


        return convertView;
    }
}
