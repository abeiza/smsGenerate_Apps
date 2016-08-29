package com.abeiza.eabeiza.smsgatewaygenerate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by eabeiza on 8/24/2016.
 */
public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_started);

        Button btnSt = (Button) findViewById(R.id.btnStart);

        btnSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, OutletActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.frag_account:
                //your code
                // EX : call intent if you want to swich to other activity
                Intent i1 = new Intent(StartActivity.this, AccountActivity.class);
                startActivity(i1);
                return true;
            case R.id.frag_setting:
                //your code
                Intent i2 = new Intent(StartActivity.this, SyncActivity.class);
                startActivity(i2);
                return true;
            case R.id.frag_about:
                //your code
                Intent i3 = new Intent(StartActivity.this, AboutActivity.class);
                startActivity(i3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
