package com.mad.currencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CurrencyListAdapter extends BaseAdapter {

    private List<String> data;
    private ExchangeRateDatabase database;

    public CurrencyListAdapter(List<String> data){
        this.data = data;
        this.database = new ExchangeRateDatabase();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();
        String currencyName = data.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.currency_list_view_item, null, false);
        }

        TextView currencyText = (TextView) view.findViewById(R.id.list_text_name);
        currencyText.setText(currencyName);

        TextView currency = (TextView) view.findViewById(R.id.list_text_currency);
        double temp = database.getExchangeRate(currencyName);
        currency.setText(Double.toString(temp));

        return view;
    }
}
