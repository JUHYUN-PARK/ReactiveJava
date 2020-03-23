package ch5.sample;

import okhttp3.*;
import utils.CommonUtils;
import utils.Log;

import java.io.IOException;

public class HttpGetExample {
    private final OkHttpClient client = new OkHttpClient();

    private static final String URL_README =
            "https://raw.githubusercontent.com/yudong80/reactivejava/master/README.md";

    public void run() {
        Request request = new Request.Builder()
                .url(URL_README)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(response.body().string());
            }
        });
    }
}
