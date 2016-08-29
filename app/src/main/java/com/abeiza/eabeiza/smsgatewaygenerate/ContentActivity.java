package com.abeiza.eabeiza.smsgatewaygenerate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by eabeiza on 8/23/2016.
 * */
public class ContentActivity extends AppCompatActivity {
    private DBAdapter db;
    private String kode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Product");


        Intent ActivityThatCalled = getIntent();
        String previousActivity = ActivityThatCalled.getExtras().getString("calling");
        TextView callingActivityMessage = (TextView) findViewById(R.id.nullTxt);
        callingActivityMessage.append("" + previousActivity);

        final EditText idPrd = (EditText) findViewById(R.id.idTxt);
        final EditText nmPrd = (EditText) findViewById(R.id.nameTxt);
        AutoCompleteTextView listProduct = (AutoCompleteTextView) findViewById(R.id.selectPrd);
        db = new DBAdapter(this);

        db.open();
        ArrayList array_list = db.getAllListProduct();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.evan_list_sample_1,array_list);
        listProduct.setAdapter(arrayAdapter);
        listProduct.setThreshold(1);
        db.close();

        /*listProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String id_parsing = parent.getItemAtPosition(position).toString();
                int angka = Integer.parseInt(id_parsing.substring(id_parsing.length() - 3));
                String namaPrd = id_parsing.substring(0,id_parsing.length() - 5);
                //Toast.makeText(parent.getContext(), "Position clicked: " + Integer.toString(angka), Toast.LENGTH_SHORT).show();
                idPrd.setText(Integer.toString(angka));
                nmPrd.setText(namaPrd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        listProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String id_parsing = parent.getItemAtPosition(position).toString();
                kode = "";
                kode = id_parsing.substring(id_parsing.length() - 4);
                int angka = Integer.parseInt(id_parsing.substring(id_parsing.length() - 3));
                String namaPrd = id_parsing.substring(0,id_parsing.length() - 11);
                //Toast.makeText(parent.getContext(), "Position clicked: " + Integer.toString(angka), Toast.LENGTH_SHORT).show();
                idPrd.setText(Integer.toString(angka));
                nmPrd.setText(namaPrd);
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    public void onSendName(View view) {
        try{
            EditText kd_prd = (EditText) findViewById(R.id.idTxt);
            EditText qty = (EditText) findViewById(R.id.qtyTxt);

            String usersName = String.valueOf("#"+kd_prd.getText()+" "+qty.getText());


            String id_prd = kode;
            db = new DBAdapter(this);
            db.open();
            db.updateCart(id_prd,"1");
            db.close();

            Intent goingBack = new Intent();
            goingBack.putExtra("UsersName", usersName);

            setResult(RESULT_OK, goingBack);

            finish();
        }catch(Throwable e){
            finish();
        }
    }

    public void goGetNamePrd(View view) {
        Intent getNameIntent = new Intent(this, ListPrdActivity.class);

        final int result = 1;
        getNameIntent.putExtra("callingActivity", "MainActivity");

        startActivityForResult(getNameIntent, result);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Before 2.0
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            super.onActivityResult(requestCode, resultCode, data);
            String nameData = "";
            EditText nmPrd = (EditText) findViewById(R.id.nameTxt);
            EditText IdPrd = (EditText) findViewById(R.id.idTxt);
            if (!data.equals(null)) {
                nameData = data.getStringExtra("UsersPrd");
            } else {
                nameData = "";
            }

            kode = "";
            kode = nameData.substring(nameData.length() - 4);
            int angka = Integer.parseInt(nameData.substring(nameData.length() - 3));
            String namaPrd = nameData.substring(0, nameData.length() - 11);

            nmPrd.setText(namaPrd);
            IdPrd.setText(Integer.toString(angka));
            //qtyPrd.setText(namaPrd);
            //Toast.makeText(this, "Position clicked: " + nameData, Toast.LENGTH_SHORT).show();
        }
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
                /*db = new DBAdapter(this);
                db.open();
                db.notupdateCart("0");
                db.close();
                Intent i1 = new Intent(ContentActivity.this, ResultActivity.class);
                startActivity(i1);*/
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
