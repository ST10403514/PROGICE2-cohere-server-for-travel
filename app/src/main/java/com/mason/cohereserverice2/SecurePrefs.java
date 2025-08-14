package com.mason.cohereserverice2;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurePrefs {
    private static final String PREF_NAME = "secure_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_BIOMETRIC_ENABLED = "biometric_enabled";

    private final SharedPreferences prefs;

    public SecurePrefs(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(String email, String password) {
        prefs.edit()
                .putString(KEY_EMAIL, email)
                .putString(KEY_PASSWORD, password)
                .apply();
    }

    public boolean checkUser(String email, String password) {
        return email.equals(prefs.getString(KEY_EMAIL, "")) &&
                password.equals(prefs.getString(KEY_PASSWORD, ""));
    }

    public boolean isUserRegistered() {
        return prefs.contains(KEY_EMAIL) && prefs.contains(KEY_PASSWORD);
    }

    public void setBiometricEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, enabled).apply();
    }

    public boolean isBiometricEnabled() {
        return prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false);
    }
}
