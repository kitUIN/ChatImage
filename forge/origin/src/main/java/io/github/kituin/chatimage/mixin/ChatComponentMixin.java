package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.tool.ChatImageStyle;
import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;
import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;


/**
 * 注入修改文本显示,自动将CICode转换为可鼠标悬浮格式文字
 *
 * @author kitUIN
 */
@SuppressWarnings("t")
@Mixin(#ChatComponent#.class)
public class #kituin$ChatComponentMixinClass#  {
    @Shadow
    @Final
    private Minecraft minecraft;
    
    @ModifyVariable(at = @At("HEAD"),
            method = "#kituin$addMessageMixin#",
            argsOnly = true)
    public #Component# addMessage(#Component# p_241484_) {
        return chatimage$replaceMessage(p_241484_);
    }

// IF >= forge-1.19
//    @Unique
//  private #Component#Contents chatImage$getContents(#Component# text){
//    return text.getContents();
//}
// ELSE
//    @Unique
//    private #Component# chatImage$getContents(#Component# text){
//    return text;
//    }
// END IF

    @Unique
    private String chatImage$getText(#PlainTextContents# text){
// IF >= forge-1.19
//    return text.text();
// ELSE
//    return text.getContents();
// END IF
    }

    @Unique
    private #Component# chatimage$replaceCode(#Component# text) {
        String checkedText = "";
        String key = "";
        #MutableComponent# player = null;
        boolean isSelf = false;
        if (chatImage$getContents(text) instanceof #PlainTextContents#) {
            #PlainTextContents# lc = (#PlainTextContents#) chatImage$getContents(text);
            checkedText = chatImage$getText(lc);
        } else if (chatImage$getContents(text) instanceof #TranslatableContents#) {
            #TranslatableContents# ttc = (#TranslatableContents#) chatImage$getContents(text);
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if (ChatImageCodeTool.checkKey(key)) {
                player = (#MutableComponent#) args[0];
                isSelf = chatImage$getContents(player).toString().equals(chatImage$getContents(this.minecraft.player.getName()).toString());
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
            }
        } else {
            checkedText = chatImage$getContents(text).toString();
        }

        // 尝试解析CQ码
        if (CONFIG.cqCode) checkedText = ChatImageCodeTool.checkCQCode(checkedText);

        #Style# style = text.getStyle();
        ChatImageBoolean allString = new ChatImageBoolean(false);

        // 尝试解析CICode
        List<Object> texts = ChatImageCodeTool.sliceMsg(checkedText, isSelf, allString, (e) -> LOGGER.error(e.getMessage()));
        // 尝试解析URL
        if (CONFIG.checkImageUri) ChatImageCodeTool.checkImageUri(texts, isSelf, allString);

        // 无识别则返回原样
        if (allString.isValue()) {
            if (style.getHoverEvent() != null) {
                ChatImageCode action = style.getHoverEvent().getValue(ChatImageStyle.SHOW_IMAGE);
                if (action != null) action.retry();
            }
            #MutableComponent# res = text.copy();
            res.getSiblings().clear();
            return res;
        }
        #MutableComponent# res = createLiteralComponent("");
        ChatImageCodeTool.buildMsg(texts,
                (obj) -> res.append(createLiteralComponent(obj).setStyle(style)),
                (obj) -> res.append(ChatImageStyle.messageFromCode(obj))
        );
        if (player == null) {
            return res;
        } else {
            return createTranslatableComponent(key, player, res).setStyle(style);
        }
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
            LOGGER.error("识别失败:{}", e.getMessage());
            return message;
        }
    }
}