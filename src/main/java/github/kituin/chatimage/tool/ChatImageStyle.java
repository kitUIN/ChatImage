package github.kituin.chatimage.tool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.exception.InvalidChatImageCodeException;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;


/**
 * @author kitUIN
 */
public class ChatImageStyle {
    public static final MapCodec<ChatImageCode> MAP_CODEC = RecordCodecBuilder.mapCodec(obj -> obj.group(
            Codec.STRING.fieldOf("url").forGetter(ChatImageCode::getUrl),
            Codec.BOOL.fieldOf("nsfw").forGetter(ChatImageCode::isNsfw)
    ).apply(obj, (url, nsfw) ->  new ChatImageCode.Builder().setNsfw(nsfw).setUrlForce(url).build()));
    public static final Codec<ChatImageCode> CODEC = MAP_CODEC.codec();
    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE  = new HoverEvent.Action<>(
            "show_chatimage",
            true,
            CODEC,
            ChatImageStyle::legacySerializer);

    private static DataResult<ChatImageCode> legacySerializer(Component text) {
        try {
            return DataResult.success(  new ChatImageCode.Builder().fromCode(text.toString()).build());
        } catch (InvalidChatImageCodeException e) {
            return DataResult.error(() -> "Failed to parse ChatImageCode: " + e.getMessage());
        }
    }
    /**
     * 文本 悬浮图片样式
     *
     * @param code {@link ChatImageCode}
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code) {
        return getStyleFromCode(code, ChatFormatting.GREEN);
    }

    /**
     * 文本 悬浮图片样式
     *
     * @param code {@link ChatImageCode}
     * @param color 颜色
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code, ChatFormatting color) {
        Style style = Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE, code));
        return style.withColor(color);
    }
    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param code {@link ChatImageCode}
     * @return {@link MutableComponent}
     */
    public static MutableComponent messageFromCode(ChatImageCode code) {
        MutableComponent t = code.messageFromCode(
                Component::literal,
                Component::translatable,
                MutableComponent::append);
        Style style = ChatImageStyle.getStyleFromCode(code);
        return t.setStyle(style);
    }


}
