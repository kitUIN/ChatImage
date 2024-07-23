package io.github.kituin.chatimage.mixin;

import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.client.MinecraftClient;
// IF < fabric-1.20
//import net.minecraft.client.gui.DrawableHelper;
// END IF
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;
import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralText;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableText;


/**
 * 注入修改文本显示,自动将CICode转换为可鼠标悬浮格式文字
 *
 * @author kitUIN
 */
@Mixin(ChatHud.class)
// IF >= fabric-1.20
public class ChatHudMixin {
// ELSE
//public class ChatHudMixin extends DrawableHelper {
// END IF
    @ModifyVariable(at = @At("HEAD"),
// IF > fabric-1.18.2
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
// ELSE
//            method = "addMessage(Lnet/minecraft/text/Text;IIZ)V",
// END IF
            argsOnly = true)
    public Text addMessage(Text message) {
        return replaceMessage(message);
    }


    @SuppressWarnings("t")
    @Unique
    private static Text replaceCode(Text text) {
        String checkedText;
        String key = "";
        MutableText player = null;
        boolean isSelf = false;
        MutableText t = text.copy();
        t.getSiblings().clear();
        Style style = text.getStyle();
        t = t.setStyle(style);
// IF > fabric-1.20
        if (text.getContent() instanceof PlainTextContent) {
            checkedText = ((PlainTextContent) text.getContent()).string();
        } else if (text.getContent() instanceof TranslatableTextContent ttc) {
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if (ChatImageCodeTool.checkKey(key)) {
                player = (MutableText) args[0];
                isSelf = player.getContent().toString().equals(MinecraftClient.getInstance().player.getName().getContent().toString());
                MutableText contents = (MutableText) args[1];
                if (contents.getContent() instanceof PlainTextContent) {
                    checkedText = ((PlainTextContent) contents.getContent()).string();
                } else {
                    checkedText = contents.getContent().toString();
                }
            } else {
                return t;
            }
        } else {
            checkedText = text.getContent().toString();
        }
// ELSE IF > fabric-1.18.2
//        if (text.getContent() instanceof LiteralTextContent) {
//            checkedText = ((LiteralTextContent) text.getContent()).string();
//        } else if (text.getContent() instanceof TranslatableTextContent ttc) {
//            key = ttc.getKey();
//            Object[] args = ttc.getArgs();
//            if (ChatImageCodeTool.checkKey(key)) {
//                player = (MutableText) args[0];
//                isSelf = player.getContent().toString().equals(MinecraftClient.getInstance().player.getName().getContent().toString());
//                MutableText contents = (MutableText) args[1];
//                if (contents.getContent() instanceof LiteralTextContent) {
//                    checkedText = ((LiteralTextContent) contents.getContent()).string();
//                } else {
//                    checkedText = contents.getContent().toString();
//                }
//            } else {
//                return t;
//            }
//        } else {
//            checkedText = text.getContent().toString();
//        }
// ELSE
//        if (text instanceof TranslatableText ttc) {
//            key = ttc.getKey();
//            Object[] args = ttc.getArgs();
//            if (ChatImageCodeTool.checkKey(key)) {
//                player = (LiteralText) args[0];
//                isSelf = player.asString().equals(MinecraftClient.getInstance().player.getName().asString());
//            }
//            if (args[1] instanceof String content) {
//                checkedText = content;
//            } else {
//                MutableText contents = (MutableText) args[1];
//                checkedText = contents.asString();
//            }
//        } else {
//            checkedText = text.asString();
//        }
// END IF


        // 尝试解析CQ码
        if (CONFIG.cqCode) checkedText = ChatImageCodeTool.checkCQCode(checkedText);

        ChatImageBoolean allString = new ChatImageBoolean(false);

        // 尝试解析CICode
        List<Object> texts = ChatImageCodeTool.sliceMsg(checkedText, isSelf, allString, (e) -> LOGGER.error(e.getMessage()));
        // 尝试解析URL
        if (CONFIG.checkImageUri) ChatImageCodeTool.checkImageUri(texts, isSelf, allString);

        // 无识别则返回原样
        if (allString.isValue()) {
            ChatImageCode action = style.getHoverEvent() == null ? null : style.getHoverEvent().getValue(ChatImageStyle.SHOW_IMAGE);
            if (action != null) action.retry();
            return t;
        }
        MutableText res = createLiteralText("");
        ChatImageCodeTool.buildMsg(texts,
                (obj) -> res.append(createLiteralText(obj).setStyle(style)),
                (obj) -> res.append(ChatImageStyle.messageFromCode(obj))
        );
        return player == null ? res : createTranslatableText(key, player, res).setStyle(style);
    }

    @Unique
    private static Text replaceMessage(Text message) {
        try {
            MutableText res = (MutableText) replaceCode(message);
            for (Text t : message.getSiblings()) {
                res.append(replaceMessage(t));
            }
            return res;
        } catch (Exception e) {
            return message;
        }
    }
}