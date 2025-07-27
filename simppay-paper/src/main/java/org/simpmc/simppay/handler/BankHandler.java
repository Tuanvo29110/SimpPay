package org.simpmc.simppay.handler;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public abstract class BankHandler implements PaymentHandler {
    public CompletableFuture<String> get(String url) {
        return CompletableFuture.supplyAsync(() -> {
            OkHttpClient client = new OkHttpClient();

            // 1) Build the request
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            // 2) Enqueue the call (asynchronous)
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                return response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
