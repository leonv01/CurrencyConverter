package com.mad.currencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class CurrencyListAdapter extends BaseAdapter {

    private final ExchangeRateDatabase database;

    public CurrencyListAdapter(ExchangeRateDatabase database){
        this.database = database;
    }

    @Override
    public int getCount() {
        return database.getCurrencies().length;
    }

    @Override
    public Object getItem(int position) {
        return database.getCurrencies()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();
        String currencyName = database.getCurrencies()[i];

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.currency_list_view_item, null, false);
        }

        TextView currencyText = (TextView) view.findViewById(R.id.list_text_name);
        currencyText.setText(currencyName);

        TextView currency = (TextView) view.findViewById(R.id.list_text_currency);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(currencyName));
        String str = format.format(database.getExchangeRate(currencyName));
        currency.setText(str);

        return view;
    }
}
