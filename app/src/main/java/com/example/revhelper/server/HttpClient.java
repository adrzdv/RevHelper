package com.example.revhelper.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.revhelper.exceptions.CustomException;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.sys.AppRev;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    private static final String SERVER_URI = "http://revhelper.server:8080";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTimeAdapter.class, new LocalDateTimeAdapter()).create();


    public static void sendRequest(Context context, Map<String, CoachOnRevision> mapToSend) {

        OkHttpClient client = new OkHttpClient();
        String json = gson.toJson(mapToSend);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        String uri = SERVER_URI + "/data";
        Request newRequest = new Request.Builder().url(uri).post(body).build();

        client.newCall(newRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        AppRev.showToast(context, "ERROR") // Безопасный вызов в UI-потоке
                );
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                new Handler(Looper.getMainLooper()).post(() ->
                        AppRev.showToast(context, String.valueOf(response.body()))
                );
            }
        });
    }

}
