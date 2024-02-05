package github.kituin.chatimage.mixin;

import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.util.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@Mixin(NewChatGui.class)
public class NewChatGuiMixin extends AbstractGui {

    private static final Logger LOGGER = LogManager.getLogger();

    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyVariable(at = @At("HEAD"),
            method = "addMessage(Lnet/minecraft/util/text/ITextComponent;IIZ)V",
            argsOnly = true)
    public ITextComponent func_238493_a_(ITextComponent p_238493_1_) {
        return chatImage$replaceMessage(p_238493_1_);
    }


    @Unique
    private ITextComponent chatImage$replaceCode(ITextComponent text) {
        String checkedText = "";
        String key = "";
        StringTextComponent player = null;
        boolean isSelf = false;
        if (text instanceof StringTextComponent) {
            checkedText = ((StringTextComponent) text).getText();
        } else if (text instanceof TranslationTextComponent) {
            TranslationTextComponent ttc = (TranslationTextComponent) text;
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if (ChatImageCodeTool.checkKey(key)) {
                player = (StringTextComponent) args[0];
                isSelf = player.getContents().equals(this.minecraft.player.getName().getContents());
                if (args[1] instanceof StringTextComponent) {
                    checkedText = ((StringTextComponent) args[1]).getText();
                } else {
                    checkedText = args[1].toString();
                }
            }
        } else {
            checkedText = text.getString();
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
            IFormattableTextComponent res = text.copy();
            res.getSiblings().clear();
            return res;
        }
        IFormattableTextComponent res = new StringTextComponent("");
        ChatImageCodeTool.buildMsg(texts,
                (obj) -> res.append(new StringTextComponent(obj).setStyle(style)),
                (obj) -> res.append(ChatImageStyle.messageFromCode(obj))
        );
        if (player == null) {
            return res;
        } else {
            return new TranslationTextComponent(key, player, res).setStyle(style);
        }
    }
    @Unique
    private ITextComponent chatImage$replaceMessage(ITextComponent message) {
        try {
            ITextComponent res = chatImage$replaceCode(message);
            for (ITextComponent t : message.getSiblings()) {
                ((IFormattableTextComponent)res).append(chatImage$replaceMessage(t));
            }
            return res;
        } catch (Exception e) {
            return message;
        }
    }
}

