package io.github.kituin.chatimage.integration;

import #NativeImage#;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.IClientAdapter;
import net.minecraft.client.Minecraft;
// IF < neoforge-1.21.5
// import net.minecraft.client.renderer.texture.DynamicTexture;
// END IF
import #Component#;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static io.github.kituin.chatimage.ChatImage.MOD_ID;
import static io.github.kituin.chatimage.network.ChatImagePacket.loadFromServer;
import static io.github.kituin.chatimage.network.ChatImagePacket.sendFilePackets;
import static io.github.kituin.ChatImageCode.NetworkHelper.createFilePacket;

public class ChatImageClientAdapter implements IClientAdapter {
    @Override
    public int getTimeOut() {
        return #kituin$ChatImageConfig#.timeout;
    }

    @Override
    public ChatImageFrame.TextureReader<ResourceLocation> loadTexture(InputStream image) throws IOException {
        NativeImage nativeImage = NativeImage.read(image);
// IF >= neoforge-1.21.4
//        ResourceLocation id = #ResourceLocationfromNamespaceAndPath#(MOD_ID , "chatimage");
//        Minecraft.getInstance().getTextureManager().register( id, new DynamicTexture(nativeImage));
// ELSE
//        ResourceLocation id = Minecraft.getInstance().getTextureManager().register(
//                MOD_ID + "/chatimage",
//                new DynamicTexture(nativeImage));
// END IF
        return new ChatImageFrame.TextureReader<>(
                id,
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
        File folder = new File(#kituin$ChatImageConfig#.cachePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public int getMaxFileSize() {
        return CONFIG.MaxFileSize;
    }

    @Override
    public Component getProcessMessage(int i) {
        return Component.translatable("process.chatimage.message", i);
    }


}