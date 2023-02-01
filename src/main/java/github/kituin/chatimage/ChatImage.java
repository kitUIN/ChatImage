package github.kituin.chatimage;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static github.kituin.chatimage.tool.ChatImageUrl.*;


/**
 * @author kitUIN
 */

public class ChatImage implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static Identifier FILE_CANNEL = new Identifier("chatimage", "filecannel");
    public static Identifier GET_FILE_CANNEL = new Identifier("chatimage", "getfilecannel");
    public static HashMap<String, HashMap<Integer, byte[]>> SERVER_CACHE_MAP = new HashMap<>();
    public static HashMap<String, Integer> FILE_COUNT_MAP = new HashMap<>();

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(FILE_CANNEL, (server, player, handler, buf, responseSender) -> {
            for (Map.Entry<String, byte[]> entry : buf.readMap(PacketByteBuf::readString, PacketByteBuf::readByteArray).entrySet()) {
                String[] order = entry.getKey().split("->");
                LOGGER.info(player.getUuidAsString() + "[put:" + order[1] + "]" + order[0] + "   " + order[2]);
                HashMap<Integer, byte[]> list = new HashMap<>();
                if (SERVER_CACHE_MAP.containsKey(order[2])) {
                    list = SERVER_CACHE_MAP.get(order[2]);
                }
                list.put(Integer.valueOf(order[0]), entry.getValue());
                SERVER_CACHE_MAP.put(order[2], list);
                FILE_COUNT_MAP.put(order[2], Integer.valueOf(order[1]));
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(GET_FILE_CANNEL, (server, player, handler, buf, responseSender) -> {
            String url = buf.readString();
            LOGGER.info(player.getUuidAsString() + "[get]" + url);
            if (SERVER_CACHE_MAP.containsKey(url) && FILE_COUNT_MAP.containsKey(url)) {
                HashMap<Integer, byte[]> list = SERVER_CACHE_MAP.get(url);
                Integer count = FILE_COUNT_MAP.get(url);
                if (count == list.size()) {
                    for (Map.Entry<Integer, byte[]> entry : list.entrySet()) {
                        sendFilePacketAsync(player, GET_FILE_CANNEL, getFilePacket(entry.getKey() + "->" + count + "->" + url, entry.getValue()));
                    }
                }
            } else {
                File file = new File(url);
                if (file.exists()) {
                    sendFilePackets(player,url,file,GET_FILE_CANNEL);
                }
            }
        });
    }


}
