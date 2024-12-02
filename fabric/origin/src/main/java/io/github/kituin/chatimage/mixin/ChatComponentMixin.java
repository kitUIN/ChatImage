package io.github.kituin.chatimage.mixin;

import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.client.MinecraftClient;
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
public class #kituin$ChatComponentMixinClass# {
    @ModifyVariable(at = @At("HEAD"),
            method = "#kituin$addMessageMixin#",
            argsOnly = true)
    public Text addMessage(Text message) {
        return replaceMessage(message);
    }
// IF >= fabric-1.19
//    @Unique
//    private #Component#Content chatImage$getContents(#Component# text){
//        return text.getContent();
//    }
// ELSE
//    @Unique
//    private #Component# chatImage$getContents(#Component# text){
//        return text;
//    }
// END IF

    @Unique
    private String chatImage$getText(#PlainTextContents# text){
// IF >= fabric-1.19
//    return text.string();
// ELSE
//        return text.asString();
// END IF
    }

    @SuppressWarnings("t")
    @Unique
    private Text replaceCode(Text text) {
        String checkedText;
        String key = "";
        MutableText player = null;
        boolean isSelf = false;
        MutableText t = text.copy();
        t.getSiblings().clear();
        Style style = text.getStyle();
        t = t.setStyle(style);
        if (chatImage$getContents(text) instanceof #PlainTextContents#) {
            checkedText = chatImage$getText((#PlainTextContents#)chatImage$getContents(text));
        } else if (chatImage$getContents(text) instanceof #TranslatableContents# ttc) {
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if (ChatImageCodeTool.checkKey(key)) {
                player = (MutableText) args[0];
                isSelf = chatImage$getContents(player).toString().equals(chatImage$getContents(MinecraftClient.getInstance().player.getName()).toString());
                if(args[1] instanceof String){
                    checkedText = (String) args[1];
                }else{
                    MutableText contents = (MutableText) args[1];
                    if (chatImage$getContents(contents) instanceof #PlainTextContents#) {
                        checkedText = chatImage$getText((#PlainTextContents#) chatImage$getContents(contents));
                    } else {
                        checkedText = chatImage$getContents(contents).toString();
                    }
                }
            } else {
                return t;
            }
        } else {
            checkedText = chatImage$getContents(text).toString();
        }

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
    private Text replaceMessage(Text message) {
        try {
            MutableText res = (MutableText) replaceCode(message);
            for (Text t : message.getSiblings()) {
                res.append(replaceMessage(t));
            }
            return res;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return message;
        }
    }
}