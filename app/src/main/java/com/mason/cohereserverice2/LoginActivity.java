package com.mason.cohereserverice2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;
    private TextView goToRegister;
    private SecurePrefs securePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        goToRegister = findViewById(R.id.tvGoToRegister);

        securePrefs = new SecurePrefs(this);

        // If user not registered, go to register
        if (!securePrefs.isUserRegistered()) {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
            return;
        }

        // Set click listeners
        loginButton.setOnClickListener(v -> loginUser());
        goToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check credentials
        if (securePrefs.checkUser(email, password)) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Go to main activity directly
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
