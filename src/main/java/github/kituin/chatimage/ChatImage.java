package github.kituin.chatimage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.HashMap;

import static github.kituin.chatimage.tool.ChatImageUrl.getFilePacket;


/**
 * @author kitUIN
 */

public class ChatImage implements ModInitializer {
    public static Identifier FILE_CANNEL = new Identifier("chatimage", "filecannel");
    public static Identifier GET_FILE_CANNEL = new Identifier("chatimage", "getfilecannel");
    public static HashMap<String, byte[]> SERVER_MAP = new HashMap<String, byte[]>();

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(FILE_CANNEL, (server, player, handler, buf, responseSender) -> {
            SERVER_MAP.putAll(buf.readMap(PacketByteBuf::readString, PacketByteBuf::readByteArray));
        });
        ServerPlayNetworking.registerGlobalReceiver(GET_FILE_CANNEL, (server, player, handler, buf, responseSender) -> {
            String url = buf.readString();
            if (SERVER_MAP.containsKey(url)) {
                ServerPlayNetworking.send(player, GET_FILE_CANNEL, getFilePacket(url, SERVER_MAP.get(url)));
            } else {
                File file = new File(url);
                if (file.exists()) {
                    PacketByteBuf packet = getFilePacket(url, file);
                    if (packet != null) {
                        ServerPlayNetworking.send(player, GET_FILE_CANNEL, packet);
                    }
                }
            }
        });
    }
}
