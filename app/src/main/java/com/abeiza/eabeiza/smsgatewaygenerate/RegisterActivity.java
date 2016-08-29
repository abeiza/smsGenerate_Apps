package com.abeiza.eabeiza.smsgatewaygenerate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by eabeiza on 7/23/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText id_ba = (EditText) findViewById(R.id.id_ba_registrasi);
        Button btnSubmit = (Button) findViewById(R.id.btnSubmitRegistrasi);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
