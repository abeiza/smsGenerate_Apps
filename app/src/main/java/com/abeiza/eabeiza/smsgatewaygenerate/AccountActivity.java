package com.abeiza.eabeiza.smsgatewaygenerate;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by eabeiza on 8/25/2016.
 */
public class AccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Account");

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c2 = db.getAccountBA();
        c2.moveToFirst();
        String id_ba = c2.getString(0);
        String nama_ba = c2.getString(1);
        String nama_tl = c2.getString(2);
        String nama_kba = c2.getString(3);
        String status = c2.getString(4);
        String ba_status;
        if(status.equals("1")) {
            ba_status = "Active";
        }else{
            ba_status = "Not Active";
        }

        TextView nama = (TextView) findViewById(R.id.accountName);
        nama.setText(nama_ba+" ("+ba_status+")");

        TextView id = (TextView) findViewById(R.id.accountCode);
        id.setText(id_ba);

        TextView tl = (TextView) findViewById(R.id.accountTL);
        tl.setText(nama_tl);

        TextView kba = (TextView) findViewById(R.id.accountKBA);
        kba.setText(nama_kba);

        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                //your code
                // EX : call intent if you want to swich to other activity
                /*Intent i1 = new Intent(AccountActivity.this, StartActivity.class);
                startActivity(i1);*/
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
