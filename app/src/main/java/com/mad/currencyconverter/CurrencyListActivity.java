package com.mad.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
        CurrencyListAdapter adapter = new CurrencyListAdapter(database);

        ListView listView = (ListView) findViewById(R.id.currency_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String entry = (String) adapter.getItem(position);
                String capital = database.getCapital(entry);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("geo:0,0`?q=%s", capital)));
                startActivity(intent);
            }
        });
    }

}