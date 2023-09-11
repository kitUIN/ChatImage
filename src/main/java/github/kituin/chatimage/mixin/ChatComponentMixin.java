package github.kituin.chatimage.mixin;

import com.github.chatimagecode.ChatImageCode;
import com.github.chatimagecode.exception.InvalidChatImageCodeException;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 注入修改文本显示,自动将CICode转换为可鼠标悬浮格式文字
 *
 * @author kitUIN
 */
@Mixin(ChatComponent.class)
public class ChatComponentMixin extends GuiComponent {
    @Shadow
    @Final
    private Minecraft minecraft;
    private static final Pattern pattern = Pattern.compile("(\\[\\[CICode,(.*?)\\]\\])");

    @ModifyVariable(at = @At("HEAD"),
            method = "addMessage(Lnet/minecraft/network/chat/Component;IIZ)V",
            argsOnly = true)
    public Component addMessage(Component p_241484_) {
        return replaceMessage(p_241484_);
    }


    private Component replaceCode(Component text) {
        String checkedText = "";
        String key = "";
        TextComponent player = null;
        boolean isSelf = false;
        boolean isIncoming = false;
        if (text instanceof TextComponent lc) {
            checkedText = lc.getContents();
        } else if (text instanceof TranslatableComponent ttc) {
            key = ttc.getKey();
            if ("chat.type.text".equals(key) || "chat.type.announcement".equals(key) || "commands.message.display.incoming".equals(key) || "commands.message.display.outgoing".equals(key)) {
                Object[] args = ttc.getArgs();
                player = (TextComponent) args[0];
                isSelf = player.getContents().equals(this.minecraft.player.getName().getContents());
                if (args[1] instanceof TextComponent lc) {
                    checkedText = lc.getContents();
                } else {
                    checkedText = (String) args[1];
                }
                if ("commands.message.display.incoming".equals(key) || "commands.message.display.outgoing".equals(key)) {
                    isIncoming = true;
                }
            }
        } else {
            checkedText = text.getContents();
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
                LogUtils.getLogger().error(e.getMessage());
            }
        }
        if (flag) {
            MutableComponent res = text.copy();
            res.getSiblings().clear();
            return res;
        }
        int lastPosition = 0;
        int j = 0;
        MutableComponent res;
        res = new TextComponent(checkedText.substring(lastPosition, nums.get(0))).setStyle(style);
        if (nums.get(0) == 0) {
            res.append(ChatImageStyle.messageFromCode(chatImageCodeList.get(0)));
            j = 2;
        }
        for (int i = j; i < nums.size(); i += 2) {
            if (i == j && j == 2) {
                res.append(new TextComponent(checkedText.substring(nums.get(1), nums.get(2))));
            }
            res.append(ChatImageStyle.messageFromCode(chatImageCodeList.get(i / 2)));
            lastPosition = nums.get(i + 1);
            if (i + 2 < nums.size() && lastPosition + 1 != nums.get(i + 2)) {
                String s = checkedText.substring(lastPosition, nums.get(i + 2));
                res.append(new TextComponent(s).setStyle(style));
            } else if (lastPosition == nums.get(nums.size() - 1)) {
                res.append(new TextComponent(checkedText.substring(lastPosition)));
            }
        }
        if (player == null) {
            return res;
        } else {
            TranslatableComponent resp = new TranslatableComponent(key, player, res);
            if (isIncoming) {
                return resp.setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true));
            } else {
                return resp;
            }
        }
    }

    private Component replaceMessage(Component message) {
        try {
            MutableComponent res = (MutableComponent) replaceCode(message);
            for (Component t : message.getSiblings()) {
                res.append(replaceMessage(t));
            }
            return res;
        } catch (Exception e) {
            return message;
        }
    }
}

