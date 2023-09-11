package github.kituin.chatimage.mixin;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.github.chatimagecode.exception.InvalidChatImageCodeException;
import com.github.chatimagecode.ChatImageCode;
import github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 注入修改文本显示,自动将CICode转换为可鼠标悬浮格式文字
 *
 * @author kitUIN
 */
@Mixin(ChatHud.class)
public class ChatHudMixin {
    private static Pattern pattern = Pattern.compile("(\\[\\[CICode,(.*?)\\]\\])");

    @ModifyVariable(at = @At("HEAD"),
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            argsOnly = true)
    public Text addMessage(Text message) {
        return replaceMessage(message);
    }


    private static Text replaceCode(Text text) {
        String checkedText = "";
        String key = "";
        MutableText player = null;
        boolean isSelf = false;
        boolean isIncoming = false;
        if (text.getContent() instanceof LiteralTextContent) {
            checkedText = ((LiteralTextContent) text.getContent()).string();
        } else if (text.getContent() instanceof TranslatableTextContent ttc) {
            key = ttc.getKey();
            if ("chat.type.text".equals(key) || "chat.type.announcement".equals(key) || "commands.message.display.incoming".equals(key) || "commands.message.display.outgoing".equals(key)) {
                Text[] args = (Text[]) ttc.getArgs();
                player = (MutableText) args[0];
                isSelf = player.getContent().toString().equals(MinecraftClient.getInstance().player.getName().getContent().toString());
                MutableText contents = (MutableText) args[1];
                if (contents.getContent() instanceof LiteralTextContent) {
                    checkedText = ((LiteralTextContent) contents.getContent()).string();
                } else {
                    checkedText = contents.getContent().toString();
                }
                if ("commands.message.display.incoming".equals(key) || "commands.message.display.outgoing".equals(key)) {
                    isIncoming = true;
                }
            }
        } else {
            checkedText = text.getContent().toString();
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
            return MutableText.of(text.getContent()).setStyle(text.getStyle());
        }
        int lastPosition = 0;
        int j = 0;
        MutableText res;
        if (nums.get(0) != 0) {
            res = Text.literal(checkedText.substring(lastPosition, nums.get(0))).setStyle(style);
        } else {
            res = Text.literal(checkedText.substring(lastPosition, nums.get(0))).setStyle(style);
            res.append(ChatImageStyle.messageFromCode(chatImageCodeList.get(0)));
            j = 2;
        }
        for (int i = j; i < nums.size(); i += 2) {
            if (i == j && j == 2) {
                res.append(Text.literal(checkedText.substring(nums.get(1), nums.get(2))));
            }
            res.append(ChatImageStyle.messageFromCode(chatImageCodeList.get(i / 2)));
            lastPosition = nums.get(i + 1);
            if (i + 2 < nums.size() && lastPosition + 1 != nums.get(i + 2)) {
                res.append(Text.literal(checkedText.substring(lastPosition, nums.get(i + 2))).setStyle(style));
            } else if (lastPosition == nums.get(nums.size() - 1)) {
                res.append(Text.literal(checkedText.substring(lastPosition)));
            }
        }
        if (player == null) {
            return res;
        } else {
            MutableText resp =  Text.translatable(key, player, res);
            if (isIncoming) {
                return resp.setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true));
            } else {
                return resp;
            }
        }
    }

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

