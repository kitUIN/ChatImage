package github.kituin.chatimage.tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import github.kituin.chatimage.Exceptions.InvalidChatImageCodeException;
import github.kituin.chatimage.Exceptions.InvalidChatImageUrlException;
import net.minecraft.text.*;
import net.minecraft.util.JsonHelper;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatImageTool {
    public static final Pattern codePattern2 = Pattern.compile("(\\[CICode,(.*?)\\])");
    public static final String DEFAULT_CHAT_IMAGE_SHOW_NAME = Text.translatable("codename.chatimage.default").getString();

    public static final HoverEvent.Action<ChatImageCode> SHOW_IMAGE = new HoverEvent.Action<>("show_chatimage", true, ChatImageTool::fromJson, ChatImageTool::toJson, ChatImageTool::fromJson);


    public static Text replaceCode(Text text) {
        String checkedText = "";
        String key = "";
        MutableText player = null;
        if (text.getContent() instanceof LiteralTextContent) {
            checkedText = ((LiteralTextContent) text.getContent()).string();
        } else if (text.getContent() instanceof TranslatableTextContent) {
            TranslatableTextContent ttc = (TranslatableTextContent) text.getContent();
            key = ttc.getKey();
            if ("chat.type.text".equals(key)) {
                Text[] args = (Text[]) ttc.getArgs();
                player = (MutableText)args[0];
                MutableText contents = (MutableText)args[1];
                if (contents.getContent() instanceof LiteralTextContent) {
                    checkedText = ((LiteralTextContent) contents.getContent()).string();

                } else {
                    checkedText = contents.getContent().toString();
                }
            }

        } else {
            checkedText = text.getContent().toString();
        }
        Style style = text.getStyle();
        List<ChatImageCode> chatImageCodeList = Lists.newArrayList();
        Matcher m = codePattern2.matcher(checkedText);
        List<Integer> nums = Lists.newArrayList();
        boolean flag = true;
        while (m.find()) {
            try {
                ChatImageCode image = ChatImageCode.of(m.group());
                flag = false;
                nums.add(m.start());
                nums.add(m.end());
                chatImageCodeList.add(image);
            } catch (InvalidChatImageCodeException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        }
        if (flag) {
            MutableText t = MutableText.of(text.getContent());
            t = t.setStyle(text.getStyle());

            return t;
        }
        int lastPosition = 0;
        int j = 0;
        MutableText res;
        if (nums.get(0) != 0) {
            res = ((MutableText) Text.of(checkedText.substring(lastPosition, nums.get(0)))).setStyle(style);
        } else {
            res = ChatImageStyle.messageFromCode(chatImageCodeList.get(0));
            j = 2;

        }
        for (int i = j; i < nums.size(); i += 2) {
            res.append(ChatImageStyle.messageFromCode(chatImageCodeList.get(i / 2)));
            lastPosition = nums.get(i + 1);
            if (i + 2 < nums.size() && lastPosition + 1 != nums.get(i + 2)) {
                String s = checkedText.substring(lastPosition, nums.get(i + 2));
                res.append(((MutableText) Text.of(s)).setStyle(style));
            } else if (lastPosition == nums.get(nums.size() - 1) && lastPosition != checkedText.length()) {
                res.append(Text.of(checkedText.substring(lastPosition)));
            }
        }
        if(player ==null)
        {
            return res;
        }else {
            return MutableText.of(new TranslatableTextContent(key,player,res));
        }


    }

    public static Text replaceMessage(Text message) {
        System.out.println(message);
        MutableText res = (MutableText) replaceCode(message);
        for (Text t : message.getSiblings()) {
            res.append(replaceMessage(t));
        }
        return res;
    }

    public static ChatImageCode fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        String code = JsonHelper.getString(jsonObject, "code");
        try {
            return new ChatImageCode(code);
        } catch (InvalidChatImageUrlException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonElement toJson(ChatImageCode code) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code.toString());
        return jsonObject;
    }

    public static ChatImageCode fromJson(Text text) {
        try {
            return new ChatImageCode(text.toString());
        } catch (InvalidChatImageUrlException e) {
            throw new RuntimeException(e);
        }
    }
}
