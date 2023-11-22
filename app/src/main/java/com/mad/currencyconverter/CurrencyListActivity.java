package com.mad.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
public class CurrencyListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 123;

    ExchangeRateDatabase database;
    boolean editModeEnabled;

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
                String currency = (String) adapter.getItem(position);
                String conversionRate = Double.toString(database.getExchangeRate(currency));
                if(editModeEnabled){
                    Intent detailsIntent = new Intent(CurrencyListActivity.this, EditCurrencyActivity.class);

                    detailsIntent.putExtra("currency", currency);
                    detailsIntent.putExtra("conversionRate", conversionRate);
                    Log.d(conversionRate, conversionRate);
                    database.setExchangeRate(currency, Double.parseDouble(conversionRate));

                    return;
                }

                String capital = database.getCapital(currency);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("geo:0,0`?q=%s", capital)));
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_mode_toolbar);
        setSupportActionBar(toolbar);

        editModeEnabled = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.currency_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.currency_list_edit_mode) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    editModeEnabled = !editModeEnabled;
                    return false;
                }
            });
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}