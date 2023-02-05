package github.kituin.chatimage.network;

import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static github.kituin.chatimage.Chatimage.*;

public class FileCannelPacket {


    private Map<String, byte[]> message;
    private static final Logger LOGGER = LogManager.getLogger();

    public FileCannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readByteArray);
    }

    public FileCannelPacket(Map<String, byte[]> message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(this.message, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeByteArray);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            for (Map.Entry<String, byte[]> entry : this.message.entrySet()) {
                String[] order = entry.getKey().split("->");
                HashMap<Integer, byte[]> list = new HashMap<>();
                String url = order[2];
                if (SERVER_CACHE_MAP.containsKey(url)) {
                    list = SERVER_CACHE_MAP.get(url);
                }
                list.put(Integer.valueOf(order[0]), entry.getValue());
                SERVER_CACHE_MAP.put(url, list);
                FILE_COUNT_MAP.put(url, Integer.valueOf(order[1]));
                LOGGER.info("[put to server:" + order[0] + "/" + (Integer.parseInt(order[1]) - 1) + "]" + url);
                if (Integer.parseInt(order[1]) == list.size() && USER_CACHE_MAP.containsKey(url)) {
                    List<String> names = USER_CACHE_MAP.get(url);
                    for (String uuid : names) {
                        ServerPlayer serverPlayer = ctx.getSender().server.getPlayerList().getPlayer(UUID.fromString(uuid));
                        for (Map.Entry<Integer, byte[]> en : list.entrySet()) {
                            DownloadFileCannel.sendToPlayer(getDownloadFilePacket(en.getKey() + "->" + order[1] + "->" + url, en.getValue()), serverPlayer);
                            LOGGER.info("[echo to client:" + en.getKey() + "/" + (list.size() - 1) + "]" + url);
                        }
                    }
                    USER_CACHE_MAP.put(url, Lists.newArrayList());
                }
            }
        });
        ctx.setPacketHandled(true);
    }

    public static DownloadFileCannelPacket getDownloadFilePacket(String url, byte[] byt) {
        HashMap<String, byte[]> fileMap = new HashMap<>();
        fileMap.put(url, byt);
        return new DownloadFileCannelPacket(fileMap);
    }
    public static FileCannelPacket getFilePacket(String url, byte[] byt) {
        HashMap<String, byte[]> fileMap = new HashMap<>();
        fileMap.put(url, byt);
        return new FileCannelPacket(fileMap);
    }
    public static void sendFilePackets(String url, File file) {
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
                FileCannel.sendToServer(getFilePacket(i + "->" + counts + "->" + url, cipher));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
