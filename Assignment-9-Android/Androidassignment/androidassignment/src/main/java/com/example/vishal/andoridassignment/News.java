package com.example.vishal.andoridassignment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.NewsFeedAdapter;
import aynctask.NewsFeedAsync;
import data.NewsFeedData;
import data.StockDetail;


public class News extends Fragment {

    private StockDetail stockDetailToBeDisplayed;

    public void setStockDetailToBeDisplayed(StockDetail stockDetailToBeDisplayed) {
        this.stockDetailToBeDisplayed = stockDetailToBeDisplayed;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        try {
            ArrayList<NewsFeedData> newsFeedDataList = new NewsFeedAsync().execute(stockDetailToBeDisplayed.getSymbol()).get();

            ListView listView = (ListView) view.findViewById(R.id.newsListView);

            NewsFeedAdapter adapter = new NewsFeedAdapter(getContext(),newsFeedDataList);

            listView.setAdapter(adapter);

        }catch(Exception e){

        }

        return view;

    }
}
