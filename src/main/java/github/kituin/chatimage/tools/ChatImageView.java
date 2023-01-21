package github.kituin.chatimage.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.Base64;

import static github.kituin.chatimage.client.ChatImageClient.MOD_ID;

/**
 * @author kitUIN
 */
public class ChatImageView {
    private String fileName;
    private String fileBase64;
    private int width;
    private int height;

    private Identifier texture = null;
    public ChatImageView(String fileName, String fileBase64)
    {
        this.fileName = fileName;
        this.fileBase64 = fileBase64;
        try {
            this.texture = getTexture(fileBase64);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Identifier getTexture(String base64) throws IOException {
        InputStream inputStream = null;
        byte[] bytes = Base64.getDecoder().decode(base64);
        inputStream = new ByteArrayInputStream(bytes);
        NativeImage nativeImage = NativeImage.read(inputStream);
        this.width = nativeImage.getWidth();
        this.height = nativeImage.getHeight();
        NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
        Identifier texture = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(MOD_ID+"/image",
                nativeImageBackedTexture);
        return texture;
    }
    public Identifier getTexture()
    {
        if(this.texture == null)
        {
            try {
                return getTexture(this.fileBase64);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this.texture;
    }
    public String getFileName()
    {
        return this.fileName;
    }
    public int getWidth()
    {
        return this.width;
    }
    public int getHeight()
    {
        return this.height;
    }
}
