package com.mad.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditCurrencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_currency);

        EditText editText = (EditText) findViewById(R.id.decimal_field_conversion);
        editText.setText(getIntent().getStringExtra("exchangeRate"));
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("newExchangeRate", v.getText().toString());
                    returnIntent.putExtra("listPosition", getIntent().getIntExtra("listPosition", -1));

                    setResult(RESULT_OK, returnIntent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}