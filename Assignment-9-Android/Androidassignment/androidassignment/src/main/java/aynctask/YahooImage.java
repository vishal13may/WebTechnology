package aynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vishal on 01-May-16.
 */
public class YahooImage extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... params) {

        Log.v("Parameter", params.toString());

        HttpURLConnection urlConnection = null;

        URL url;

        InputStream in = null;
        Bitmap bmp = null;

        try {

            String yahooURL = "http://chart.finance.yahoo.com/t?s=" + params[0] + "&lang=en-US&width=600&height=500";
            url = new URL(yahooURL);
            urlConnection = (HttpURLConnection) url
                    .openConnection();
            in = urlConnection.getInputStream();
            bmp = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return bmp;
    }
}
