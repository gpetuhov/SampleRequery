package com.gpetuhov.android.samplerequery;

import android.app.Application;

import com.gpetuhov.android.samplerequery.requery.Models;

import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import timber.log.Timber;

public class SampleRequeryApp extends Application {

    private ReactiveEntityStore<Persistable> dataStore;

    // Return data source
    // (create new data source if doesn't exist)
    ReactiveEntityStore<Persistable> getData() {
        if (dataStore == null) {
            // Override onUpgrade to handle migrating to a new version
            DatabaseSource source = new DatabaseSource(this, Models.DEFAULT, 1);
            if (BuildConfig.DEBUG) {
                // Use this in development mode to drop and recreate the tables on every upgrade
                source.setTableCreationMode(TableCreationMode.DROP_CREATE);
            }
            Configuration configuration = source.getConfiguration();
            dataStore = ReactiveSupport.toReactiveStore(
                    new EntityDataStore<Persistable>(configuration));
        }
        return dataStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Timber
        if (BuildConfig.DEBUG) {
            // Here we use Timber DebugTree for logging in debug version
            Timber.plant(new Timber.DebugTree());
        } else {
            // Here we may use some crash library to report crashes in release version
        }
    }
}