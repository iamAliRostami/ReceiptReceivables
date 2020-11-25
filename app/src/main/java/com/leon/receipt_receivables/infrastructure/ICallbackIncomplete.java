package com.leon.receipt_receivables.infrastructure;

import retrofit2.Response;

public interface ICallbackIncomplete<T> {
    void executeIncomplete(Response<T> response);
}
