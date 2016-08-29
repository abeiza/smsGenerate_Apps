package com.abeiza.eabeiza.smsgatewaygenerate;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
 * Created by eabeiza on 8/25/2016.
 */
public class SyncActivity extends AppCompatActivity {
    private final String url_prd = "http://112.78.137.253/sms/index.php/api/product";
    private final String url_otl = "http://112.78.137.253/sms/index.php/api/outlet/";
    private final String url_ba = "http://112.78.137.253/sms/index.php/api/ba/";
    private DBAdapter db;
    private TextView loading;
    private String id_ba;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sync Data");

        db = new DBAdapter(this);

        db.open();
        Cursor c2 = db.getAccountBA();
        c2.moveToFirst();
        id_ba = c2.getString(0);
        db.close();

        Button sync = (Button) findViewById(R.id.synBtn);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parameter = id_ba;
                SycBA sync_ba = new SycBA();
                sync_ba.execute(parameter,"Second Parameter");
            }
        });
    }

    private class SycProduct extends AsyncTask<String, Void, String> {

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
                            db.insertProduct(id, nama, volume, desc);
                        }
                        db.close();

                        //final String data_akhir = data;

                        // Menampilkan string dalam textview
                        SyncActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //loading.setText("");
                                Toast.makeText(SyncActivity.this, "Sync Data Product was Finished", Toast.LENGTH_LONG).show();
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
                        SyncActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //loading.setText("");
                                SycProduct sync_prd = new SycProduct();
                                sync_prd.execute("Sending Parameter","Second Parameter");
                                Toast.makeText(SyncActivity.this, "Sync Data Outlet was Finished", Toast.LENGTH_LONG).show();
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

                            //data = id;
                            data += "id_ba : " + id_ba + "\n nama_ba : " + nama_ba + "\n\n";
                            db.insertBA(id_ba, nama_ba, id_tl, nama_tl, id_kba, nama_kba, status);
                        }
                        db.close();

                        final String data_akhir = data;

                        // Menampilkan string dalam textview
                        SyncActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //loading.setText("");
                                String parameter = id_ba;
                                SycOutlet sync_outlet = new SycOutlet();
                                sync_outlet.execute(parameter,"Second Parameter");
                                Toast.makeText(SyncActivity.this, "Identify BA was Finished", Toast.LENGTH_LONG).show();
                                //loading.setText(data_akhir)

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
                /*Intent i1 = new Intent(SyncActivity.this, StartActivity.class);
                startActivity(i1);*/
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
