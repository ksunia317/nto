import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button loginButton;
    private TextView errorTextView;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_LOGIN_KEY = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username);
        loginButton = findViewById(R.id.login);
        errorTextView = findViewById(R.id.error);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedLogin = sharedPreferences.getString(PREF_LOGIN_KEY, null);

        // Если пользователь уже авторизован
        if (savedLogin != null) {
            finish();
            return;
        }

        usernameEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateUsername(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        loginButton.setOnClickListener(v -> authenticateUser(usernameEditText.getText().toString()));
    }

    private void validateUsername(String username) {
        boolean isValid = username.length() >= 3 &&
                !Character.isDigit(username.charAt(0)) &&
                username.matches("[a-zA-Z0-9]+");
        loginButton.setEnabled(isValid);
        errorTextView.setVisibility(View.GONE); // Скрытие ошибки при вводе
    }

    private void authenticateUser(String username) {
        String urlString = "https://api.example.com/" + username + "/auth"; // URL  API

        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();

                runOnUiThread(() -> {
                    if (responseCode == 200) { // Авторизация
                        sharedPreferences.edit().putString(PREF_LOGIN_KEY, username).apply();
                        finish();
                    } else { // Ошибка авторизации
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                });

            } catch (IOException e) {
                runOnUiThread(() -> errorTextView.setVisibility(View.VISIBLE)); // Ошибка сети
            }
        }).start();
    }
}