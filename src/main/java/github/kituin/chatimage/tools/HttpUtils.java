package github.kituin.chatimage.tools;

import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import okhttp3.*;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

import static github.kituin.chatimage.client.ChatImageClient.CACHE_PATH;
import static github.kituin.chatimage.client.ChatImageClient.MOD_ID;


/**
 * @author kitUIN
 */
public class HttpUtils {
    public static HashMap<String, Identifier> CLOCK_MAP = new HashMap<String, Identifier>();
    public static HashMap<String, Integer> HTTPS_MAP = new HashMap<String, Integer>();
    public static void getInputStream(String url) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();

        Request getRequest = new Request.Builder()
                .url(url)
                .get()
                .build();
        if(HTTPS_MAP.containsKey(url)) {
            return;
        }else {
            HTTPS_MAP.put(url,1);
        }
        Call call = httpClient.newCall(getRequest);
        LogUtils.getLogger().info("[GET]" + url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HTTPS_MAP.remove(String.valueOf(call.request().url()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String url = String.valueOf(call.request().url());
                Path path = FileSystems.getDefault().getPath(call.request().url().toString().replace("http://","").replace("https://",""));

                ResponseBody body = response.body();
                InputStream inputStream = body.byteStream();
                File folder = new File(CACHE_PATH);
                String filePath = CACHE_PATH + "/" + path.getFileName().toString();
                if(!folder.exists()){ folder.mkdirs(); }
                try {
                    OutputStream os = new FileOutputStream(filePath);
                    os.write(inputStream.readAllBytes());
                    os.close();

                }catch (IOException e)
                {
                    e.printStackTrace();
                    HTTPS_MAP.remove(String.valueOf(call.request().url()));
                }

                if(!CLOCK_MAP.containsKey(url))
                {
                    File temp = new File(filePath);
                    NativeImage nativeImage = NativeImage.read(new FileInputStream(temp));
                    CLOCK_MAP.put(url,MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(MOD_ID + "/chatimage",
                            new NativeImageBackedTexture(nativeImage)));
                }
            }
        });
    }



}

