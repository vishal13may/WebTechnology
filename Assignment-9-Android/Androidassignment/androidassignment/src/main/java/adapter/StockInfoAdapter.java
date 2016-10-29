package adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vishal.andoridassignment.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Vishal on 01-May-16.
 */
public class StockInfoAdapter extends ArrayAdapter<String> {

    private Bitmap bitmap;

    public StockInfoAdapter(Context context, ArrayList<String> dataList) {
        super(context, 0, dataList);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stockinfo, parent, false);
        }
        // Lookup view for data population
        TextView textViewHeader = (TextView) convertView.findViewById(R.id.textViewHeader);
        TextView textViewValue = (TextView) convertView.findViewById(R.id.textViewValue);

        // Populate the data into the template view using the data object

        String[] str = data.split("#");

        textViewHeader.setText(str[0]);
        textViewValue.setText(str[1]);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView textViewStockActivity = (TextView)convertView.findViewById(R.id.textViewStockActivity);
        textViewStockActivity.setHeight(0);
        if (str.length > 2) {
            if (str[2].equalsIgnoreCase("UP")) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.up);
            } else {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.down);
            }
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }

        if (str[0].equalsIgnoreCase("YAHOO")) {
            ImageView yahooImageView = (ImageView) convertView.findViewById(R.id.imageViewYahoo);
            try {
                yahooImageView.setVisibility(View.VISIBLE);
                textViewStockActivity.setHeight(100);
                textViewStockActivity.setVisibility(View.VISIBLE);
                yahooImageView.setImageBitmap(getBitmap());
                textViewHeader.setHeight(0);
                textViewValue.setHeight(0);
                imageView.setVisibility(View.INVISIBLE);
                yahooImageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        showImageAlert();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // Return the completed view to render on screen
        return convertView;
    }

    public void showImageAlert() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setContentView(R.layout.yahooimageview);
        ImageView image = (ImageView) dialog.findViewById(R.id.yahooImageViewParent);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
        image.setImageBitmap(getBitmap());
        dialog.show();
        mAttacher.update();
    }
}

