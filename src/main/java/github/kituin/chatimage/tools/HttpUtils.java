package github.kituin.chatimage.tools;

import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import okhttp3.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static github.kituin.chatimage.client.ChatImageClient.MOD_ID;
import static github.kituin.chatimage.tools.ChatImageTool.bytesToHex;


/**
 * @author kitUIN
 */
public class HttpUtils {
    public static HashMap<String, Identifier> CLOCK_MAP = new HashMap<String, Identifier>();
    public static HashMap<String, Integer> HTTPS_MAP = new HashMap<String, Integer>();

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
            return "unknown";
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
            LogUtils.getLogger().info("can not request url: " + url);
            return false;
        }
        if (HTTPS_MAP.containsKey(url) && HTTPS_MAP.get(url) == 1) {
            return true;
        } else {
            HTTPS_MAP.put(url, 1);
        }
        Call call = httpClient.newCall(getRequest);
        LogUtils.getLogger().info("[GET]" + url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HTTPS_MAP.put(url, 2);
                //HTTPS_MAP.remove(String.valueOf(call.request().url()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String url = String.valueOf(call.request().url());
                ResponseBody body = response.body();
                InputStream inputStream = body.byteStream();
                byte[] is = inputStream.readAllBytes();
                //String type = getPicType(is);
                if (!CLOCK_MAP.containsKey(url)) {
                    try {
                        NativeImage nativeImage = NativeImage.read(new ByteArrayInputStream(is));
                        CLOCK_MAP.put(url, MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(MOD_ID + "/chatimage",
                                new NativeImageBackedTexture(nativeImage)));
                    } catch (java.io.IOException e) {
                        HTTPS_MAP.put(url, 2);
                    }
                }
            }
        });
        return true;

    }


}

