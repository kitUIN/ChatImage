package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.tool.ChatImageStyle;
import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import #HoverEvent#;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;
import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.createBuilder;
import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;
import static #HoverEvent#.Action.SHOW_TEXT;

// IF >= fabric-1.16.5
//import net.minecraft.client.MinecraftClient;
//import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
// ELSE IF >= forge-1.16.5 || > neoforge-1.20.1
//import net.minecraft.client.Minecraft;
//import static io.github.kituin.chatimage.ChatImage.CONFIG;
// END IF


/**
 * 注入修改文本显示,自动将CICode转换为可鼠标悬浮格式文字
 *
 * @author kitUIN
 */
@Mixin(#ChatComponent#.class)
public class #kituin$ChatComponentMixinClass# {
    @Shadow
    @Final
// IF >= fabric-1.16.5
//    private MinecraftClient client;
// ELSE IF >= forge-1.16.5 || > neoforge-1.20.1
//   private Minecraft minecraft;
// END IF
    @ModifyVariable(at = @At("HEAD"),
            method = "#kituin$addMessageMixin#",
            argsOnly = true)
    public #Component# addMessage(#Component# message) {
        return chatimage$replaceMessage(message);
    }
// IF >= fabric-1.19
//    @Unique
//    private #Component#Content chatImage$getContents(#Component# text){
//        return text.getContent();
//    }
// ELSE IF >= forge-1.19 || > neoforge-1.20.1
//    @Unique
//    private #Component#Contents chatImage$getContents(#Component# text){
//        return text.getContents();
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
//        return text.string();
// ELSE IF >= forge-1.19 || > neoforge-1.20.1
//        return text.text();
// ELSE IF < fabric-1.19
//        return text.asString();
// ELSE
//        return text.getContents();
// END IF
    }

    @SuppressWarnings("t")
    @Unique
    private #Component# chatimage$replaceCode(#Component# text) {
        String checkedText;
        String key = "";
        #MutableComponent# player = null;
        boolean isSelf = false;
        #MutableComponent# originText = text.copy();
        originText.getSiblings().clear();
        #Style# style = text.getStyle();
        if (chatImage$getContents(text) instanceof #PlainTextContents#) {
            checkedText = chatImage$getText((#PlainTextContents#)chatImage$getContents(text));
        } else if (chatImage$getContents(text) instanceof #TranslatableContents#) {
            #TranslatableContents# ttc = (#TranslatableContents#) chatImage$getContents(text);
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if (ChatImageCodeTool.checkKey(key)) {
                player = (#MutableComponent#) args[0];
// IF >= fabric-1.16.5
//                isSelf = chatImage$getContents(player).toString().equals(chatImage$getContents(client.player.getName()).toString());
// ELSE IF >= forge-1.16.5 || > neoforge-1.20.1
//                isSelf = chatImage$getContents(player).toString().equals(chatImage$getContents(minecraft.player.getName()).toString());
// END IF
                if(args[1] instanceof String){
                    checkedText = (String) args[1];
                }else{
                    #MutableComponent# contents = (#MutableComponent#) args[1];
                    if (chatImage$getContents(contents) instanceof #PlainTextContents#) {
                        checkedText = chatImage$getText((#PlainTextContents#) chatImage$getContents(contents));
                    } else {
                        checkedText = chatImage$getContents(contents).toString();
                    }
                }
            } else {
                return originText;
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
            ChatImageCode action = style.getHoverEvent() == null ? null : style.getHoverEvent().getValue(SHOW_IMAGE);
            if (action != null) action.retry();
            try {
                #Component# showText = style.getHoverEvent() == null ? null : style.getHoverEvent().getValue(SHOW_TEXT);
                if (showText != null &&
                        chatImage$getContents(showText) instanceof #PlainTextContents#) {
                    originText.setStyle(
                            style.withHoverEvent(new HoverEvent(
                                    SHOW_IMAGE,
                                    createBuilder()
                                            .fromCode(chatImage$getText(
                                                    (#PlainTextContents#)chatImage$getContents(showText)))
                                            .setIsSelf(isSelf)
                                            .build())));
                }
            } catch (Exception e){
                // LOGGER.error(e.getMessage());
            }
            return originText;
        }
        #MutableComponent# res = createLiteralComponent("");
        ChatImageCodeTool.buildMsg(texts,
                (obj) -> res.append(createLiteralComponent(obj).setStyle(style)),
                (obj) -> res.append(ChatImageStyle.messageFromCode(obj))
        );
        return player == null ? res : createTranslatableComponent(key, player, res).setStyle(style);
    }

    @Unique
    private #Component# chatimage$replaceMessage(#Component# message) {
        try {
            #MutableComponent# res = (#MutableComponent#) chatimage$replaceCode(message);
            for (#Component# t : message.getSiblings()) {
                res.append(chatimage$replaceMessage(t));
            }
            return res;
        } catch (Exception e) {
            LOGGER.warn("识别失败:{}", e.getMessage());
            return message;
        }
    }
}