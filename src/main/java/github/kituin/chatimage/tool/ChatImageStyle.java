package github.kituin.chatimage.tool;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.kituin.chatimage.exception.InvalidChatImageCodeException;
import github.kituin.chatimage.exception.InvalidChatImageUrlException;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;


/**
 * @author kitUIN
 */
public class ChatImageStyle {
    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE = new HoverEvent.Action<>("show_chatimage", true, ChatImageStyle::fromJson, ChatImageStyle::toJson, ChatImageStyle::fromJson);

    /**
     * 文本 悬浮图片样式
     *
     * @param code [CICode]
     * @return 解析成功返回, 失败返回{@link Style#EMPTY}
     */
    public static Style getStyleFromCode(String code) {
        ChatImageCode view;
        try {
            view = ChatImageCode.of(code);
        } catch (InvalidChatImageCodeException e) {
            return Style.EMPTY;
        }
        Style style = Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE, view));
        return style.withColor(ChatFormatting.GREEN);
    }

    /**
     * 文本 悬浮图片样式
     *
     * @param code {@link ChatImageCode}
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code) {
        Style style = Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE, code));
        return style.withColor(ChatFormatting.GREEN);
    }

    /**
     * 文本 悬浮图片样式
     *
     * @param url ChatImageUrl
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageUrl url) {
        Style style = Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE, new ChatImageCode(url)));
        return style.withColor(ChatFormatting.GREEN);
    }

    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param message 消息
     * @param code    [CICode]
     * @return {@link MutableComponent}
     */
    public static MutableComponent messageFromCode(String message, String code) {
        MutableComponent text = Component.literal(message);
        Style style = ChatImageStyle.getStyleFromCode(code);
        return text.withStyle(style);
    }

    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param message 消息
     * @param code    {@link ChatImageCode}
     * @return {@link MutableComponent}
     */
    public static MutableComponent messageFromCode(String message, ChatImageCode code) {
        MutableComponent text = Component.literal(message);
        Style style = ChatImageStyle.getStyleFromCode(code);
        return text.withStyle(style);
    }

    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param code {@link ChatImageCode}
     * @return {@link MutableComponent}
     */
    public static MutableComponent messageFromCode(ChatImageCode code) {
        MutableComponent text = Component.literal("[").append(code.getName()).append("]");
        Style style = ChatImageStyle.getStyleFromCode(code);
        return text.withStyle(style);
    }

    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param message 消息
     * @param url     图片地址 {@link ChatImageUrl}
     * @return {@link MutableComponent}
     */
    public static MutableComponent messageFromCode(String message, ChatImageUrl url) {
        MutableComponent text = Component.literal(message);
        Style style = ChatImageStyle.getStyleFromCode(url);
        return text.withStyle(style);
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

    public static ChatImageCode fromJson(Component text) {
        try {
            return new ChatImageCode(text.toString());
        } catch (InvalidChatImageUrlException e) {
            throw new RuntimeException(e);
        }
    }
}
