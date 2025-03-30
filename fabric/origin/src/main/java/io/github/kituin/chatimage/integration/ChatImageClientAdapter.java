package io.github.kituin.chatimage.integration;

import com.google.common.collect.Maps;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.IClientAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import #Component#;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
// IF >= fabric-1.20.5
//import io.github.kituin.chatimage.network.FileChannelPacket;
// END IF
import static io.github.kituin.ChatImageCode.NetworkHelper.createFilePacket;
import static #kituin$ChatImageConfig#;
import static io.github.kituin.chatimage.client.ChatImageClient.MOD_ID;
import static io.github.kituin.chatimage.network.ChatImagePacket.*;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;

public class ChatImageClientAdapter implements IClientAdapter {
// IF >= fabric-1.21.4
//     private final Map<String, Integer> dynamicIdCounters = Maps.newHashMap();
//     public Identifier registerDynamicTexture(String prefix, NativeImageBackedTexture texture) {
//         Integer integer = (Integer)this.dynamicIdCounters.get(prefix);
//         if (integer == null) {
//             integer = 1;
//         } else {
//             integer = integer + 1;
//         }
//
//         this.dynamicIdCounters.put(prefix, integer);
//         Identifier identifier = Identifier.ofVanilla(String.format(Locale.ROOT, "dynamic/%s_%d", prefix, integer));
//         MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, texture);
//         return identifier;
//     }
// END IF

    @Override
    public int getTimeOut() {
        return CONFIG.timeout;
    }

    @Override
    public ChatImageFrame.TextureReader<Identifier> loadTexture(InputStream image) throws IOException {
        NativeImage nativeImage = NativeImage.read(image);
        return new ChatImageFrame.TextureReader<>(
// IF >= fabric-1.21.4
//                registerDynamicTexture(
// ELSE
//                         MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(
// END IF
                        MOD_ID + "/chatimage",
                        new NativeImageBackedTexture(() -> "textureName",nativeImage)),
                nativeImage.getWidth(),
                nativeImage.getHeight()
        );
    }

    @Override
    public void sendToServer(String url, File file, boolean isToServer) {
        if (isToServer) {
            List<String> stringList = createFilePacket(url, file);
// IF >= fabric-1.20.5
//            sendPacketAsync(FileChannelPacket::new, stringList);
// ELSE
//            sendPacketAsync(FILE_CHANNEL, stringList);
// END IF
        } else {
            loadFromServer(url);
        }
    }

    @Override
    public void checkCachePath() {
        File folder = new File(CONFIG.cachePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public int getMaxFileSize() {
        return CONFIG.MaxFileSize;
    }

    @Override
    public Text getProcessMessage(int i) {
        return createTranslatableComponent("process.chatimage.message", i);
    }


}