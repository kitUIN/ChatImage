package github.kituin.chatimage.mixin;

import com.mojang.logging.LogUtils;
import github.kituin.chatimage.exception.InvalidChatImageCodeException;
import github.kituin.chatimage.tool.ChatImageCode;
import github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Mixin;
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
@Mixin(ChatHud.class)
public class ChatHudMixin extends DrawableHelper {
    private static Pattern pattern = Pattern.compile("(\\[CICode,(.*?)\\])");

    @ModifyVariable(at = @At("HEAD"),
            method = "addMessage(Lnet/minecraft/text/Text;IIZ)V",
            argsOnly = true)
    public Text addMessage(Text message) {
        return replaceMessage(message);
    }


    private static Text replaceCode(Text text) {
        System.out.println(text);
        String checkedText;
        String key = "";
        MutableText player = null;
        boolean isSelf = false;
        boolean isIncoming = false;
        if (text instanceof TranslatableText ttc) {
            System.out.println("000");
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if ("chat.type.text".equals(key) || "chat.type.announcement".equals(key) || "commands.message.display.incoming".equals(key)||"commands.message.display.outgoing".equals(key)) {
                player = (LiteralText) args[0];
                isSelf = player.asString().equals(MinecraftClient.getInstance().player.getName().asString());
                if ("commands.message.display.incoming".equals(key)) {
                    isIncoming = true;
                }
            }
            System.out.println("11111");
            if (args[1] instanceof String content) {
                checkedText = content;
            } else {
                MutableText contents = (MutableText) args[1];
                checkedText = contents.asString();
            }
        } else {
            checkedText = text.asString();
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
        System.out.println("222");
        if (flag) {
            return text;
        }
        int lastPosition = 0;
        int j = 0;
        MutableText res;
        if (nums.get(0) != 0) {
            res = ((MutableText) Text.of(checkedText.substring(lastPosition, nums.get(0)))).setStyle(style);
        } else {
            res = ((MutableText) Text.of(checkedText.substring(lastPosition, nums.get(0)))).setStyle(style);
            res.append(ChatImageStyle.messageFromCode(chatImageCodeList.get(0)));
            j = 2;
        }
        System.out.println("3333");
        for (int i = j; i < nums.size(); i += 2) {
            if (i == j && j == 2) {
                res.append(Text.of(checkedText.substring(nums.get(1), nums.get(2))));
            }
            res.append(ChatImageStyle.messageFromCode(chatImageCodeList.get(i / 2)));
            lastPosition = nums.get(i + 1);
            if (i + 2 < nums.size() && lastPosition + 1 != nums.get(i + 2)) {
                String s = checkedText.substring(lastPosition, nums.get(i + 2));
                res.append(((MutableText) Text.of(s)).setStyle(style));
            } else if (lastPosition == nums.get(nums.size() - 1)) {
                res.append(Text.of(checkedText.substring(lastPosition)));
            }
        }
        System.out.println("4444");
        if (player != null) {
            TranslatableText resp = new TranslatableText(key, player, res);
            System.out.println("5555");
            if (isIncoming) {
                return resp.setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true));
            }
            return resp;
        } else {
            return res;
        }
    }

    private static Text replaceMessage(Text message) {
        MutableText res = (MutableText) replaceCode(message);
        for (Text t : message.getSiblings()) {
            res.append(replaceMessage(t));
        }
        return res;
    }
}

