package github.kituin.chatimage.tool;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;

import static github.kituin.chatimage.ChatImage.LOGGER;
import static github.kituin.chatimage.tool.ChatImageHandler.loadFile;

/**
 * @author kitUIN
 */
public class ChatImageHttpHandler {
    public static HashMap<String, Integer> HTTPS_MAP = new HashMap<>();

    public static boolean getInputStream(String url) {
        OkHttpClient httpClient = new OkHttpClient();
        Request getRequest;
        try {
            getRequest = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
        } catch (IllegalArgumentException ep) {
            LOGGER.error("[ChatImageHttpHandler]can not request url: " + url);
            return false;
        }
        if (HTTPS_MAP.containsKey(url) && HTTPS_MAP.get(url) == 1) {
            return true;
        } else {
            HTTPS_MAP.put(url, 1);
        }
        Call call = httpClient.newCall(getRequest);
        LOGGER.info("[ChatImageHttpHandler-GET]" + url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HTTPS_MAP.put(url, 2);
            }

            @Override
            public void onResponse(Call call, Response response) {
                String url = String.valueOf(call.request().url());
                ResponseBody body = response.body();
                if (body != null) {
                    loadFile(body.byteStream(), url);
                }
                HTTPS_MAP.put(url, 2);
            }
        });
        return true;

    }


}

