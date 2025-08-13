package com.mason.cohereserverice2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText inputQuery;
    private TextView responseText;
    private ChatApi chatApi;
    private static final String BASE_URL = "https://cohere-serverr-tbzg.onrender.com"; // Replace with your Render URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputQuery = findViewById(R.id.input_query);
        responseText = findViewById(R.id.response_text);
        Button buttonAsk = findViewById(R.id.button_ask);
        Button buttonItinerary = findViewById(R.id.button_itinerary);

        // Initialize Retrofit
        chatApi = ApiClient.getClient(BASE_URL).create(ChatApi.class);

        // Handle "Ask Question" button
        buttonAsk.setOnClickListener(v -> {
            String query = inputQuery.getText().toString().trim();
            if (!query.isEmpty()) {
                sendGenerateRequest(query);
            } else {
                Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle "Get Itinerary" button
        buttonItinerary.setOnClickListener(v -> {
            String query = inputQuery.getText().toString().trim();
            if (!query.isEmpty()) {
                sendHolidayRequest(query);
            } else {
                Toast.makeText(this, "Please enter an itinerary request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendGenerateRequest(String prompt) {
        GenerateRequest request = new GenerateRequest(prompt);
        Call<GenerateResponse> call = chatApi.generate(request);
        call.enqueue(new Callback<GenerateResponse>() {
            @Override
            public void onResponse(Call<GenerateResponse> call, Response<GenerateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseText.setText(response.body().getText());
                } else {
                    responseText.setText("Error: Unable to get response");
                }
            }

            @Override
            public void onFailure(Call<GenerateResponse> call, Throwable t) {
                responseText.setText("Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendHolidayRequest(String userInput) {
        HolidayRequest request = new HolidayRequest(userInput);
        Call<HolidayResponse> call = chatApi.getHoliday(request);
        call.enqueue(new Callback<HolidayResponse>() {
            @Override
            public void onResponse(Call<HolidayResponse> call, Response<HolidayResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseText.setText(response.body().getItinerary());
                } else {
                    responseText.setText("Error: Unable to get itinerary");
                }
            }

            @Override
            public void onFailure(Call<HolidayResponse> call, Throwable t) {
                responseText.setText("Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}