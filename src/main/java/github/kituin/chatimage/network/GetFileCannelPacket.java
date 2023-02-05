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
import java.util.function.Supplier;

import static github.kituin.chatimage.Chatimage.*;
import static github.kituin.chatimage.network.FileCannelPacket.getDownloadFilePacket;
import static github.kituin.chatimage.network.FileCannelPacket.getFilePacket;
import static github.kituin.chatimage.network.GetFileCannel.GET_FILE_CANNEL;

public class GetFileCannelPacket {

    private String message;
    private static final Logger LOGGER = LogManager.getLogger();

    public GetFileCannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf();
    }

    public GetFileCannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.message);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {

        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            String url = this.message;
            ServerPlayer serverPlayer = ctx.getSender();
            if (SERVER_CACHE_MAP.containsKey(url) && FILE_COUNT_MAP.containsKey(url)) {
                HashMap<Integer, byte[]> list = SERVER_CACHE_MAP.get(url);
                Integer count = FILE_COUNT_MAP.get(url);
                if (count == list.size()) {
                    for (Map.Entry<Integer, byte[]> entry : list.entrySet()) {
                        DownloadFileCannel.sendToPlayer(getDownloadFilePacket(entry.getKey() + "->" + count + "->" + url, entry.getValue()),serverPlayer);
                        LOGGER.info("[send to client:" + entry.getKey() + "/" + (list.size() - 1) + "]" + url);
                    }
                    return;
                }
            } else {
                File file = new File(url);
                if (file.exists()) {
                    sendFilePackets(serverPlayer, url, file);
                    LOGGER.info("[send to client(from file)]" + url);
                    return;
                }
            }
            DownloadFileCannel.sendToPlayer(getDownloadFilePacket("0->0->" + url, new byte[1]), serverPlayer);
            List<String> names;
            if (USER_CACHE_MAP.containsKey(url)) {
                names = USER_CACHE_MAP.get(url);
            } else {
                names = Lists.newArrayList();
            }
            names.add(serverPlayer.getStringUUID());
            USER_CACHE_MAP.put(url, names);
            LOGGER.info("[not found in server]" + url);
        });
        return true;
    }
    private static void sendFilePackets(ServerPlayer player, String url, File file) {
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
                DownloadFileCannel.sendToPlayer(getDownloadFilePacket(i + "->" + counts + "->" + url, cipher),player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
