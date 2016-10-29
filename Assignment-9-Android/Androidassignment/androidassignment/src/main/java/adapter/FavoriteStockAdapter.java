package adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.vishal.andoridassignment.R;

import java.util.ArrayList;

import data.NewsFeedData;
import data.StockDetail;

/**
 * Created by Vishal on 03-May-16.
 */
public class FavoriteStockAdapter extends ArrayAdapter<StockDetail> {


    public FavoriteStockAdapter(Context context, ArrayList<StockDetail> dataList) {
        super(context, 0, dataList);
    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StockDetail data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favoritestock, parent, false);
        }
        // Lookup view for data population
        TextView textViewSymbol = (TextView) convertView.findViewById(R.id.textViewSymbol);
        TextView textViewCompanyName = (TextView) convertView.findViewById(R.id.textViewCompanyName);
        TextView textMarketCap = (TextView) convertView.findViewById(R.id.textViewMarketCap);
        TextView textViewStockPrice = (TextView) convertView.findViewById(R.id.textViewStockPrice);
        TextView textViewChangePercent = (TextView) convertView.findViewById(R.id.textViewChangePercent);

        textViewSymbol.setText(data.getSymbol());
        textViewCompanyName.setText(data.getName());
        textViewStockPrice.setText("$ " + data.getLastPrice());

        Double percentChange = Double.parseDouble(data.getChangePercent());
        percentChange = Math.round(percentChange * 100.0) / 100.0;
        String percentChangeString;
        if (percentChange > 0.00) {
            percentChangeString = "+" + percentChange.toString() + "%";
            textViewChangePercent.setBackgroundColor(Color.GREEN);
        } else if (percentChange < 0.00) {
            percentChangeString = percentChange.toString() + "%";
            textViewChangePercent.setBackgroundColor(Color.RED);
        } else {
            percentChangeString = percentChange.toString() + "%";
        }
        textViewChangePercent.setText(percentChangeString);

        Double marketCap = Double.parseDouble(data.getMarketCap());
        String marketCapString = "Market Cap : ";
        if (marketCap / 1000000000 > 1) {
            marketCap = marketCap / 1000000000;
            marketCap = Math.round(marketCap * 100.0) / 100.0;
            marketCapString = marketCapString + marketCap.toString() + " Billion";
        } else {
            marketCap = marketCap / 1000000;
            marketCap = Math.round(marketCap * 100.0) / 100.0;
            marketCapString = marketCapString + marketCap.toString() + " Million";
        }
        textMarketCap.setText(marketCapString);

        return convertView;
    }
}
