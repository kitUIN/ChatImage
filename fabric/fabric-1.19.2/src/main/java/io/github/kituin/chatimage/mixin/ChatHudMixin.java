package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.tool.ChatImageStyle;
import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;


/**
 * 注入修改文本显示,自动将CICode转换为可鼠标悬浮格式文字
 *
 * @author kitUIN
 */
@Mixin(ChatHud.class)
public class ChatHudMixin extends DrawableHelper {
    @ModifyVariable(at = @At("HEAD"),
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            argsOnly = true)
    public Text addMessage(Text message) {
        return replaceMessage(message);
    }


    @Unique
    private static Text replaceCode(Text text) {
        String checkedText = "";
        String key = "";
        MutableText player = null;
        boolean isSelf = false;
        if (text.getContent() instanceof LiteralTextContent) {
            checkedText = ((LiteralTextContent) text.getContent()).string();
        } else if (text.getContent() instanceof TranslatableTextContent ttc) {
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if (ChatImageCodeTool.checkKey(key)) {
                player = (MutableText) args[0];
                isSelf = player.getContent().toString().equals(MinecraftClient.getInstance().player.getName().getContent().toString());
                MutableText contents = (MutableText) args[1];
                if (contents.getContent() instanceof LiteralTextContent) {
                    checkedText = ((LiteralTextContent) contents.getContent()).string();
                } else {
                    checkedText = contents.getContent().toString();
                }
            }
        } else {
            checkedText = text.getContent().toString();
        }
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
            MutableText t = text.copy();
            t.getSiblings().clear();
            return t.setStyle(style);
        }
        MutableText res = Text.literal("");
        ChatImageCodeTool.buildMsg(texts,
                (obj) -> res.append(Text.literal(obj).setStyle(style)),
                (obj) -> res.append(ChatImageStyle.messageFromCode(obj))
        );
        if (player == null) {
            return res;
        } else {
            return MutableText.of(new TranslatableTextContent(key, player, res)).setStyle(style);
        }
    }

    @Unique
    private static Text replaceMessage(Text message) {
        try{
            MutableText res = (MutableText) replaceCode(message);
            for (Text t : message.getSiblings()) {
                res.append(replaceMessage(t));
            }
            return res;
        }
        catch (Exception e){
            return message;
        }
    }
}

