package github.kituin.chatimage.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import okhttp3.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public static void getInputStream(String url) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();

        Request getRequest = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = httpClient.newCall(getRequest);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String url = String.valueOf(call.request().url());
                Path path = FileSystems.getDefault().getPath(call.request().url().toString().replace("http://","").replace("https://",""));

                ResponseBody body = response.body();
                InputStream inputStream = body.byteStream();
                try {
                    OutputStream os = new FileOutputStream(CACHE_PATH + "/" + path.getFileName().toString());
                    os.write(inputStream.readAllBytes());
                    os.close();

                }catch (IOException e)
                {
                    e.printStackTrace();
                }

                if(!CLOCK_MAP.containsKey(url))
                {
                    NativeImage nativeImage = NativeImage.read(inputStream);
                    CLOCK_MAP.put(url,MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(MOD_ID + "/chatimage",
                            new NativeImageBackedTexture(nativeImage)));
                }
            }
        });
    }



}

