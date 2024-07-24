package io.github.kituin.chatimage.integration;

import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.IClientAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;

// IF forge-1.16.5
//import net.minecraft.client.renderer.texture.NativeImage;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.text.TextComponent;
// ELSE
//import net.minecraft.network.chat.MutableComponent;
//import com.mojang.blaze3d.platform.NativeImage;
//import net.minecraft.resources.ResourceLocation;
// END IF
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.ChatImage.MOD_ID;
import static io.github.kituin.chatimage.network.ChatImagePacket.loadFromServer;
import static io.github.kituin.chatimage.network.ChatImagePacket.sendFilePackets;
import static io.github.kituin.ChatImageCode.NetworkHelper.createFilePacket;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;

public class ChatImageClientAdapter implements IClientAdapter {
    @Override
    public int getTimeOut() {
        return CONFIG.timeout;
    }

    @Override
    public ChatImageFrame.TextureReader<ResourceLocation> loadTexture(InputStream image) throws IOException {
        NativeImage nativeImage = NativeImage.read(image);
        return new ChatImageFrame.TextureReader<>(
                Minecraft.getInstance().getTextureManager().register(MOD_ID + "/chatimage",
                        new DynamicTexture(nativeImage)),
                nativeImage.getWidth(),
                nativeImage.getHeight()
        );
    }

    @Override
    public void sendToServer(String url, File file, boolean isToServer) {
        if (isToServer) {
            List<String> bufs = createFilePacket(url, file);
            sendFilePackets(bufs);
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
// IF forge-1.16.5
//    public TextComponent getProcessMessage(int i)  {
// ELSE
//    public MutableComponent getProcessMessage(int i)  {
// END IF
        return createTranslatableComponent("process.chatimage.message", i);
    }
}