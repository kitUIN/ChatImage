package github.kituin.chatimage.tool;

import com.madgag.gif.fmsware.GifDecoder;
import com.mojang.logging.LogUtils;
import github.kituin.chatimage.exception.InvalidChatImageUrlException;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static github.kituin.chatimage.ChatImage.*;
import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static github.kituin.chatimage.tool.HttpUtils.CACHE_MAP;

public class ChatImageUrl {
    private String originalUrl;
    private String httpUrl;

    private UrlMethod urlMethod;
    private String fileUrl;


    public ChatImageUrl(String url) throws InvalidChatImageUrlException {
        this.originalUrl = url;
        init();
    }

    private void init() throws InvalidChatImageUrlException {
        File folder = new File(CONFIG.cachePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (this.originalUrl.startsWith("http://") || this.originalUrl.startsWith("https://")) {
            this.urlMethod = UrlMethod.HTTP;
            this.httpUrl = this.originalUrl;
            if (!CACHE_MAP.containsKey(this.httpUrl)) {
                boolean f = HttpUtils.getInputStream(this.httpUrl);
                if (!f) {
                    throw new InvalidChatImageUrlException("Invalid HTTP URL",
                            InvalidChatImageUrlException.InvalidUrlMode.HttpNotFound);
                }
            }

        } else if (this.originalUrl.startsWith("file:///")) {
            this.urlMethod = UrlMethod.FILE;
            this.fileUrl = this.originalUrl
                    .replace("\\", "\\\\")
                    .replace("file:///", "");
            File file = new File(this.fileUrl);
            if (!CACHE_MAP.containsKey(this.fileUrl)) {
                if (file.exists()) {
                    try {
                        loadLocalFile(new FileInputStream(file), this.fileUrl);
                        if (MinecraftClient.getInstance().player != null) {
                            sendFilePackets(MinecraftClient.getInstance().player, this.fileUrl, file, FILE_CANNEL);
                        }
                    } catch (IOException e) {
                        CACHE_MAP.put(this.fileUrl,new ChatImageFrame(ChatImageFrame.FrameError.FILE_LOAD_ERROR));
                    }
                } else {
                    tryGetFromServer(this.fileUrl);
                }
            }
        } else {
            throw new InvalidChatImageUrlException(originalUrl + "<- this url is invalid, Please Recheck",
                    InvalidChatImageUrlException.InvalidUrlMode.NotFound);
        }
    }

    public static void tryGetFromServer(String url) {
        if (MinecraftClient.getInstance().player != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(url);
            sendFilePacketAsync(MinecraftClient.getInstance().player, GET_FILE_CANNEL, buf);
            LogUtils.getLogger().info("[try get from server]" + url);
        } else {
            CACHE_MAP.put(url, new ChatImageFrame(ChatImageFrame.FrameError.FILE_NOT_FOUND));
        }
    }

    public static void loadLocalFile(InputStream input, String url) throws IOException {
        if ("gif".equals(url.substring(url.length() - 3))) {
            loadGif(input, url);
        } else {
            ChatImageFrame frame = new ChatImageFrame(input);
            CACHE_MAP.put(url, frame);
        }
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public UrlMethod getUrlMethod() {
        return this.urlMethod;
    }

    public String getUrl() {
        switch (this.urlMethod) {

            case FILE:
                return this.fileUrl;
            case HTTP:
                return this.httpUrl;
            default:
                return this.originalUrl;
        }

    }

    @Override
    public String toString() {
        return this.originalUrl;
    }


    public static void loadGif(InputStream is, String url) {
        CompletableFuture.supplyAsync(() -> {
            try {
                GifDecoder gd = new GifDecoder();
                int status = gd.read(is);
                if (status != GifDecoder.STATUS_OK) {
                    return null;
                }
                ChatImageFrame frame = new ChatImageFrame(gd.getFrame(0));
                for (int i = 1; i < gd.getFrameCount(); i++) {
                    frame.append(new ChatImageFrame(gd.getFrame(i)));
                }
                CACHE_MAP.put(url, frame);

            } catch (IOException ignored) {
                CACHE_MAP.put(url, new ChatImageFrame(ChatImageFrame.FrameError.FILE_LOAD_ERROR));
            }
            return null;
        });
    }

    /**
     * Url的类型
     */
    public enum UrlMethod {
        /**
         * 本地文件 格式
         */
        FILE,
        /**
         * http(s)格式
         */
        HTTP,
    }

}
