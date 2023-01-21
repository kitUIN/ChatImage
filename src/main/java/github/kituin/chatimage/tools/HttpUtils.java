package github.kituin.chatimage.tools;

import okhttp3.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author kitUIN
 */
public class HttpUtils {
    public static int clock = 1;
    public static void getInputStream(String url) throws IOException {
        clock = 0;
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

                System.out.println(call.request().url());
                Path path = FileSystems.getDefault().getPath(call.request().url().toString().replace("http://","").replace("https://",""));

                ResponseBody body = response.body();
                InputStream inputStream = body.byteStream();
                try {
                    OutputStream os = new FileOutputStream("ChatImages/" + path.getFileName().toString());
                    os.write(inputStream.readAllBytes());
                    os.close();

                }catch (IOException e)
                {
                    e.printStackTrace();
                }
                clock = 1;
            }
        });
    }



}

