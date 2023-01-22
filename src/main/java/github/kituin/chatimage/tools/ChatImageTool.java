package github.kituin.chatimage.tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.kituin.chatimage.Exceptions.InvalidChatImageUrlException;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

public class ChatImageTool {
    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE = new HoverEvent.Action<>("show_chatimage", true, ChatImageTool::fromJson, ChatImageTool::toJson, ChatImageTool::fromJson);

    public static ChatImageCode fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        String code = JsonHelper.getString(jsonObject, "code");
        try {
            return new ChatImageCode(code);
        } catch (InvalidChatImageUrlException e) {
            throw new RuntimeException(e);
        }
    }
    public static JsonElement toJson(ChatImageCode code)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code.toString());
        return  jsonObject;
    }
    public static ChatImageCode fromJson(Text text)
    {
        try {
            return new ChatImageCode(text.toString());
        } catch (InvalidChatImageUrlException e) {
            throw new RuntimeException(e);
        }
    }
}
