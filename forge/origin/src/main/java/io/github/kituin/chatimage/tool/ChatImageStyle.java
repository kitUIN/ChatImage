package io.github.kituin.chatimage.tool;


import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.exception.InvalidChatImageCodeException;

// IF > forge-1.20.2
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.DataResult;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
// END IF


/**
 * @author kitUIN
 */
public class ChatImageStyle {

// IF <= forge-1.20.2
//    public static final #HoverEvent#.Action<ChatImageCode> SHOW_IMAGE =
//            new #HoverEvent#.Action<>("show_chatimage", true,
//                    ChatImageCode::fromJson,
//                    ChatImageCode::toJson,
//                    ChatImageStyle::fromJson);
//    public static ChatImageCode fromJson(#Component# text) {
//        try {
//            return ChatImageCodeInstance.createBuilder().fromCode(text.toString()).build();
//        } catch (InvalidChatImageCodeException e) {
//            return ChatImageCodeInstance.createBuilder().build();
//        }
//    }
// ELSE
//public static final MapCodec<ChatImageCode> MAP_CODEC = RecordCodecBuilder.mapCodec(obj -> obj.group(
//        Codec.STRING.fieldOf("url").forGetter(ChatImageCode::getUrl),
//        Codec.BOOL.optionalFieldOf("nsfw",false).forGetter(ChatImageCode::isNsfw)
//).apply(obj, (url, nsfw) ->  new ChatImageCode.Builder().setNsfw(nsfw).setUrlForce(url).build()));
//    public static final Codec<ChatImageCode> CODEC = MAP_CODEC.codec();
//    public static final #HoverEvent#.Action<ChatImageCode> SHOW_IMAGE  = new #HoverEvent#.Action<>(
//            "show_chatimage",
//            true,
//            CODEC,ChatImageStyle::legacySerializer);
//
//    private static DataResult<ChatImageCode> legacySerializer(Component text) {
//        try {
//            return DataResult.success(  new ChatImageCode.Builder().fromCode(text.toString()).build());
//        } catch (InvalidChatImageCodeException e) {
//            return DataResult.error(() -> "Failed to parse ChatImageCode: " + e.getMessage());
//        }
//    }
// END IF
    /**
     * 文本 悬浮图片样式
     *
     * @param code {@link ChatImageCode}
     * @return 悬浮图片样式
     */
    public static #Style# getStyleFromCode(ChatImageCode code) {
        return getStyleFromCode(code, #ChatFormatting#.GREEN);
    }

    /**
     * 文本 悬浮图片样式
     *
     * @param code {@link ChatImageCode}
     * @param color 颜色
     * @return 悬浮图片样式
     */
    public static #Style# getStyleFromCode(ChatImageCode code, #ChatFormatting# color) {
        #Style# style = #Style#.EMPTY.withHoverEvent(new #HoverEvent#(SHOW_IMAGE, code));
        return style.withColor(color);
    }
    /**
     * 获取悬浮图片样式的Text消息
     *
     * @param code {@link ChatImageCode}
     * @return {@link #MutableComponent#}
     */
    public static #MutableComponent# messageFromCode(ChatImageCode code) {
        #MutableComponent# t = code.messageFromCode(
                SimpleUtil::createLiteralComponent,
                SimpleUtil::createTranslatableComponent,
                #MutableComponent#::append);
        #Style# style = ChatImageStyle.getStyleFromCode(code);
        return t.setStyle(style);
    }

}