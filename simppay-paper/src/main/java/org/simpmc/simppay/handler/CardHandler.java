package org.simpmc.simppay.handler;

import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class CardHandler implements CardAdapter, PaymentHandler {
    public CompletableFuture<String> postFormData(List<Map<String, String>> formData, String url) {
        return CompletableFuture.supplyAsync(() -> {
            OkHttpClient client = new OkHttpClient();

            // 1) Build the multipart/form-data body
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            for (Map<String, String> field : formData) {
                for (Map.Entry<String, String> entry : field.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value != null) {
                        requestBodyBuilder.addFormDataPart(key, value);
                    }
                }
            }
            RequestBody requestBody = requestBodyBuilder.build();

            // 2) Build the request
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            // 3) Enqueue the call (asynchronous)
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
