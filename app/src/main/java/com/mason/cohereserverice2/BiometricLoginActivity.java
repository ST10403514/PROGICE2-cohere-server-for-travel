package com.mason.cohereserverice2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class BiometricLoginActivity extends AppCompatActivity {

    private SecurePrefs securePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use correct layout file
        setContentView(R.layout.activity_biometric_login);

        securePrefs = new SecurePrefs(this);

        // If no user exists, go to registration
        if (!securePrefs.isUserRegistered()) {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
            return;
        }

        // Show biometric if enabled, else fallback to login
        if (securePrefs.isBiometricEnabled()) {
            showBiometricPrompt();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void showBiometricPrompt() {
        BiometricManager biometricManager = BiometricManager.from(this);

        // Check if biometric hardware and enrolled
        int canAuthenticate = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.DEVICE_CREDENTIAL
        );

        if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
            // Biometric unavailable, fallback to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(BiometricLoginActivity.this, "Biometric Auth Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BiometricLoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(BiometricLoginActivity.this, "Error: " + errString, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BiometricLoginActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(BiometricLoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Use Face or Fingerprint")
                .setNegativeButtonText("Use password instead")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
