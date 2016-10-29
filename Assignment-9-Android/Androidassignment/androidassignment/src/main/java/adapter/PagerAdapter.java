package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vishal.andoridassignment.Current;
import com.example.vishal.andoridassignment.Historical;
import com.example.vishal.andoridassignment.News;

import data.StockDetail;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    StockDetail stockDetailToBeDisplayed;

    public void setStockDetailToBeDisplayed(StockDetail stockDetailToBeDisplayed) {
        this.stockDetailToBeDisplayed = stockDetailToBeDisplayed;
    }

    public StockDetail getStockDetailToBeDisplayed() {
        return stockDetailToBeDisplayed;
    }

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Current tab1 = new Current();
                tab1.setStockDetailToBeDisplayed(stockDetailToBeDisplayed);
                return tab1;
            case 1:
                Historical tab2 = new Historical();
                tab2.setStockDetailToBeDisplayed(stockDetailToBeDisplayed);
                return tab2;
            case 2:
                News tab3 = new News();
                tab3.setStockDetailToBeDisplayed(stockDetailToBeDisplayed);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}