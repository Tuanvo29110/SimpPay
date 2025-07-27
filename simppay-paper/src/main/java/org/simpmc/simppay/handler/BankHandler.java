package org.simpmc.simppay.handler;

import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
