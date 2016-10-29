package com.example.vishal.andoridassignment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import adapter.StockInfoAdapter;
import data.StockDetail;
import uk.co.senab.photoview.PhotoViewAttacher;


public class Current extends Fragment {

    private StockDetail stockDetailToBeDisplayed;

    public void setStockDetailToBeDisplayed(StockDetail stockDetailToBeDisplayed) {
        this.stockDetailToBeDisplayed = stockDetailToBeDisplayed;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current, container, false);

        // adapter to polulate listview


        Log.d("MyApp********", "Data received in Current Fragment is : " + stockDetailToBeDisplayed.toString());

        ArrayList<String> lst = new ArrayList<String>();

        lst.add("NAME#"+stockDetailToBeDisplayed.getName());

        lst.add("SYMBOL#"+stockDetailToBeDisplayed.getSymbol());

        lst.add("LASTPRICE#" + stockDetailToBeDisplayed.getLastPrice());

        Double change = Double.parseDouble(stockDetailToBeDisplayed.getChange());
        change = Math.round(change * 100.0) / 100.0;

        Double percentChange = Double.parseDouble(stockDetailToBeDisplayed.getChangePercent());
        percentChange = Math.round(percentChange * 100.0) / 100.0;

        if(change > 0.00){
            lst.add("CHANGE#"+change.toString()+"(+"+percentChange.toString()+"%)"+"#"+"UP");
        }else if(change < 0.00){
            lst.add("CHANGE#"+change.toString()+"("+percentChange.toString()+"%)"+"#"+"DOWN");
        }else{
            lst.add("CHANGE#"+change.toString()+"("+percentChange.toString()+"%)");
        }

        try {
            DateFormat format = new SimpleDateFormat("EEE MMM d hh:mm:ss 'UTC'ZZZ yyyy", Locale.ENGLISH);
            Date timestamp = format.parse(stockDetailToBeDisplayed.getTimeStamp());
            lst.add("TIMESTAMP#" + timestamp.toString());
        }
        catch (Exception e){
            e.printStackTrace();
            lst.add("TIMESTAMP#" + stockDetailToBeDisplayed.getTimeStamp());
        }

        Double marketCap = Double.parseDouble(stockDetailToBeDisplayed.getMarketCap());

        if (marketCap / 1000000000 > 1) {
            marketCap = marketCap / 1000000000;
            marketCap = Math.round(marketCap * 100.0) / 100.0;
            lst.add("MARKETCAP#"+marketCap+" Billion");
        } else {
            marketCap = marketCap / 1000000;
            marketCap = Math.round(marketCap* 100.0) / 100.0 ;
            lst.add("MARKETCAP#"+marketCap+" Million");
        }

        Double volume = Double.parseDouble(stockDetailToBeDisplayed.getVolume());

        if (volume / 1000000000 > 1) {
            volume = volume / 1000000000;
            volume = Math.round(volume * 100.0) / 100.0;
            lst.add("VOLUME#"+volume+" Billion");
        } else {
            volume = volume / 1000000;
            volume = Math.round(volume* 100.0) / 100.0 ;
            lst.add("VOLUME#"+volume+" Million");
        }

       // lst.add("VOLUME#"+stockDetailToBeDisplayed.getVolume());

        Double changeYTD = Double.parseDouble(stockDetailToBeDisplayed.getChangeYTD());
        changeYTD = Math.round(change * 100.0) / 100.0;

        Double percentChangeYTD = Double.parseDouble(stockDetailToBeDisplayed.getChangePercentYTD());
        percentChangeYTD = Math.round(percentChange * 100.0) / 100.0;

        if(changeYTD > 0.00){
            lst.add("CHANGE YTD#"+changeYTD.toString()+"(+"+percentChangeYTD.toString()+"%)"+"#"+"UP");
        }else if(change < 0.00){
            lst.add("CHANGE YTD#"+changeYTD.toString()+"("+percentChangeYTD.toString()+"%)"+"#"+"DOWN");
        }else{
            lst.add("CHANGE YTD#"+changeYTD.toString()+"("+percentChangeYTD.toString()+"%)");
        }

        Double high = Double.parseDouble(stockDetailToBeDisplayed.getHigh());
        high = Math.round(high * 100.0) / 100.0;
        lst.add("HIGH#"+high.toString());

        Double low = Double.parseDouble(stockDetailToBeDisplayed.getLow());
        low = Math.round(high * 100.0) / 100.0;
        lst.add("LOW#"+low.toString());

        Double open = Double.parseDouble(stockDetailToBeDisplayed.getOpen());
        open = Math.round(high * 100.0) / 100.0;
        lst.add("OPEN#" + open.toString());

        String symbolValue = stockDetailToBeDisplayed.getSymbol();

        lst.add("YAHOO#" + symbolValue);
        ListView listView = (ListView) view.findViewById(R.id.listView);

        StockInfoAdapter adapter = new StockInfoAdapter(getContext(),lst);

        adapter.setBitmap(stockDetailToBeDisplayed.getBitmap());

        listView.setAdapter(adapter);


        return view;
    }

}
