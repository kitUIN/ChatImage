package github.kituin.chatimage;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


/**
 * @author kitUIN
 */
public class ChatImage implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static Identifier FILE_CANNEL = new Identifier("chatimage", "filecannel");
    public static Identifier GET_FILE_CANNEL = new Identifier("chatimage", "getfilecannel");
    public static Identifier DOWNLOAD_FILE_CANNEL = new Identifier("chatimage", "downloadfilecannel");
    public static HashMap<String, HashMap<Integer, byte[]>> SERVER_CACHE_MAP = new HashMap<>();
    public static HashMap<String, Integer> FILE_COUNT_MAP = new HashMap<>();

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(FILE_CANNEL, (server, player, handler, buf, responseSender) -> {
            for (Map.Entry<String, byte[]> entry : buf.readMap(PacketByteBuf::readString, PacketByteBuf::readByteArray).entrySet()) {
                String[] order = entry.getKey().split("->");
                HashMap<Integer, byte[]> list = new HashMap<>();
                if (SERVER_CACHE_MAP.containsKey(order[2])) {
                    list = SERVER_CACHE_MAP.get(order[2]);
                }
                list.put(Integer.valueOf(order[0]), entry.getValue());
                SERVER_CACHE_MAP.put(order[2], list);
                FILE_COUNT_MAP.put(order[2], Integer.valueOf(order[1]));
                LOGGER.info("[put to server:" + order[0] + "/" + (Integer.parseInt(order[1]) - 1) + "]" + order[2]);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(GET_FILE_CANNEL, (server, player, handler, buf, responseSender) -> {
            String url = buf.readString();
            if (SERVER_CACHE_MAP.containsKey(url) && FILE_COUNT_MAP.containsKey(url)) {
                HashMap<Integer, byte[]> list = SERVER_CACHE_MAP.get(url);
                Integer count = FILE_COUNT_MAP.get(url);
                if (count == list.size()) {
                    for (Map.Entry<Integer, byte[]> entry : list.entrySet()) {
                        sendFilePacketAsync(player, DOWNLOAD_FILE_CANNEL, getFilePacket(entry.getKey() + "->" + count + "->" + url, entry.getValue()));
                        LOGGER.info("[send to client:" + entry.getKey() + "/" + (list.size() - 1) + "]" + url);
                    }
                    return;
                }
            } else {
                File file = new File(url);
                if (file.exists()) {
                    sendFilePackets(player, url, file, GET_FILE_CANNEL);
                    LOGGER.info("[send to client]" + url);
                    return;
                }
            }
            sendFilePacketAsync(player, DOWNLOAD_FILE_CANNEL, getFilePacket("0->0->" + url, new byte[1]));
            LOGGER.info("[not found in server]" + url);
        });
    }

    public static PacketByteBuf getFilePacket(String url, byte[] byt) {
        PacketByteBuf buf = PacketByteBufs.create();
        HashMap<String, byte[]> fileMap = new HashMap<>();
        fileMap.put(url, byt);
        buf.writeMap(fileMap, PacketByteBuf::writeString, PacketByteBuf::writeByteArray);
        return buf;
    }

    public static PacketByteBuf getStringPacket(String str) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(str);
        return buf;
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
            int limit = 29000 - url.getBytes().length;
            int status = input.read(byt);
            ByteBuffer bb = ByteBuffer.wrap(byt);
            int count = byt.length / limit;
            int counts = 0;
            if (byt.length % limit == 0) {
                counts = count;
            } else {
                counts = count + 1;
            }
            for (int i = 0; i <= count; i++) {
                int bLength = limit;
                if (i == count) {
                    bLength = byt.length - i * limit;
                    if (bLength == 0) {
                        return;
                    }
                }
                byte[] cipher = new byte[bLength];
                bb.get(cipher, 0, cipher.length).array();
                sendFilePacketAsync(player, cannel, getFilePacket(i + "->" + counts + "->" + url, cipher));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
