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
import java.util.List;

import data.Autocomplete;

/**
 * Created by Vishal on 30-Apr-16.
 */
public class StockSearch extends AsyncTask<String, Void, List<Autocomplete>> {
    @Override
    protected List<Autocomplete> doInBackground(String... params) {

        //ArrayList<String> autoComplete = new ArrayList<String>();

        List<Autocomplete> autocompleteList = new ArrayList<Autocomplete>();

        Log.v("Parameter", params.toString());
        HttpURLConnection urlConnection = null;
        URL url;
        try {

            // String stockSearchURL = "http://stockinfo-1272.appspot.com/stockInfo.php?autoCompleteSymbol="+params[0];

            String stockSearchURL = "http://stockinfo-1272.appspot.com/stockInfo.php?autoCompleteSymbol=" + params[0];
            url = new URL(stockSearchURL);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            char[] buffer = new char[urlConnection.getContentLength()];
            isw.read(buffer);
            String result = new String(buffer);

            Log.v("My Response :: ", result);

            JSONArray jArray = new JSONArray(result);


            for (int i = 0; i < jArray.length(); i++) {
                Autocomplete stock = new Autocomplete();
                JSONObject jObj = jArray.getJSONObject(i);
                stock.setName(jObj.getString("Name"));
                stock.setExchange(jObj.getString("Exchange"));
                stock.setSymbol(jObj.getString("Symbol"));
                autocompleteList.add(stock);
            }
            //br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return autocompleteList;
    }
}


         /* String str1 = result;

                JSONObject jsonObject1 = new JSONObject(str1);

                Log.v("My Response :: ", result);
                result = result.replace("\\", "");
                result = result.replaceFirst("\"", "");
                int ind = result.lastIndexOf("\"");
                if( ind>=0 )
                    result = new StringBuilder(result).replace(ind, ind+1,"").toString();

                String  mr = "\"myArray\"";
                result = "{"+mr+":"+result+"}";
                Log.v("My Response :: ", result);
                JSONObject jsonObject = new JSONObject(result);*/

