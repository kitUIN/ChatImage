package io.github.kituin.chatimage.tool;

import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.exception.InvalidChatImageCodeException;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
// IF >= fabric-1.20.3
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
// ELSE
// import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
// END IF

/**
 * @author kitUIN
 */
public class ChatImageStyle {
    // IF >= fabric-1.20.3
//    public static final MapCodec<ChatImageCode> MAP_CODEC = RecordCodecBuilder.mapCodec(obj -> obj.group(
//            Codec.STRING.fieldOf("url").forGetter(ChatImageCode::getUrl),
//            Codec.BOOL.optionalFieldOf("nsfw", false).forGetter(ChatImageCode::isNsfw)
//    ).apply(obj, (url, nsfw) -> new ChatImageCode.Builder().setNsfw(nsfw).setUrlForce(url).build()));
//    public static final Codec<ChatImageCode> CODEC = MAP_CODEC.codec();
//
    // END IF
// IF >= fabric-1.21.5
//    public static record ShowImage(ChatImageCode value) implements HoverEvent {
//        public static final MapCodec<ShowImage> CODEC = RecordCodecBuilder.mapCodec(obj -> obj.group(
//                Codec.STRING.fieldOf("url").forGetter(ShowImage::getUrl),
//                Codec.BOOL.optionalFieldOf("nsfw", false).forGetter(ShowImage::isNsfw)
//        ).apply(obj, (url, nsfw) -> new ShowImage(new ChatImageCode.Builder().setNsfw(nsfw).setUrlForce(url).build())));
//        public ShowImage(ChatImageCode value) {
//            this.value = value;
//        }
//
//        public Action getAction() {
//            return Action.valueOf("SHOW_IMAGE");
//        }
//
//        public ChatImageCode value() {
//            return this.value;
//        }
//        public String getUrl() {
//            return this.value.getUrl();
//        }
//        public boolean isNsfw() {
//            return this.value.isNsfw();
//        }
//    }
// ELSE IF >= fabric-1.20.5
//     public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE = new HoverEvent.Action<>(
//         "show_chatimage",
//         true,
//         CODEC,
//         ChatImageStyle::legacySerializer);
//     private static DataResult<ChatImageCode> legacySerializer(Text text, @Nullable RegistryOps<?> ops) {
//         try {
//             return DataResult.success(new ChatImageCode.Builder().fromCode(text.toString()).build());
//         } catch (InvalidChatImageCodeException e) {
//             return DataResult.error(() -> "Failed to parse ChatImageCode: " + e.getMessage());
//         }
//     }
// ELSE IF >= fabric-1.20.3
//    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE = new HoverEvent.Action<>(
//            "show_chatimage",
//            true,
//            CODEC,
//            ChatImageStyle::legacySerializer);
//    private static DataResult<ChatImageCode> legacySerializer(Text text) {
//        try {
//            return DataResult.success(new ChatImageCode.Builder().fromCode(text.toString()).build());
//        } catch (InvalidChatImageCodeException e) {
//            return DataResult.error(() -> "Failed to parse ChatImageCode: " + e.getMessage());
//        }
//    }
// ELSE
//    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE =
//        new HoverEvent.Action<>("show_chatimage", true,
//                ChatImageCode::fromJson,
//                ChatImageCode::toJson,
//                ChatImageStyle::fromJson);
// END IF


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
        Style style = Style.EMPTY.withHoverEvent(
// IF >= fabric-1.21.5
//                new ShowImage(code)
// ELSE
//                 new HoverEvent(SHOW_IMAGE, code)
// END IF
        );
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
// IF fabric-1.16.5 || fabric-1.18.2
//                LiteralText::new,
//                TranslatableText::new,
//                (obj, s)-> (BaseText) obj.append(s));
// ELSE
//                Text::literal,
//                Text::translatable,
//                MutableText::append);
// END IF
        Style style = ChatImageStyle.getStyleFromCode(code);
        return t.fillStyle(style);
    }

// IF < fabric-1.20.3
//    public static ChatImageCode fromJson(Text text) {
//        try {
//            return ChatImageCodeInstance.createBuilder().fromCode(text.toString()).build();
//        } catch (InvalidChatImageCodeException e) {
//            return ChatImageCodeInstance.createBuilder().build();
//        }
//    }
// END IF



}
