package com.rodriguezpacojr.winestore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetIpActivity extends AppCompatActivity {

    @BindView(R.id.edtipAddress)
    EditText edtipAddress;
    @BindView(R.id.edtportNumber)
    EditText edtportNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ip);

        ButterKnife.bind(this);
        edtipAddress.setText("192.168.0.8");
        edtportNumber.setText("8080");
    }

    @OnClick(R.id.btnok)
    public void onClick(View v) {
        if (v.getId() == R.id.btnok) {
            if (edtipAddress.getText().toString().equals("") || edtportNumber.getText().toString().equals(""))
                Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            else {
                String ip = edtipAddress.getText().toString();
                String port = edtportNumber.getText().toString();
                Setup setup = new Setup();
                setup.setIpAddress(ip);
                setup.setPortNumber(port);

                finish();
            }
        }
    }
}