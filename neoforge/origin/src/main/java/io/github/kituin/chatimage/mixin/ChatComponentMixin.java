package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.tool.ChatImageStyle;
import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.createBuilder;
import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT;


/**
 * 注入修改文本显示,自动将CICode转换为可鼠标悬浮格式文字
 *
 * @author kitUIN
 */
@Mixin(ChatComponent.class)
public class ChatComponentMixin  {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow @Final private static Logger LOGGER;

    @ModifyVariable(at = @At("HEAD"),
             method = "#kituin$addMessageMixin#",
            argsOnly = true)
    public Component addMessage(Component p_241484_) {
        return chatimage$replaceMessage(p_241484_);
    }

    
    @Unique
    private Component chatimage$replaceCode(Component text) {
        String checkedText = "";
        String key = "";
        MutableComponent player = null;
        boolean isSelf = false;
        MutableComponent originText = text.copy();
        originText.getSiblings().clear();
        Style style = text.getStyle();
        if (text.getContents() instanceof #PlainTextContents# lc) {
            checkedText = lc.text();
        } else if (text.getContents() instanceof TranslatableContents ttc) {
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if (ChatImageCodeTool.checkKey(key)) {
                player = (MutableComponent) args[0];
                isSelf = player.getContents().toString().equals(this.minecraft.player.getName().getContents().toString());
                if(args[1] instanceof String){
                    checkedText = (String) args[1];
                }else {
                    MutableComponent contents = (MutableComponent) args[1];
                    if (contents.getContents() instanceof #PlainTextContents# lc){
                        checkedText = lc.text();
                    } else{
                        checkedText = contents.getContents().toString();
                    }
                }
            }
        } else {
            checkedText = text.getContents().toString();
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
            if (style.getHoverEvent() != null) {
                ChatImageCode action = style.getHoverEvent().getValue(ChatImageStyle.SHOW_IMAGE);
                if (action != null) action.retry();
            }
            try {
                Component showText = style.getHoverEvent() == null ? null : style.getHoverEvent().getValue(SHOW_TEXT);
                if (showText != null &&
                        showText.getContents() instanceof #PlainTextContents#) {
                    originText.setStyle(
                            style.withHoverEvent(new HoverEvent(
                                    ChatImageStyle.SHOW_IMAGE,
                                    createBuilder()
                                            .fromCode((
                                                    (#PlainTextContents#)showText.getContents()).text())
                                    .setIsSelf(isSelf)
                                    .build())));
                }
            } catch (Exception e){
                // LOGGER.error(e.getMessage());
            }
            return originText;
        }
        MutableComponent res = Component.literal("");
        ChatImageCodeTool.buildMsg(texts,
                (obj) -> res.append(Component.literal(obj).setStyle(style)),
                (obj) -> res.append(ChatImageStyle.messageFromCode(obj))
        );
        if (player == null) {
            return res;
        } else {
            return Component.translatable(key, player, res).setStyle(style);
        }
    }

    @Unique
    private Component chatimage$replaceMessage(Component message) {
        try {
            MutableComponent res = (MutableComponent) chatimage$replaceCode(message);
            for (Component t : message.getSiblings()) {
                res.append(chatimage$replaceMessage(t));
            }
            return res;
        } catch (Exception e) {
            LOGGER.warn("识别失败:"+e.getMessage());
            return message;
        }
    }
}