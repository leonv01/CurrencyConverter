package com.mad.currencyconverter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ExchangeRateUpdateWorker extends Worker {


    ExchangeRateDatabase database;
    public ExchangeRateUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters){
        super(context, workerParameters);

    }

    @NonNull
    @Override
    public Result doWork(){
        Log.d("Update Currencies","Update Currencies");
        database = ExchangeRateDatabase.getInstance();
        RefreshRate.updateCurrencies(database);
        Log.d("Update done", "Update done");

        return Result.success();
    }
}
