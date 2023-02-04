package github.kituin.chatimage.tool;


import okhttp3.*;
import org.apache.logging.log4j.LogManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static github.kituin.chatimage.tool.ChatImageTool.bytesToHex;
import static github.kituin.chatimage.tool.ChatImageUrl.loadGif;


/**
 * @author kitUIN
 */
public class HttpUtils {
    public static HashMap<String, ChatImageFrame> CACHE_MAP = new HashMap<String, ChatImageFrame>();
    public static HashMap<String, Integer> HTTPS_MAP = new HashMap<String, Integer>();
    public static HashMap<String, Integer> NSFW_MAP = new HashMap<String, Integer>();

    private static String getPicType(byte[] is) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = is[i];
        }
        String type = bytesToHex(b).toUpperCase();
        if (type.contains("FFD8FF")) {
            return "jpg";
        } else if (type.contains("89504E47")) {
            return "png";
        } else if (type.contains("47494638")) {
            return "gif";
        } else if (type.contains("424D")) {
            return "bmp";
        } else {
            return "png";
        }
    }

    public static boolean getInputStream(String url) {

        OkHttpClient httpClient = new OkHttpClient();
        Request getRequest;
        try {
            getRequest = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
        } catch (java.lang.IllegalArgumentException ep) {
            LogManager.getLogger().info("can not request url: " + url);
            return false;
        }
        if (HTTPS_MAP.containsKey(url) && HTTPS_MAP.get(url) == 1) {
            return true;
        } else {
            HTTPS_MAP.put(url, 1);
        }
        Call call = httpClient.newCall(getRequest);
        LogManager.getLogger().info("[HTTP-GET]" + url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HTTPS_MAP.put(url, 2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String url = String.valueOf(call.request().url());
                ResponseBody body = response.body();
                if (body != null) {
                    InputStream inputStream = body.byteStream();
                    byte[] is = inputStream.readAllBytes();
                    String type = getPicType(is);
                    if ("gif".equals(type)) {
                        loadGif(new ByteArrayInputStream(is), url);
                    } else {
                        try {
                            CACHE_MAP.put(url, new ChatImageFrame(new ByteArrayInputStream(is)));
                        } catch (java.io.IOException e) {
                            CACHE_MAP.put(url, new ChatImageFrame(ChatImageFrame.FrameError.FILE_LOAD_ERROR));
                        }
                    }

                }
                HTTPS_MAP.put(url, 2);
            }
        });
        return true;

    }


}

