package aynctask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import data.NewsFeedData;

/**
 * Created by Vishal on 01-May-16.
 */
public class NewsFeedAsync extends AsyncTask<String, Void, ArrayList<NewsFeedData>> {
    @Override
    protected ArrayList<NewsFeedData> doInBackground(String... params) {

        ArrayList<NewsFeedData> newsFeedDataList = new ArrayList<NewsFeedData>();

        Log.v("Parameter", params.toString());

        HttpURLConnection urlConnection = null;

        URL url;

        try {

            String newsFeeds = "http://stockinfo-1272.appspot.com/stockInfo.php?newsFeedsSymbol=" + params[0];

            Log.v("url", newsFeeds);

            url = new URL(newsFeeds);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            Reader reader = null;

            reader = new InputStreamReader(in, "UTF-8");

            int len = urlConnection.getContentLength();

            String result = new String(); // or use a StringBuilder if you prefer

            char[] buffer = new char[len];

            int count;

            while ((count = reader.read(buffer, 0, len)) > 0) {
                result += new String(buffer, 0, count);
            }

            Log.v("My Response :: ", result);

            JSONObject jsonObject = new JSONObject(result);

            JSONObject json = jsonObject.getJSONObject("d");

            JSONArray jsonArray = json.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                if(i==5)
                    break;
                NewsFeedData data = new NewsFeedData();
                JSONObject obj = jsonArray.getJSONObject(i);
                //JSONObject metaData = obj.getJSONObject("__metadata");
                data.setUrl(obj.getString("Url"));
                data.setTitle(obj.getString("Title"));
                data.setDate(obj.getString("Date"));
                data.setDescription(obj.getString("Description"));
                data.setPublisher(obj.getString("Source"));

                newsFeedDataList.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return newsFeedDataList;
    }
}

