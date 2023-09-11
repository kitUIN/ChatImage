package github.kituin.chatimage.tool;

import com.github.chatimagecode.ChatImageCode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.github.chatimagecode.exception.InvalidChatImageUrlException;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.JsonHelper;


/**
 * @author kitUIN
 */
public class ChatImageStyle {
    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE = new HoverEvent.Action<>("show_chatimage", true, ChatImageStyle::fromJson, ChatImageStyle::toJson, ChatImageStyle::fromJson);

    /**
     * 文本 悬浮图片样式
     *
     * @param code {@link ChatImageCode}
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code) {
        return Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE, code)).withColor(Formatting.GREEN);
    }

    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param code {@link ChatImageCode}
     * @return {@link MutableText}
     */
    public static MutableText messageFromCode(ChatImageCode code) {
        MutableText t = (MutableText) Text.of("[");
        if ("codename.chatimage.default".equals(code.getName())) {
            t.append(Text.translatable(code.getName()));
        } else {
            t.append(Text.of(code.getName()));
        }
        t.append("]");
        Style style = ChatImageStyle.getStyleFromCode(code);
        return t.fillStyle(style);
    }


    public static ChatImageCode fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        String code = JsonHelper.getString(jsonObject, "code");
        try {
            return new ChatImageCode(code);
        } catch (InvalidChatImageUrlException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonElement toJson(ChatImageCode code) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code.toString());
        return jsonObject;
    }

    public static ChatImageCode fromJson(Text text) {
        try {
            return new ChatImageCode(text.toString());
        } catch (InvalidChatImageUrlException e) {
            throw new RuntimeException(e);
        }
    }
}
