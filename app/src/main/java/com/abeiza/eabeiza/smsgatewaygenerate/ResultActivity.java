package com.abeiza.eabeiza.smsgatewaygenerate;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by eabeiza on 8/23/2016.
 */
public class ResultActivity extends AppCompatActivity{
    private DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Result Message");

        EditText msgText = (EditText) findViewById(R.id.txtMsg);
        db = new DBAdapter(this);
        db.open();
            Cursor c = db.getDataOutlet();
            String kd_outlet = c.getString(0);
        db.close();
        db.open();
            Cursor c2 = db.getBA();
            c2.moveToFirst();
            String kd_ba = c2.getString(0);
        db.close();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
        String formattedDate = df.format(cal.getTime());
        String msg = formattedDate + " " + kd_ba + " " + kd_outlet;
        msgText.setText(msg);
    }

    public void goGetName(View view) {
        Intent getNameSceenIntent = new Intent(this, ContentActivity.class);

        final int result = 1;
        getNameSceenIntent.putExtra("callingActivity", "MainActivity");

        startActivityForResult(getNameSceenIntent, result);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            super.onActivityResult(requestCode, resultCode, data);

            TextView usersNameMessage = (TextView) findViewById(R.id.txtMsg);

            String nameSentBack = data.getStringExtra("UsersName");

            usersNameMessage.append("" + nameSentBack);
        }
    }

    private void sendSMSByVIntent() {
        EditText msgText = (EditText) findViewById(R.id.txtMsg);
        Intent smsVIntent = new Intent(Intent.ACTION_VIEW);
        smsVIntent.setType("vnd.android-dir/mms-sms");

        smsVIntent.putExtra("address","+628118679444");
        smsVIntent.putExtra("sms_body", msgText.getText().toString());
        try{
            startActivity(smsVIntent);
        }catch(Exception ex){
            Toast.makeText(ResultActivity.this, "Pengiriman SMS Gagal . . . ", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    private void sendSMSBySIntent() {
        EditText msgText = (EditText) findViewById(R.id.txtMsg);
        Uri uri = Uri.parse("smsto:" + "+628118679444");

        Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
        smsSIntent.putExtra("sms_body", msgText.getText().toString());
        try{
            startActivity(smsSIntent);
        }catch(Exception ex){
            Toast.makeText(ResultActivity.this, "Pengiriman SMS Gagal . . . ", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    public void sendSMSByManager(){
        try{
            EditText msgText = (EditText) findViewById(R.id.txtMsg);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+628118679444",
                    null,
                    msgText.getText().toString(),
                    null,
                    null);
            Toast.makeText(getApplicationContext(), "SMS Berhasil Dikirim", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "SMS Gagal Dikirim", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                //your code
                // EX : call intent if you want to swich to other activity
                db = new DBAdapter(this);
                db.open();
                db.notupdateCart("0");
                db.close();
                Intent i = new Intent(ResultActivity.this, OutletActivity.class);
                startActivity(i);
                return true;
            case R.id.frag_account:
                //your code
                // EX : call intent if you want to swich to other activity
                db = new DBAdapter(this);
                db.open();
                db.notupdateCart("0");
                db.close();
                Intent i1 = new Intent(ResultActivity.this, AccountActivity.class);
                startActivity(i1);
                return true;
            case R.id.frag_setting:
                //your code
                db = new DBAdapter(this);
                db.open();
                db.notupdateCart("0");
                db.close();
                Intent i2 = new Intent(ResultActivity.this, SyncActivity.class);
                startActivity(i2);
                return true;
            case R.id.frag_about:
                //your code
                db = new DBAdapter(this);
                db.open();
                db.notupdateCart("0");
                db.close();
                Intent i3 = new Intent(ResultActivity.this, AboutActivity.class);
                startActivity(i3);
                return true;
            case R.id.frag_send:
                //your code
                db = new DBAdapter(this);
                db.open();
                db.notupdateCart("0");
                db.close();

                sendSMSBySIntent();

                EditText msgText = (EditText) findViewById(R.id.txtMsg);
                db.open();
                Cursor c = db.getDataOutlet();
                String kd_outlet = c.getString(0);
                db.close();

                db.open();
                Cursor c2 = db.getBA();
                c2.moveToFirst();
                String kd_ba = c2.getString(0);
                db.close();

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
                String formattedDate = df.format(cal.getTime());
                String msg = formattedDate + " " + kd_ba + " " + kd_outlet;
                msgText.setText(msg);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
