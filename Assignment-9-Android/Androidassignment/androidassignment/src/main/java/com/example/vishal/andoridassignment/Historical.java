package com.example.vishal.andoridassignment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import data.StockDetail;


public class Historical extends Fragment {

    private StockDetail stockDetailToBeDisplayed;

    public void setStockDetailToBeDisplayed(StockDetail stockDetailToBeDisplayed) {
        this.stockDetailToBeDisplayed = stockDetailToBeDisplayed;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historical, container, false);
        final WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/Areachart.html");
        final  String str = stockDetailToBeDisplayed.getSymbol();
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                webView.loadUrl("javascript:loadHistoricChart('" + str + "')");
            }
        });
        return view;
    }
}
