package com.abeiza.eabeiza.smsgatewaygenerate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by eabeiza on 8/22/2016.
 */
public class OutletActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outet);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Outlet");

        ListView listOutlet = (ListView) findViewById(R.id.listOutlet);
        final DBAdapter db = new DBAdapter(this);

        db.open();
        ArrayList array_list = db.getAllNameOutlet();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,array_list);
        listOutlet.setAdapter(arrayAdapter);
        db.close();

        listOutlet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db.open();
                String id_parsing = parent.getItemAtPosition(position).toString();
                String kode = (id_parsing.substring(0,8));
                String status_yes = "active";
                String status_no = "not";
                //Toast.makeText(parent.getContext(), "Position clicked: " + kode, Toast.LENGTH_SHORT).show();
                db.updateOutlet(kode,status_yes);
                db.notupdateOutlet(kode,status_no);
                db.close();

                OutletActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(OutletActivity.this, ResultActivity.class);
                        startActivity(i);
                    }
                });
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
            case android.R.id.home:
                //your code
                // EX : call intent if you want to swich to other activity
                Intent i = new Intent(OutletActivity.this, StartActivity.class);
                startActivity(i);
                return true;
            case R.id.frag_account:
                //your code
                // EX : call intent if you want to swich to other activity
                Intent i1 = new Intent(OutletActivity.this, AccountActivity.class);
                startActivity(i1);
                return true;
            case R.id.frag_setting:
                //your code
                Intent i2 = new Intent(OutletActivity.this, SyncActivity.class);
                startActivity(i2);
                return true;
            case R.id.frag_about:
                //your code
                Intent i3 = new Intent(OutletActivity.this, AboutActivity.class);
                startActivity(i3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
