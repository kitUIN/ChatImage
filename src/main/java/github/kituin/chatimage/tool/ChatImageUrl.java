package github.kituin.chatimage.tool;

import com.madgag.gif.fmsware.GifDecoder;
import github.kituin.chatimage.exception.InvalidChatImageUrlException;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static github.kituin.chatimage.ChatImage.FILE_CANNEL;
import static github.kituin.chatimage.ChatImage.GET_FILE_CANNEL;
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
                        throw new InvalidChatImageUrlException("file open error",
                                InvalidChatImageUrlException.InvalidUrlMode.FileNotFound);
                    }
                } else {
                    if (MinecraftClient.getInstance().player != null) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeString(this.fileUrl);
                        sendFilePacketAsync(MinecraftClient.getInstance().player, GET_FILE_CANNEL, buf);
                    }
                }
            }


        } else {
            throw new InvalidChatImageUrlException(originalUrl + "<- this url is invalid, Please Recheck",
                    InvalidChatImageUrlException.InvalidUrlMode.NotFound);
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

    public static void sendFilePacketAsync(PlayerEntity player, Identifier cannel, PacketByteBuf buf) {
        CompletableFuture.supplyAsync(() -> {
            if (player instanceof ClientPlayerEntity) {
                ClientPlayNetworking.send(cannel, buf);
            } else if (player instanceof ServerPlayerEntity) {
                ServerPlayNetworking.send((ServerPlayerEntity) player, cannel, buf);
            }
            return null;
        });

    }

    public static void sendFilePackets(PlayerEntity player, String url, File file, Identifier cannel) {
        try (InputStream input = new FileInputStream(file)) {
            byte[] byt = new byte[input.available()];
            int limit = 28000 - url.getBytes().length;
            int status = input.read(byt);
            ByteBuffer bb = ByteBuffer.wrap(byt);
            int count = byt.length / 20000;
            if (byt.length % 20000 == 0) {
                count -= 1;
            }
            for (int i = 0; i <= count; i++) {
                int bLength = 20000;
                if (i == count) {
                    bLength = byt.length - i * 20000;
                }
                byte[] cipher = new byte[bLength];
                System.out.println(limit);
                sendFilePacketAsync(player, cannel, getFilePacket(i + "->" + count + 1 + "->" + url, bb.get(cipher, 0, cipher.length).array()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static PacketByteBuf getFilePacket(String url, byte[] byt) {
        PacketByteBuf buf = PacketByteBufs.create();
        HashMap<String, byte[]> fileMap = new HashMap<>();
        fileMap.put(url, byt);
        buf.writeMap(fileMap, PacketByteBuf::writeString, PacketByteBuf::writeByteArray);
        return buf;
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
