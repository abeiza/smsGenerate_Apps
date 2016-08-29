package com.abeiza.eabeiza.smsgatewaygenerate;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eabeiza on 7/23/2016.
 */
public class MainActivity extends AppCompatActivity {
    private final String url_prd = "http://112.78.137.253/sms/index.php/api/product";
    private final String url_otl = "http://112.78.137.253/sms/index.php/api/outlet/";
    private final String url_ba = "http://112.78.137.253/sms/index.php/api/ba/";
    private DBAdapter db;
    private TextView loading;
    private EditText id_ba;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("SMS Generate Apps");
        Boolean firsttime = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("firsttimerun", true);
        if(firsttime){
            setContentView(R.layout.activity_register);
            id_ba = (EditText) findViewById(R.id.id_ba_registrasi);
            Button btnSubmit = (Button) findViewById(R.id.btnSubmitRegistrasi);
            loading = (TextView) findViewById(R.id.load);

            db = new DBAdapter(this);

            btnSubmit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String parameter = id_ba.getText().toString();
                    SycBA sync_ba = new SycBA();
                    sync_ba.execute(parameter,"Second Parameter");
                }
            });
            Toast.makeText(MainActivity.this,"Welcome to Generate Message Apps,\n Please Register !",Toast.LENGTH_LONG).show();
        }else{
            setContentView(R.layout.activity_started);

            db = new DBAdapter(this);
            db.open();
            db.notupdateCart("0");
            Cursor c2 = db.getAccountBA();
            c2.moveToFirst();
            String nama_ba = c2.getString(1);


            Toast.makeText(MainActivity.this,"Welcome Mr/Mrs."+nama_ba,Toast.LENGTH_LONG).show();
            db.close();
            Button btnSt = (Button) findViewById(R.id.btnStart);

            btnSt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, OutletActivity.class);
                    startActivity(i);
                }
            });

            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("firsttimerun",false).commit();
        }
    }

    private class SycProduct extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            client.cache();

            Request request = new Request.Builder()
                    .url(url_prd)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //Masukan kode disini jika request gagal
                    Toast.makeText(getApplicationContext(), "HTTP Request Failure", Toast.LENGTH_LONG).show();

                    //kode untuk me-log error
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    //jika HTTP respone kode bukan 200
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    //Merubah response body menjadi string
                    final String responseData = "{ \"data1\" : " + response.body().string() + "}";
                    String data = "";
                    JSONObject Jobject = null;
                    try {
                        Jobject = new JSONObject(responseData);
                        JSONArray Jarray = Jobject.getJSONArray("data1");
                        db.open();
                        db.deleteProduct();
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            String id = object.optString("ID_PRODUCT").toString();
                            String nama = object.optString("NAMA_PRODUCT").toString();
                            String volume = object.optString("VOLUME").toString();
                            String desc = object.optString("DESCRIPTION_PRINCIPLE").toString();
                            //data = id;
                            data += "id_product : " + id + "\n nama_product : " + nama + "\n desc : " + desc + "\n\n";
                            db.insertProduct(id, nama,volume, desc);
                        }
                        db.close();

                        //final String data_akhir = data;

                        // Menampilkan string dalam textview
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //loading.setText("");
                                Toast.makeText(MainActivity.this, "Sync Data Product was Finished", Toast.LENGTH_LONG).show();
                                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("firsttimerun",false).commit();
                                Intent i = new Intent(MainActivity.this, StartActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();

        }
    }

    private class SycOutlet extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            String param = params[0];
            client.cache();

            Request request = new Request.Builder()
                    .url(url_otl+param)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //Masukan kode disini jika request gagal
                    Toast.makeText(getApplicationContext(), "HTTP Request Failure", Toast.LENGTH_LONG).show();

                    //kode untuk me-log error
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    //jika HTTP respone kode bukan 200
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    //Merubah response body menjadi string
                    final String responseData = "{ \"data2\" : " + response.body().string() + "}";
                    String data = "";
                    JSONObject Jobject = null;
                    try {
                        Jobject = new JSONObject(responseData);
                        JSONArray Jarray = Jobject.getJSONArray("data2");

                        db.open();
                        db.deleteOUTLET();
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            String id = object.optString("ID_OUTLET").toString();
                            String nama = object.optString("NAMA_OUTLET").toString();
                            String status = "not";
                            //data = id;
                            data += "id_OUTLET : " + id + "\n nama_OUTLET : " + nama + "\n\n";
                            db.insertOUTLET(id, nama, status);
                        }
                        db.close();

                        final String data_akhir = data;

                        // Menampilkan string dalam textview
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //loading.setText("");
                                SycProduct sync_prd = new SycProduct();
                                sync_prd.execute("Sending Parameter","Second Parameter");
                                Toast.makeText(MainActivity.this, "Sync Data Outlet was Finished", Toast.LENGTH_LONG).show();
                                //loading.setText(data_akhir);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
        }
    }

    private class SycBA extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            String param = params[0];
            client.cache();

            Request request = new Request.Builder()
                    .url(url_ba+param)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //Masukan kode disini jika request gagal
                    Toast.makeText(getApplicationContext(), "HTTP Request Failure", Toast.LENGTH_LONG).show();

                    //kode untuk me-log error
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    //jika HTTP respone kode bukan 200
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    //Merubah response body menjadi string
                    final String responseData = "{ \"data3\" : " + response.body().string() + "}";
                    String data = "";
                    final String notif_final;
                    String notif_start = "";
                    JSONObject Jobject = null;
                    try {
                        Jobject = new JSONObject(responseData);
                        JSONArray Jarray = Jobject.getJSONArray("data3");

                        db.open();
                        db.deleteBA();
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            String id_ba = object.optString("ID_BA").toString();
                            String nama_ba = object.optString("NAMA_BA").toString();
                            String id_tl = object.optString("ID_TL").toString();
                            String nama_tl = object.optString("NAMA_TL").toString();
                            String id_kba = object.optString("ID_KBA").toString();
                            String nama_kba = object.optString("NAMA_KBA").toString();
                            String status = object.optString("STATUS").toString();
                            if(id_ba.equals("-")){
                                notif_start =  "Sorry";
                            }else {
                                //data = id;
                                notif_start = "Success";
                                data += "id_ba : " + id_ba + "\n nama_ba : " + nama_ba + "\n\n";
                                db.insertBA(id_ba, nama_ba, id_tl, nama_tl, id_kba, nama_kba, status);
                            }
                        }
                        notif_final = notif_start;
                        db.close();

                        final String data_akhir = data;

                        // Menampilkan string dalam textview
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(notif_final.equals("Sorry")){
                                    Toast.makeText(MainActivity.this, "Identify Data is Failure\nBA ID is invalid. Please Try Again", Toast.LENGTH_LONG).show();
                                }else {
                                    String parameter = id_ba.getText().toString();
                                    SycOutlet sync_outlet = new SycOutlet();
                                    sync_outlet.execute(parameter, "Second Parameter");
                                    Toast.makeText(MainActivity.this, "Identify BA was Finished", Toast.LENGTH_LONG).show();
                                    //loading.setText(data_akhir)
                                }
                            }
                        });
                    } catch (JSONException e) {
                        //e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Sorry Your Data is Invalid", Toast.LENGTH_LONG).show();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
        }
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
                Intent i1 = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(i1);
                return true;
            case R.id.frag_setting:
                //your code
                Intent i2 = new Intent(MainActivity.this, SyncActivity.class);
                startActivity(i2);
                return true;
            case R.id.frag_about:
                //your code
                Intent i3 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
