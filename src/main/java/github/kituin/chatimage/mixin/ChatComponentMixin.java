package github.kituin.chatimage.mixin;

import github.kituin.chatimage.tool.ChatImageStyle;
import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.*;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

import static github.kituin.chatimage.ChatImage.CONFIG;


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
            method = "addMessage(Lnet/minecraft/network/chat/Component;IIZ)V",
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
        if (text instanceof TextComponent lc) {
            checkedText = lc.getContents();
        } else if (text instanceof TranslatableComponent ttc) {
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if (ChatImageCodeTool.checkKey(key)) {
                player = (MutableComponent) args[0];
                isSelf = player.getContents().equals(this.minecraft.player.getName().getContents());
                if (args[1] instanceof TextComponent lc) {
                    checkedText = lc.getContents();
                } else {
                    checkedText = args[1].toString();
                }
            }
        } else {
            checkedText = text.getContents();
        }
        LOGGER.info(checkedText);
        // 尝试解析CQ码
        if (CONFIG.cqCode) checkedText = ChatImageCodeTool.checkCQCode(checkedText);

        Style style = text.getStyle();
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
            MutableComponent res = text.copy();
            res.getSiblings().clear();
            return res;
        }
        MutableComponent res = new TextComponent("");
        ChatImageCodeTool.buildMsg(texts,
                (obj) -> res.append(new TextComponent(obj).setStyle(style)),
                (obj) -> res.append(ChatImageStyle.messageFromCode(obj))
        );
        if (player == null) {
            return res;
        } else {
            return new TranslatableComponent(key, player, res).setStyle(style);
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
            return message;
        }
    }
}

