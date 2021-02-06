package com.leon.receipt_receivables.infrastructure;

import com.leon.receipt_receivables.tables.LoginFeedBack;
import com.leon.receipt_receivables.tables.LoginInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAbfaService {

    @POST("kontoriNew/V1/Account/Login")
    Call<LoginFeedBack> login(@Body LoginInfo logininfo);
}

