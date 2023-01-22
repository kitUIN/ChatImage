package github.kituin.chatimage.tools;

import github.kituin.chatimage.Exceptions.InvalidChatImageCodeException;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static github.kituin.chatimage.tools.ChatImageTool.SHOW_IMAGE;

/**
 * @author kitUIN
 */
public class ChatImageStyle {
    /**
     * 文本 悬浮图片样式
     * @param code [CICode]
     * @return 解析成功返回,失败返回{@link Style#EMPTY}
     */
    public static Style getStyleFromCode(String code)
    {
        ChatImageCode view;
        try {
            view = ChatImageCode.of(code);
        } catch (InvalidChatImageCodeException e) {
            return Style.EMPTY;
        }
        Style style = Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE,view));
        return style.withColor(Formatting.GREEN);
    }
    /**
     * 文本 悬浮图片样式
     * @param code  {@link ChatImageCode}
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code)
    {
        Style style = Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE,code));
        return style.withColor(Formatting.GREEN);
    }
    /**
     * 文本 悬浮图片样式
     * @param url ChatImageUrl
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageUrl url)
    {
        Style style = Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE,new ChatImageCode(url)));
        return style.withColor(Formatting.GREEN);
    }

    /**
     * 获取悬浮图片样式的Text消息
     * @param message 消息
     * @param code [CICode]
     * @return {@link MutableText}
     */
    public static MutableText messageFromCode(String message, String code)
    {
        MutableText text = (MutableText) Text.of(message);
        Style style = ChatImageStyle.getStyleFromCode(code);
        return text.fillStyle(style);
    }
    /**
     * 获取悬浮图片样式的Text消息
     * @param message 消息
     * @param code {@link ChatImageCode}
     * @return {@link MutableText}
     */
    public static MutableText messageFromCode(String message, ChatImageCode code)
    {
        MutableText text = (MutableText) Text.of(message);
        Style style = ChatImageStyle.getStyleFromCode(code);
        return text.fillStyle(style);
    }
    /**
     * 获取悬浮图片样式的Text消息
     * @param message 消息
     * @param url 图片地址 {@link ChatImageUrl}
     * @return {@link MutableText}
     */
    public static MutableText messageFromCode(String message, ChatImageUrl url)
    {
        MutableText text = (MutableText) Text.of(message);
        Style style = ChatImageStyle.getStyleFromCode(url);
        return text.fillStyle(style);
    }
}
