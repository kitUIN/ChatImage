package github.kituin.chatimage.tool;

import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.exception.InvalidChatImageUrlException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;


/**
 * @author kitUIN
 */
public class ChatImageStyle {
    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE = new HoverEvent.Action<ChatImageCode>("show_chatimage", true, ChatImageStyle::fromJson, ChatImageStyle::toJson, ChatImageStyle::fromJson);

    /**
     * 文本 悬浮图片样式
     *
     * @param code {@link ChatImageCode}
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code) {
        Style style = Style.EMPTY.setHoverEvent(new HoverEvent(SHOW_IMAGE, code));
        return style.setFormatting(TextFormatting.GREEN);
    }


    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param code {@link ChatImageCode}
     * @return {@link StringTextComponent}
     */
    public static StringTextComponent messageFromCode(ChatImageCode code) {
        StringTextComponent t = new StringTextComponent("[");
        if ("codename.chatimage.default".equals(code.getName())) {
            t.appendSibling(new TranslationTextComponent(code.getName()));
        } else {
            t.appendString(code.getName());
        }
        t.appendString("]");
        Style style = ChatImageStyle.getStyleFromCode(code);
        return (StringTextComponent) t.setStyle(style);

    }


    public static ChatImageCode fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.has("code")) {
            String code = jsonObject.get("code").getAsString();
            try {
                return new ChatImageCode(code);
            } catch (InvalidChatImageUrlException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static JsonElement toJson(ChatImageCode code) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code.toString());
        return jsonObject;
    }

    public static ChatImageCode fromJson(ITextComponent text) {
        try {
            return new ChatImageCode(text.toString());
        } catch (InvalidChatImageUrlException e) {
            throw new RuntimeException(e);
        }
    }
}
