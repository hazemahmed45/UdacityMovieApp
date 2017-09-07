package com.example.hazem.udacitymovieapp.Features.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.hazem.udacitymovieapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_settings);
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ())
        {
            case android.R.id.home:
            {
                onBackPressed ();
                break;
            }
        }
        return super.onOptionsItemSelected (item);
    }
}
