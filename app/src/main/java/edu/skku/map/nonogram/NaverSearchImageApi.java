package edu.skku.map.nonogram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NaverSearchImageApi {
    private static final String NAVER_CLIENT_ID = "oz04i9HuvH5kU1cLeDtc";
    private static final String NAVER_CLIENT_SECRET = "BIm2H9CtyM";
    private static final String NAVER_IMAGE_SEARCH_API_URL = "https://openapi.naver.com/v1/search/image";

    public static void request(String searchText, final NaverSearchImageApiCallback naverSearchImageApiCallback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(NAVER_IMAGE_SEARCH_API_URL).newBuilder();
        urlBuilder.addQueryParameter("query", searchText);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Naver-Client-Id", NAVER_CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", NAVER_CLIENT_SECRET)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseBodyString = response.body().string();

                Gson gson = new GsonBuilder().create();
                final NaverImageResponse naverImageResponse = gson.fromJson(responseBodyString, NaverImageResponse.class);

                naverSearchImageApiCallback.onSuccess(naverImageResponse);
            }
        });
    }
}
