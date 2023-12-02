package com.mad.currencyconverter;

import android.content.Context;
import android.database.CursorWindowAllocationException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class CurrencyItemAdapter extends BaseAdapter {

    ExchangeRateDatabase database;

    public CurrencyItemAdapter(ExchangeRateDatabase database){
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
            view = inflater.inflate(R.layout.currency_list_view_flag_item, null, false);
        }
        ImageView flagBox = view.findViewById(R.id.list_image);
        String imageIdString = String.format("flag_%s", currencyName.toLowerCase());
        int imageID = context.getResources().getIdentifier(imageIdString, "drawable", context.getPackageName());
        flagBox.setImageResource(imageID);

        TextView currencyText = (TextView) view.findViewById(R.id.list_text_name);
        currencyText.setText(currencyName);

        TextView currencyRate = (TextView) view.findViewById(R.id.list_rate);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(currencyName));
        String str = format.format(database.getExchangeRate(currencyName));
        currencyRate.setText(str);

        return view;
    }
}
