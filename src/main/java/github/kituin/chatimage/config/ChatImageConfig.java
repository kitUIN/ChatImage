package github.kituin.chatimage.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author kitUIN
 */
public class ChatImageConfig {
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping().setLenient().setPrettyPrinting()
            .create();

    public String cachePath = "ChatImageCache";

    public int limitWidth = 125;
    public int limitHeight = 125;
    public int paddingLeft = 1;
    public int paddingRight = 1;
    public int paddingTop = 1;
    public int paddingBottom = 1;
    public int gifSpeed = 3;
    public boolean nsfw = false;
    public boolean cqCode = true;
    public int timeout = 5;
    public ChatImageConfig()
    {

    }
    public static ChatImageConfig loadConfig() {
        try {
            ChatImageConfig config;
            File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "chatimageconfig.json");
            if (configFile.exists()) {
                String json = IOUtils.toString(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
                config = GSON.fromJson(json, ChatImageConfig.class);
            } else {
                config = new ChatImageConfig();
            }
            saveConfig(config);
            return config;
        }
        catch(IOException e) {
            e.printStackTrace();
            return new ChatImageConfig();
        }
    }

    public static void saveConfig(ChatImageConfig config) {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "chatimageconfig.json");
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
            writer.write(GSON.toJson(config));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
