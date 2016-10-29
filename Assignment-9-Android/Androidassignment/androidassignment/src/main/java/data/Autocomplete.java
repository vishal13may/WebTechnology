package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal on 30-Apr-16.
 */
public class Autocomplete {
    private String symbol;
    private String name;
    private String exchange;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
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
}
