package com.example.nto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity2 extends AppCompatActivity {

    private TextView fullnameTextView;
    private ImageView photoImageView;
    private TextView positionTextView;
    private TextView lastEntryTextView;
    private TextView errorTextView;
    private Button logoutButton;
    private Button refreshButton;
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullnameTextView = findViewById(R.id.fullname);
        photoImageView = findViewById(R.id.photo);
        positionTextView = findViewById(R.id.position);
        lastEntryTextView = findViewById(R.id.lastEntry);
        errorTextView = findViewById(R.id.error);
        logoutButton = findViewById(R.id.logout);
        refreshButton = findViewById(R.id.refresh);
        scanButton = findViewById(R.id.scan);

        loadUserData();

        logoutButton.setOnClickListener(v -> logout());
        refreshButton.setOnClickListener(v -> loadUserData());
        scanButton.setOnClickListener(v -> openScanner());
    }

    private void loadUserData() {
        String login = "user_login"; // логин
        new Thread(() -> {
            try {
                URL url = new URL("https://api.example.com/api/" + login + "/info");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    parseUserInfo(response.toString());
                } else {
                    runOnUiThread(() -> showError("Ошибка получения данных.")); // Ошибка
                }
            } catch (IOException e) {
                runOnUiThread(() -> showError("Ошибка сети."));
            }
        }).start();
    }

    private void parseUserInfo(String jsonResponse) {

        runOnUiThread(() -> {
            fullnameTextView.setText("");
            positionTextView.setText("");
            lastEntryTextView.setText(""); //
            photoImageView.setImageResource(R.drawable.);
            errorTextView.setVisibility(View.GONE);
        });
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
        hideUserInfo();
    }

    private void hideUserInfo() {
        fullnameTextView.setVisibility(View.GONE);
        photoImageView.setVisibility(View.GONE);
        positionTextView.setVisibility(View.GONE);
        lastEntryTextView.setVisibility(View.GONE);
        logoutButton.setVisibility(View.GONE);
        refreshButton.setVisibility(View.GONE);
        scanButton.setVisibility(View.GONE);
    }

    private void logout() {


        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openScanner() {
        Intent intent = new Intent(MainActivity2.this, ScanActivity.class);
        startActivity(intent);
    }
}