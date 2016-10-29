package data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Vishal on 30-Apr-16.
 */
public class StockDetail implements Parcelable {
    private String name;
    private String symbol;
    private String lastPrice;
    private String change;
    private String changePercent;
    private String timeStamp;
    private String mSDate;
    private String marketCap;
    private String volume;
    private String changeYTD;
    private String changePercentYTD;
    private String high;
    private String low;
    private String open;
    private String status;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getmSDate() {
        return mSDate;
    }

    public void setmSDate(String mSDate) {
        this.mSDate = mSDate;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getChangeYTD() {
        return changeYTD;
    }

    public void setChangeYTD(String changeYTD) {
        this.changeYTD = changeYTD;
    }

    public String getChangePercentYTD() {
        return changePercentYTD;
    }

    public void setChangePercentYTD(String changePercentYTD) {
        this.changePercentYTD = changePercentYTD;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StockDetail{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", lastPrice=" + lastPrice +
                ", change=" + change +
                ", changePercent=" + changePercent +
                ", timeStamp='" + timeStamp + '\'' +
                ", mSDate='" + mSDate + '\'' +
                ", marketCap=" + marketCap +
                ", volume=" + volume +
                ", changeYTD=" + changeYTD +
                ", changePercentYTD=" + changePercentYTD +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(symbol);
        out.writeString(lastPrice);
        out.writeString(change);
        out.writeString(changePercent);
        out.writeString(timeStamp);
        out.writeString(mSDate);
        out.writeString(marketCap);
        out.writeString(volume);
        out.writeString(changeYTD);
        out.writeString(changePercentYTD);
        out.writeString(high);
        out.writeString(low);
        out.writeString(open);
        out.writeString(status);
    }

    public static final Parcelable.Creator<StockDetail> CREATOR
            = new Parcelable.Creator<StockDetail>() {
        public StockDetail createFromParcel(Parcel in) {

            return new StockDetail(in);
        }

        public StockDetail[] newArray(int size) {
            return new StockDetail[size];
        }


    };

    private StockDetail(Parcel in) {
        name = in.readString();
        symbol = in.readString();
        lastPrice = in.readString();
        change = in.readString();
        changePercent = in.readString();
        timeStamp = in.readString();
        mSDate = in.readString();
        marketCap = in.readString();
        volume = in.readString();
        changeYTD = in.readString();
        changePercentYTD = in.readString();
        high = in.readString();
        low = in.readString();
        open = in.readString();
        status = in.readString();
    }

    public StockDetail() {
    }

}
