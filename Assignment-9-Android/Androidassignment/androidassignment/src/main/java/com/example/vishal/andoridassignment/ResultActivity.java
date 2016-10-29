package com.example.vishal.andoridassignment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;

import java.util.ArrayList;

import adapter.PagerAdapter;
import constants.ApplicationConstants;
import data.StockDetail;


public class ResultActivity extends AppCompatActivity {

    private ImageButton facebookShare;
    private ImageButton bookMarkImage;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private StockDetail stockDetail;
    private SharedPreferences pref;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Fetch BookMark Button :
        bookMarkImage = (ImageButton) findViewById(R.id.bookMarkButton);
        addBookMarkListener();

        // Add Facebook Button
        facebookShare = (ImageButton) findViewById(R.id.facebookShareButton);
        addFacebookShareButtonListener();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getApplicationContext(),
                        "You shared this post", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),
                        "You cancelled this post", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(),
                        "There is some error. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

      //  EditText editTextStockName = (EditText) findViewById(R.id.editTextStockName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tabLayout.setTabTextColors(getColorStateList(R.color.black));
            } else {
                tabLayout.setTabTextColors(ColorStateList.valueOf(0));
            }
        }
        tabLayout.addTab(tabLayout.newTab().setText("CURRENT"));
        tabLayout.addTab(tabLayout.newTab().setText("HISTORICAL"));
        tabLayout.addTab(tabLayout.newTab().setText("NEWS"));

        tabLayout.setTabTextColors(Color.parseColor("#606060"), Color.parseColor("#000000"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        // myParcelArray
        ArrayList<Parcelable> dataList = getIntent().getExtras().getParcelableArrayList("myParcelArray");
        Bitmap b = null;
        if (getIntent().hasExtra("byteArray")) {
            b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
        }

        viewPager.setAdapter(adapter);
        adapter.setStockDetailToBeDisplayed((StockDetail) dataList.get(0));
        adapter.getStockDetailToBeDisplayed().setBitmap(b);

        stockDetail = adapter.getStockDetailToBeDisplayed();

        checkBookmark();

       // editTextStockName.setText(adapter.getStockDetailToBeDisplayed().getName());
        getSupportActionBar().setTitle(adapter.getStockDetailToBeDisplayed().getName());

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void checkBookmark() {
        pref = getSharedPreferences(ApplicationConstants.PREFERENCES, Context.MODE_PRIVATE);
        String favoriteStock = pref.getString(ApplicationConstants.FAVORITE, "");
        if (favoriteStock.length() > 1) {
            try {
                JSONArray jArray = new JSONArray(favoriteStock);
                int i = 0;
                for (i = 0; i < jArray.length(); i++) {
                    if (jArray.getString(i).equals(stockDetail.getSymbol())) {
                        bookMarkImage.setImageResource(R.drawable.yellow);
                        bookMarkImage.setTag("REMOVEBOOKMARK".toString());
                        break;
                    }
                }
                if (i == jArray.length()) {
                    bookMarkImage.setImageResource(R.drawable.white);
                    bookMarkImage.setTag("ADDBOOKMARK".toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            bookMarkImage.setImageResource(R.drawable.white);
            bookMarkImage.setTag("ADDBOOKMARK".toString());
        }
    }

    private void addBookMarkListener() {
        bookMarkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String backgroundImageName = bookMarkImage.getTag().toString();
                pref = getSharedPreferences(ApplicationConstants.PREFERENCES, Context.MODE_PRIVATE);
                String favoriteStock = pref.getString(ApplicationConstants.FAVORITE, "");
                Log.d("**FavoriteStock******", favoriteStock);
                if (backgroundImageName.equalsIgnoreCase("ADDBOOKMARK")) {
                    if (favoriteStock.length() > 1) {
                        try {
                            JSONArray jArray = new JSONArray(favoriteStock);
                            jArray.put(stockDetail.getSymbol());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(ApplicationConstants.FAVORITE, jArray.toString());
                            editor.commit();
                            Toast.makeText(getApplicationContext(),
                                    "You added "+ stockDetail.getName() +"("+stockDetail.getSymbol()+") to favorite list.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            JSONArray jArray = new JSONArray();
                            jArray.put(stockDetail.getSymbol());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(ApplicationConstants.FAVORITE, jArray.toString());
                            editor.commit();
                            Toast.makeText(getApplicationContext(),
                                    "You added "+ stockDetail.getName() +"("+stockDetail.getSymbol()+") to favorite list.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    bookMarkImage.setImageResource(R.drawable.yellow);
                    bookMarkImage.setTag("REMOVEBOOKMARK".toString());
                } else {
                    bookMarkImage.setImageResource(R.drawable.white);
                    bookMarkImage.setTag("ADDBOOKMARK".toString());
                    if (favoriteStock != null) {
                        try {
                            JSONArray jArray = new JSONArray(favoriteStock);
                            for (int i = 0; i < jArray.length(); i++) {
                                if (jArray.getString(i).equals(stockDetail.getSymbol())) {
                                    jArray.remove(i);
                                    break;
                                }
                            }
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(ApplicationConstants.FAVORITE, jArray.toString());
                            editor.commit();
                            Toast.makeText(getApplicationContext(),
                                    "You removed "+ stockDetail.getName() +"("+stockDetail.getSymbol()+") from favorite list.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                MainActivity.populateFavoriteList();
            }
        });
    }

    private void addFacebookShareButtonListener() {
        facebookShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    String title = "Current Stock Price of " + stockDetail.getName() + " is $" + stockDetail.getLastPrice();
                    String description = "Stock information of " + stockDetail.getName() + " (" + stockDetail.getSymbol() + ")";
                    String imageURL = "http://chart.finance.yahoo.com/t?s=" + stockDetail.getSymbol() + "&lang=en-US&width=400&height=300";
                    Uri imgUri = Uri.parse(imageURL);
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(title)
                            .setContentDescription(description)
                            .setImageUrl(imgUri)
                            .setContentUrl(Uri.parse("http://chart.finance.yahoo.com/t?s=" + stockDetail.getSymbol() + "&lang=en-US&width=400&height=300"))
                            .build();

                    shareDialog.show(linkContent);
                }
            }
        });
    }
}
