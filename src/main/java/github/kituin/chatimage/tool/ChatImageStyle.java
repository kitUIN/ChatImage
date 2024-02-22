package github.kituin.chatimage.tool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.exception.InvalidChatImageCodeException;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


/**
 * @author kitUIN
 */
public class ChatImageStyle {
    public static final MapCodec<ChatImageCode> MAP_CODEC = RecordCodecBuilder.mapCodec(obj -> obj.group(
            Codec.STRING.fieldOf("url").forGetter(ChatImageCode::getUrl),
            Codec.BOOL.optionalFieldOf("nsfw",false).forGetter(ChatImageCode::isNsfw)
    ).apply(obj, (url, nsfw) ->  new ChatImageCode.Builder().setNsfw(nsfw).setUrlForce(url).build()));
    public static final Codec<ChatImageCode> CODEC = MAP_CODEC.codec();
    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE  = new HoverEvent.Action<>(
            "show_chatimage",
            true,
            CODEC,
            ChatImageStyle::legacySerializer);

    private static DataResult<ChatImageCode> legacySerializer(Text text) {
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
        return getStyleFromCode(code, Formatting.GREEN);
    }

    /**
     * 文本 悬浮图片样式
     *
     * @param code  {@link ChatImageCode}
     * @param color 颜色
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code, Formatting color) {
        Style style = Style.EMPTY.withHoverEvent(new HoverEvent(SHOW_IMAGE, code));
        return style.withColor(color);
    }

    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param code {@link ChatImageCode}
     * @return {@link MutableText}
     */
    public static MutableText messageFromCode(ChatImageCode code) {
        MutableText t = code.messageFromCode(
                Text::literal,
                Text::translatable,
                MutableText::append);
        Style style = ChatImageStyle.getStyleFromCode(code);
        return t.fillStyle(style);
    }

}
