package com.example.nto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {

    private TextView resultTextView;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = findViewById(R.id.result);
        closeButton = findViewById(R.id.close);

        String scannedData = getIntent().getStringExtra("scanned_data");

        if (scannedData == null || scannedData.isEmpty() || scannedData.equals("Отмена")) {
            resultTextView.setText("Вход был отменён/Operation was cancelled");
        } else {
            sendToServer(scannedData);
        }

        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendToServer(String scannedData) {
        new Thread(() -> {
            String login = "";
            String apiUrl = "https://api.example.com/api/" + login + "/open";
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.getOutputStream().write(scannedData.getBytes());

                int responseCode = connection.getResponseCode();
                runOnUiThread(() -> {
                    if (responseCode == 200) {
                        resultTextView.setText("Успешно/Success");
                    } else {
                        resultTextView.setText("Что-то пошло не так/Something wrong");
                    }
                });

            } catch (IOException e) {
                runOnUiThread(() -> resultTextView.setText("Что-то пошло не так/Something wrong"));
            }
        }).start();
    }
}