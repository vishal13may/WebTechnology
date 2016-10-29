package aynctask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import data.StockDetail;

/**
 * Created by Vishal on 03-May-16.
 */
public abstract class FavoriteStockDetailsAsync extends AsyncTask<String, Void, ArrayList<StockDetail>> implements CallbackReciever {

    public abstract void receiveData(Object object);

    @Override
    protected void onPostExecute(ArrayList<StockDetail> favoriteStockDetailList) {
        receiveData(favoriteStockDetailList);
    }

    @Override
    protected ArrayList<StockDetail> doInBackground(String... params) {

        ArrayList<StockDetail> favoriteStockDetailList = new ArrayList<>();

        HttpURLConnection urlConnection = null;

        URL url;

        String stockSearchURL = "http://stockinfo-1272.appspot.com/stockInfo.php?symbol=";

        try {
            String favoriteStock = params[0];
            JSONArray jArray = new JSONArray(favoriteStock);
            for (int i = 0; i < jArray.length(); i++) {

                url = new URL(stockSearchURL + jArray.get(i));

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                char[] buffer = new char[urlConnection.getContentLength()];
                isw.read(buffer);
                String result = new String(buffer);

                Log.v("My Response :: ", result);
                JSONObject jsonObject = new JSONObject(result);
                StockDetail stockDetail = new StockDetail();
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
                        favoriteStockDetailList.add(stockDetail);
                    } else {
                        stockDetail.setStatus("Error");
                        favoriteStockDetailList.add(stockDetail);
                    }
                } else {
                    stockDetail.setStatus("Error");
                    favoriteStockDetailList.add(stockDetail);
                }
            }
        } catch (Exception e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return favoriteStockDetailList;
    }
}


