package io.github.kituin.chatimage.integration;

import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.IClientAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
// IF >= fabric-1.20.5
//import io.github.kituin.chatimage.network.FileChannelPacket;
// END IF
import static io.github.kituin.ChatImageCode.NetworkHelper.createFilePacket;
import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.client.ChatImageClient.MOD_ID;
import static io.github.kituin.chatimage.network.ChatImagePacket.*;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;

public class ChatImageClientAdapter implements IClientAdapter {
    @Override
    public int getTimeOut() {
        return CONFIG.timeout;
    }

    @Override
    public ChatImageFrame.TextureReader<Identifier> loadTexture(InputStream image) throws IOException {
        NativeImage nativeImage = NativeImage.read(image);
        return new ChatImageFrame.TextureReader<>(
                MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(MOD_ID + "/chatimage",
                        new NativeImageBackedTexture(nativeImage)),
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