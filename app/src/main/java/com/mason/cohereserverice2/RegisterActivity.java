package com.mason.cohereserverice2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Switch biometricSwitch;
    private Button registerButton;
    private SecurePrefs securePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        biometricSwitch = findViewById(R.id.switchBiometric);
        registerButton = findViewById(R.id.buttonRegister);

        securePrefs = new SecurePrefs(this);

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save credentials
        securePrefs.saveUser(email, password);
        securePrefs.setBiometricEnabled(biometricSwitch.isChecked());

        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

        // If biometric enabled, go directly to biometric login
        if (biometricSwitch.isChecked()) {
            startActivity(new Intent(this, BiometricLoginActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }
}
