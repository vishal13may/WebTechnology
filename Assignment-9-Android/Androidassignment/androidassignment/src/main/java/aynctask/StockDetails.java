package aynctask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import data.StockDetail;


public class StockDetails extends AsyncTask<String, Void, StockDetail> {
    @Override
    protected StockDetail doInBackground(String... params) {

        StockDetail stockDetail = new StockDetail();

        Log.v("Parameter", params.toString());

        HttpURLConnection urlConnection = null;

        URL url;

        try {

            String stockSearchURL = "http://stockinfo-1272.appspot.com/stockInfo.php?symbol=" + params[0];
            url = new URL(stockSearchURL);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            char[] buffer = new char[urlConnection.getContentLength()];
            isw.read(buffer);
            String result = new String(buffer);

            Log.v("My Response :: ", result);
            JSONObject jsonObject = new JSONObject(result);

            if (jsonObject.has("Status")) {
                if (jsonObject.getString("Status").equalsIgnoreCase("SUCCESS")) {
                    stockDetail.setSymbol(jsonObject.getString("Symbol"));
                    stockDetail.setName(jsonObject.getString("Name"));
                    stockDetail.setLastPrice(jsonObject.getString("LastPrice"));
                    stockDetail.setChange(jsonObject.getString("Change"));
                    stockDetail.setChangePercent(jsonObject.getString("ChangePercent"));
                    stockDetail.setTimeStamp(jsonObject.getString("Timestamp"));
                    stockDetail.setmSDate(jsonObject.getString("MSDate"));
                    stockDetail.setMarketCap(jsonObject.getString("MarketCap"));
                    stockDetail.setVolume(jsonObject.getString("Volume"));
                    stockDetail.setChangeYTD(jsonObject.getString("ChangeYTD"));
                    stockDetail.setChangePercentYTD(jsonObject.getString("ChangePercentYTD"));
                    stockDetail.setHigh(jsonObject.getString("High"));
                    stockDetail.setLow(jsonObject.getString("Low"));
                    stockDetail.setOpen(jsonObject.getString("Open"));
                    stockDetail.setStatus(jsonObject.getString("Status"));

                } else {
                    stockDetail.setStatus("Error");
                }
            } else {
                stockDetail.setStatus("Error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return stockDetail;
    }
}

