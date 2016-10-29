package com.example.vishal.andoridassignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import adapter.FavoriteStockAdapter;
import adapter.SuggestionAdapter;
import aynctask.FavoriteStockDetailsAsync;
import aynctask.StockDetails;
import aynctask.YahooImage;
import constants.ApplicationConstants;
import data.StockDetail;
import swipe.SwipeToDismissTouchListener;
import swipe.adapter.ListViewAdapter;
import ui.CustomAutocompleteTextView;

public class MainActivity extends AppCompatActivity {

    private CustomAutocompleteTextView mSearchbar;
    private Button clearButton;
    private Button getQuoteButton;
    private ProgressBar progressBar;
    private static ProgressBar refreshProgressBar;
    private String selectedSymbol;
    private int measuredWidth = 0;
    private int measuredHeight = 0;
    private static SharedPreferences sharedpreferences;
    private static ListView listViewFavoriteStock;
    private static FavoriteStockAdapter adapter;
    private static Context context;
    private int mInterval = 10000; // 10 seconds by default, can be changed later
    private Handler mHandler;
    private Switch switchAutoRefresh;
    private ImageButton autoRefreshButton;
    private static SwipeToDismissTouchListener<ListViewAdapter> touchListener;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.stock);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;

        refreshProgressBar = (ProgressBar) findViewById(R.id.refreshProgress);

        sharedpreferences = getSharedPreferences(ApplicationConstants.PREFERENCES, Context.MODE_PRIVATE);
        listViewFavoriteStock = (ListView) findViewById(R.id.listViewFavoriteStock);
        context = getBaseContext();
        populateFavoriteList();

        // ClickListener
        bindListViewClickListener();

        // Auto Refresh
        mHandler = new Handler();

        // Switch
        switchAutoRefresh = (Switch) findViewById(R.id.switchAutoRefresh);
        addAutoRefreshSwitchListener();

        //Add RefreshButtonListener
        autoRefreshButton = (ImageButton) findViewById(R.id.autoRefreshButton);
        addRefreshButtonListener();

        mSearchbar = (CustomAutocompleteTextView) findViewById(R.id.stockSearchTextView);
        clearButton = (Button) findViewById(R.id.clearButton);
        getQuoteButton = (Button) findViewById(R.id.getQuoteButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mSearchbar.setLoadingIndicator(progressBar);

        addClearButtonListener();
        addGetQuoteButtonListener();

        mSearchbar.setAdapter(new SuggestionAdapter(this, mSearchbar.getText().toString()));
        mSearchbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String str = (String) adapterView.getItemAtPosition(position);
                selectedSymbol = str.split(":")[0];
                mSearchbar.setText(selectedSymbol);
            }
        });
    }

    public void addRefreshButtonListener() {
        autoRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                populateFavoriteList();
            }
        });
    }

    public void addAutoRefreshSwitchListener() {
        switchAutoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startRepeatingTask();
                } else {
                    stopRepeatingTask();
                }
            }
        });
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                populateFavoriteList(); //this function can change value of mInterval.
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    public static void populateFavoriteList() {
        Log.d("******************", "Updating Favorite");

        final String favoriteStock = sharedpreferences.getString(ApplicationConstants.FAVORITE, "");
        if (favoriteStock.length() > 1) {
            try {
                refreshProgressBar.setVisibility(View.VISIBLE);
                FavoriteStockDetailsAsync favoriteStockDetailsAsync = new FavoriteStockDetailsAsync() {
                    public void receiveData(Object object) {
                        ArrayList<StockDetail> favoriteStockDetailList = (ArrayList<StockDetail>) object;
                        adapter = new FavoriteStockAdapter(context, favoriteStockDetailList);
                        listViewFavoriteStock.setAdapter(adapter);
                        refreshProgressBar.setVisibility(View.INVISIBLE);
                    }
                };
                favoriteStockDetailsAsync.execute(favoriteStock);
            } catch (Exception e) {
                e.printStackTrace();
                refreshProgressBar.setVisibility(View.VISIBLE);
            }
        }
        touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listViewFavoriteStock),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onPendingDismiss(final ListViewAdapter recyclerView, final int position) {
                                Log.d("Swipe", "The swipe is left ");
                                final StockDetail stock = adapter.getItem(position);
                                touchListener.setDismissDelay(1000000);
                                new AlertDialog.Builder(activity)
                                        .setMessage("Do you want to delete " + stock.getName() + " from favorites ?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                deleteFavorite(stock.getSymbol());
                                                touchListener.processPendingDismisses();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                touchListener.undoPendingDismiss();
                                            }
                                        }).show();
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                StockDetail stock = adapter.getItem(position);
                                adapter.remove(stock);
                            }
                        });

        listViewFavoriteStock.setOnTouchListener(touchListener);
        listViewFavoriteStock.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
    }

    public static void deleteFavorite(String symbol) {
        String symbols = sharedpreferences.getString(ApplicationConstants.FAVORITE, "");
        if (symbols != null) {
            try {
                JSONArray jArray = new JSONArray(symbols);
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray.getString(i).equals(symbol)) {
                        jArray.remove(i);
                        break;
                    }
                }
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(ApplicationConstants.FAVORITE, jArray.toString());
                editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void bindListViewClickListener() {
        // Bind onclick event handler
        listViewFavoriteStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockDetail stock = (StockDetail) parent.getItemAtPosition(position);
                showStockDetail(stock.getSymbol());
                // Put in your code here, what you wanted trigger :)
            }
        });

    }

    private void showStockDetail(String symbol) {
        try {
            StockDetail stockDetail = new StockDetails().execute(symbol).get();
            Bitmap bitmap = new YahooImage().execute(symbol).get();
            if (stockDetail.getStatus().equalsIgnoreCase("SUCCESS")) {
                Log.v("---Symbol----", stockDetail.toString());
                Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                ArrayList<Parcelable> parcelArray = new ArrayList<Parcelable>();
                parcelArray.add(stockDetail);
                i.putParcelableArrayListExtra("myParcelArray", parcelArray);
                Bundle bundle = new Bundle();
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                i.putExtra("byteArray", bs.toByteArray());
                startActivity(i);
            } else {
                AlertDialog alertDialog = getAlertDialog("No Stock Information available.");
                alertDialog.show();
            }
        } catch (Exception e) {
        }
    }

    private void addClearButtonListener() {
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSearchbar.setText("");
            }
        });
    }

    private AlertDialog getAlertDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return alertDialog;
    }

    private void addGetQuoteButtonListener() {
        getQuoteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String symbol = mSearchbar.getText().toString();
                if (symbol.trim().length() == 0) {
                    AlertDialog alertDialog = getAlertDialog("Please enter a Stock Name/Symbol");
                    alertDialog.show();
                } else if (selectedSymbol == null) {
                    AlertDialog alertDialog = getAlertDialog("Invalid Symbol");
                    alertDialog.show();
                } else if (!selectedSymbol.equalsIgnoreCase(symbol)) {
                    AlertDialog alertDialog = getAlertDialog("Invalid Symbol");
                    alertDialog.show();
                } else {
                    showStockDetail(symbol);
                }
            }
        });
    }
}

