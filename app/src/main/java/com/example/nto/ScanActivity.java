package com.example.nto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class QrScanActivity extends AppCompatActivity {

    private TextView qrResultTextView;
    private Button scanButton;
    private ActivityResultLauncher<Intent> qrScannerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        qrResultTextView = findViewById(R.id.qr_result);
        scanButton = findViewById(R.id.scan_button);

        qrScannerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String scannedData = data.getStringExtra("scanned_data");
                            if (scannedData != null) {
                                qrResultTextView.setText(scannedData);
                            } else {
                                qrResultTextView.setText("Не удалось получить данные.");
                            }
                        }
                    }
                }
        );

        scanButton.setOnClickListener(v -> {
            Intent scanIntent = new Intent(ScanActivity.this,Result.class);
            qrScannerLauncher.launch(scanIntent);
        });
    }
}