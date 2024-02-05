package github.kituin.chatimage.tool;

import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.exception.InvalidChatImageCodeException;
import net.minecraft.util.text.*;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.HoverEvent;

import java.awt.*;


/**
 * @author kitUIN
 */
public class ChatImageStyle {
    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE =
            new HoverEvent.Action<>("show_chatimage", true,
                    ChatImageCode::fromJson,
                    ChatImageCode::toJson,
                    ChatImageStyle::fromJson);
    /**
     * 文本 悬浮图片样式
     *
     * @param code {@link ChatImageCode}
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code) {
        return getStyleFromCode(code, TextFormatting.GREEN);
    }

    /**
     * 文本 悬浮图片样式
     *
     * @param code {@link ChatImageCode}
     * @param color 颜色
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code, TextFormatting color) {
        Style style = Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE, code));
        return style.withColor(color);
    }
    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param code {@link ChatImageCode}
     * @return {@link IFormattableTextComponent}
     */
    public static IFormattableTextComponent messageFromCode(ChatImageCode code) {
        IFormattableTextComponent t = code.messageFromCode(
                StringTextComponent::new,
                TranslationTextComponent::new,
                (baseComponent, pSibling) -> (TextComponent) baseComponent.append(pSibling));
        Style style = ChatImageStyle.getStyleFromCode(code);
        return t.setStyle(style);
    }



    public static ChatImageCode fromJson(ITextComponent text) {
        try {
            return ChatImageCodeInstance.createBuilder().fromCode(text.toString()).build();
        } catch (InvalidChatImageCodeException e) {
            return ChatImageCodeInstance.createBuilder().build();
        }
    }
}
