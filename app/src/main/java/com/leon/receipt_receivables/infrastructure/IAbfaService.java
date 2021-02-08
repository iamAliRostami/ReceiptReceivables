package com.leon.receipt_receivables.infrastructure;

import com.leon.receipt_receivables.tables.CustomPlace;
import com.leon.receipt_receivables.tables.LoginFeedBack;
import com.leon.receipt_receivables.tables.LoginInfo;
import com.leon.receipt_receivables.tables.ReceiptReceivablesFeedback;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAbfaService {

    @POST("kontoriNew/V1/Account/Login")
    Call<LoginFeedBack> login(@Body LoginInfo logininfo);

    @GET("moshtarakinApi/vosoolApp/test")
    Call<ReceiptReceivablesFeedback> download();

    @POST("/MoshtarakinApi/Gis/V1/GetXy/jesuschrist/{billId}")
    Call<CustomPlace> getXY(
            @Path("billId") String billId
    );
}

