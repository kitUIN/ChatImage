package github.kituin.chatimage.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

import static github.kituin.chatimage.client.ChatImageClient.MOD_ID;
import static github.kituin.chatimage.tools.HttpUtils.clock;


/**
 * @author kitUIN
 */
public class ChatImageView {
    private String url;
    private int width;
    private int height;
    private String message = "";

    private Identifier texture;
    public ChatImageView(String url){

        this.url = url;

    }
    private Identifier getTexture(String url) throws IOException {
        InputStream inputStream = null;
        if(url.startsWith("http"))
        {
            File datas = new File("ChatImages");
            if(!datas.exists())
            {
                datas.mkdirs();
            }
            Path path = null;
            try{
                 path = FileSystems.getDefault().getPath(url.replace("http://","").replace("https://",""));
            }catch ( java.nio.file.InvalidPathException ep)
            {
                throw new IOException();
            }
            File temp = new File("ChatImages/" + path.getFileName().toString());
            if(temp.exists()) {

                inputStream =  new FileInputStream(temp);
            }
            else {
                if(clock == 1)
                {
                    HttpUtils.getInputStream(url);
                }
                 throw new IOException();
            }
        } else if (url.startsWith("data:image/"))
         {
             String base64 = url.replace("data:image/jpeg;base64,", "").replace("data:image/png;base64,", "");
             byte[] bytes = Base64.getDecoder().decode(base64);
             inputStream = new ByteArrayInputStream(bytes);
        }
        else if(url.startsWith("file:///"))
        {
            url = url.replace("\\\\", "/").replace("\\","/");
            URI url2 = URI.create(url);
            System.out.println(url2);
            File file = new  File(url2);
            inputStream = new FileInputStream(file);
        }
        else
        {
            throw new IOException();
        }

        NativeImage nativeImage = NativeImage.read(inputStream);
        this.width = nativeImage.getWidth();
        this.height = nativeImage.getHeight();
        NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
        return MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(MOD_ID+"/image",
                nativeImageBackedTexture);
    }
    public Identifier getTexture() throws IOException {
        if(this.texture == null)
        {
            return getTexture(this.url);
        }
        return this.texture;
    }
    public int getWidth()
    {
        return this.width;
    }
    public int getHeight()
    {
        return this.height;
    }
    public String getMessage()
    {
        return this.message;
    }
}
