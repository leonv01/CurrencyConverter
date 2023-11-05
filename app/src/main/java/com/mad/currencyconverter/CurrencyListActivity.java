package com.mad.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.Arrays;

public class CurrencyListActivity extends AppCompatActivity {

    ExchangeRateDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);

        this.database = new ExchangeRateDatabase();

        String[] temp = database.getCurrencies();
        CurrencyListAdapter adapter = new CurrencyListAdapter(Arrays.asList(temp));

        ListView listView = (ListView) findViewById(R.id.currency_list_view);
        listView.setAdapter(adapter);
    }
}