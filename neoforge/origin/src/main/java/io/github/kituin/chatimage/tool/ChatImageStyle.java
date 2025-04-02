package io.github.kituin.chatimage.tool;


import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.exception.InvalidChatImageCodeException;
import net.minecraft.ChatFormatting;
import #Component#;
import #HoverEvent#;
import #MutableComponent#;
import #Style#;
// IF neoforge-1.20.2
// ELSE IF >= neoforge-1.20.3
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.DataResult;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
// END IF

/**
 * @author kitUIN
 */
public class ChatImageStyle {
// IF neoforge-1.20.2
//     public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE =
//             new HoverEvent.Action<>("show_chatimage", true,
//                     ChatImageCode::fromJson,
//                     ChatImageCode::toJson,
//                     ChatImageStyle::fromJson);
// ELSE IF >= neoforge-1.21.5
//    public static record ShowImage(ChatImageCode value) implements HoverEvent {
//        public static final com.mojang.serialization.MapCodec<ShowImage> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(obj -> obj.group(
//                ChatImageStyle.MAP_CODEC.forGetter(ShowImage::value)
//        ).apply(obj, ShowImage::new));
//
//
//        public ShowImage(ChatImageCode value) {
//            this.value = value;
//        }
//
//        public Action action() {
//            return Action.valueOf("SHOW_IMAGE");
//        }
//
//        public ChatImageCode value() {
//            return this.value;
//        }
//    }
// END IF

    // IF >= neoforge-1.20.3
//    public static final MapCodec<ChatImageCode> MAP_CODEC = RecordCodecBuilder.mapCodec(obj -> obj.group(
//            Codec.STRING.fieldOf("url").forGetter(ChatImageCode::getUrl),
//            Codec.BOOL.optionalFieldOf("nsfw", false).forGetter(ChatImageCode::isNsfw)
//    ).apply(obj, (url, nsfw) -> new ChatImageCode.Builder().setNsfw(nsfw).setUrlForce(url).build()));
//    public static final Codec<ChatImageCode> CODEC = MAP_CODEC.codec();
// END IF
// IF >= neoforge-1.20.3 && < neoforge-1.21.5
//     public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE = new HoverEvent.Action<ChatImageCode>(
//             "show_chatimage",
//             true,
//             CODEC,
// IF >= neoforge-1.20.5
//             (text, p_329862_) -> legacySerializer(text));
// ELSE
//              ChatImageStyle::legacySerializer);
// END IF
//     private static DataResult<ChatImageCode> legacySerializer(Component text) {
//         try {
//             return DataResult.success(new ChatImageCode.Builder().fromCode(text.toString()).build());
//         } catch (InvalidChatImageCodeException e) {
//             return DataResult.error(() -> "Failed to parse ChatImageCode: " + e.getMessage());
//         }
//     }
// END IF

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
     * @param code  {@link ChatImageCode}
     * @param color 颜色
     * @return 悬浮图片样式
     */
    public static Style getStyleFromCode(ChatImageCode code, ChatFormatting color) {
        Style style = Style.EMPTY.withHoverEvent(
// IF >= neoforge-1.21.5
//                new ShowImage(code)
// ELSE
//         new HoverEvent(SHOW_IMAGE, code)
// END IF
        );
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


    public static ChatImageCode fromJson(Component text) {
        try {
            return ChatImageCodeInstance.createBuilder().fromCode(text.toString()).build();
        } catch (InvalidChatImageCodeException e) {
            return ChatImageCodeInstance.createBuilder().build();
        }
    }
}