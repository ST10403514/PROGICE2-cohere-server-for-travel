package com.mason.cohereserverice2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatApi {
    @POST("generate")
    Call<GenerateResponse> generate(@Body GenerateRequest request);

    @POST("holiday")
    Call<HolidayResponse> getHoliday(@Body HolidayRequest request);
}