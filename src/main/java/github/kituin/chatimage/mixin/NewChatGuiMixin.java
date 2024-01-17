package github.kituin.chatimage.mixin;

import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.exception.InvalidChatImageCodeException;
import com.google.common.collect.Lists;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Minecraft mc;
    private static final Pattern pattern = Pattern.compile("(\\[\\[CICode,(.*?)\\]\\])");
    private static final Pattern cqPattern = Pattern.compile("\\[CQ:image,(.*?)\\]");

    @ModifyVariable(at = @At("HEAD"),
            method = "func_238493_a_(Lnet/minecraft/util/text/ITextComponent;IIZ)V",
            argsOnly = true)
    public ITextComponent func_238493_a_(ITextComponent p_238493_1_) {
        return replaceMessage(p_238493_1_);
    }


    private ITextComponent replaceCode(ITextComponent text) {
        String checkedText = "";
        String key = "";
        StringTextComponent player = null;
        boolean isSelf = false;
        boolean isIncoming = false;
        if (text instanceof StringTextComponent) {
            checkedText = ((StringTextComponent) text).getText();
        } else if (text instanceof TranslationTextComponent) {
            TranslationTextComponent ttc =  (TranslationTextComponent) text;
            key = ttc.getKey();
            if ("chat.type.text".equals(key) || "chat.type.announcement".equals(key) || "commands.message.display.incoming".equals(key) || "commands.message.display.outgoing".equals(key)) {
                Object[] args = ttc.getFormatArgs();
                player = (StringTextComponent) args[0];
                isSelf = player.equals(this.mc.player.getName());
                if (args[1] instanceof StringTextComponent) {
                    checkedText = ((StringTextComponent) args[1]).getText();
                } else {
                    checkedText =  args[1].toString();
                }
                if ("commands.message.display.incoming".equals(key) || "commands.message.display.outgoing".equals(key)) {
                    isIncoming = true;
                }
            }
        } else {
            checkedText = text.getString();
        }
        if(CONFIG.cqCode){
            Matcher cqm = cqPattern.matcher(checkedText);
            while (cqm.find()) {
                String[] cqArgs = cqm.group(1).split(",");
                String cq_Url = "";
                for(int i=0;i<cqArgs.length;i++){
                    String[] cqParams = cqArgs[i].split("=");
                    if("url".equals(cqParams[0])){
                        cq_Url = cqParams[1];
                        break;
                    }
                }
                if(!cq_Url.isEmpty()){
                    checkedText = checkedText.replace(cqm.group(0), String.format("[[CICode,url=%s]]", cq_Url));
                }
            }
        }
        Style style = text.getStyle();
        List<ChatImageCode> chatImageCodeList = Lists.newArrayList();
        Matcher m = pattern.matcher(checkedText);
        List<Integer> nums = Lists.newArrayList();
        boolean flag = true;
        while (m.find()) {
            try {
                ChatImageCode image = ChatImageCode.of(m.group(), isSelf);
                flag = false;
                nums.add(m.start());
                nums.add(m.end());
                chatImageCodeList.add(image);
            } catch (InvalidChatImageCodeException e) {
                LOGGER.error(e.getMessage());
            }
        }
        if (flag) {
            ITextComponent res = text.copyRaw();
            res.getSiblings().clear();
            return res;
        }
        int lastPosition = 0;
        int j = 0;
        IFormattableTextComponent res;
        res =   new StringTextComponent(checkedText.substring(lastPosition, nums.get(0))).setStyle(style);
        if (nums.get(0) == 0) {
            res.appendSibling(ChatImageStyle.messageFromCode(chatImageCodeList.get(0)));
            j = 2;
        }
        for (int i = j; i < nums.size(); i += 2) {
            if (i == j && j == 2) {
                res.appendSibling(new StringTextComponent(checkedText.substring(nums.get(1), nums.get(2))));
            }
            res.appendSibling(ChatImageStyle.messageFromCode(chatImageCodeList.get(i / 2)));
            lastPosition = nums.get(i + 1);
            if (i + 2 < nums.size() && lastPosition + 1 != nums.get(i + 2)) {
                String s = checkedText.substring(lastPosition, nums.get(i + 2));
                res.appendSibling(new StringTextComponent(s).setStyle(style));
            } else if (lastPosition == nums.get(nums.size() - 1)) {
                res.appendSibling(new StringTextComponent(checkedText.substring(lastPosition)));
            }
        }
        if (player == null) {
            return res;
        } else {
            TranslationTextComponent resp = new TranslationTextComponent(key, player, res);
            if (isIncoming) {
                return resp.setStyle(Style.EMPTY.setFormatting(TextFormatting.GRAY).setItalic(true));
            } else {
                return resp;
            }
        }
    }

    private ITextComponent replaceMessage(ITextComponent message) {
        try {
            ITextComponent res = replaceCode(message);
            for (ITextComponent t : message.getSiblings()) {
                ((IFormattableTextComponent)res).appendSibling(replaceMessage(t));
            }
            return res;
        } catch (Exception e) {
            return message;
        }
    }
}

