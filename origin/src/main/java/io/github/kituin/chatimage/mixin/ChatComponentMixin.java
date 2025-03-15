package io.github.kituin.chatimage.mixin;

import com.google.common.collect.Lists;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;
import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.createBuilder;
import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;

// IF >= fabric-1.16.5
// import net.minecraft.client.MinecraftClient;
// import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
// ELSE IF >= forge-1.16.5 || > neoforge-1.20.1
// import net.minecraft.client.Minecraft;
// import static io.github.kituin.chatimage.ChatImage.CONFIG;
// END IF


/**
 * 注入修改文本显示,自动将CICode转换为可鼠标悬浮格式文字
 *
 * @author kitUIN
 */
@Mixin(#ChatComponent#.class)
public class #kituin$ChatComponentMixinClass# {
    @Shadow
    @Final
// IF >= fabric-1.16.5
//    private MinecraftClient client;
// ELSE IF >= forge-1.16.5 || > neoforge-1.20.1
//   private Minecraft minecraft;
// END IF

    @ModifyVariable(at = @At("HEAD"),
            method = "#kituin$addMessageMixin#",
            argsOnly = true)
    public #Component# addMessage(#Component# message) {
        if (CONFIG.experimentalTextComponentCompatibility) {
            StringBuilder sb = new StringBuilder();
            #Component# temp = chatImage$flattenTree(message, sb,false);
            ChatImageBoolean allString = new ChatImageBoolean(true);
            ChatImageCodeTool.sliceMsg(sb.toString(), true, allString, (e) -> LOGGER.error(e.getMessage()));
            if (!allString.isValue()) message = temp;
        }
        return chatimage$replaceMessage(message);
    }
// IF >= fabric-1.19
//    @Unique
//    private #Component#Content chatImage$getContents(#Component# text){
//        return text.getContent();
//    }
// ELSE IF >= forge-1.19 || > neoforge-1.20.1
//    @Unique
//    private #Component#Contents chatImage$getContents(#Component# text){
//        return text.getContents();
//    }
// ELSE
//    @Unique
//    private #Component# chatImage$getContents(#Component# text) {
//        return text;
//    }
// END IF

    @Unique
    private String chatImage$getText(
// IF >= fabric-1.19
//          #Component#Content text
// ELSE IF >= forge-1.19 || > neoforge-1.20.1
//          #Component#Contents text
// ELSE
//             #Component# text
// END IF
    ) {
        if (text instanceof #PlainTextContents#)
// IF >= fabric-1.19
//            return ((#PlainTextContents#) text).string();
// ELSE IF >= forge-1.19 || > neoforge-1.20.1
//            return ((#PlainTextContents#) text).text();
// ELSE IF < fabric-1.19
//            return text.asString();
// ELSE
//            return ((#PlainTextContents#) text).getContents();
// END IF
        return "";
    }

    @SuppressWarnings("t")
    @Unique
    private #Component# chatimage$replaceCode(#Component# text) {
        String checkedText;
        String key = "";
        #MutableComponent# player = null;
        boolean isSelf = false;
        #MutableComponent# originText = text.copy();
        originText.getSiblings().clear();
        #Style# style = text.getStyle();
        if (chatImage$getContents(text) instanceof #PlainTextContents#) {
            checkedText = chatImage$getText((#PlainTextContents#) chatImage$getContents(text));
        } else if (chatImage$getContents(text) instanceof #TranslatableContents#) {
            #TranslatableContents# ttc = (#TranslatableContents#) chatImage$getContents(text);
            key = ttc.getKey();
            Object[] args = ttc.getArgs();
            if (ChatImageCodeTool.checkKey(key)) {
                player = (#MutableComponent#) args[0];
// IF >= fabric-1.16.5
//                isSelf = chatImage$getContents(player).toString().equals(chatImage$getContents(client.player.getName()).toString());
// ELSE IF >= forge-1.16.5 || > neoforge-1.20.1
//                isSelf = chatImage$getContents(player).toString().equals(chatImage$getContents(minecraft.player.getName()).toString());
// END IF
                if (args[1] instanceof String) {
                    checkedText = (String) args[1];
                } else {
                    #MutableComponent# contents = (#MutableComponent#) args[1];
                    if (chatImage$getContents(contents) instanceof #PlainTextContents#) {
                        checkedText = chatImage$getText((#PlainTextContents#) chatImage$getContents(contents));
                    } else {
                        checkedText = chatImage$getContents(contents).toString();
                    }
                }
            } else {
                List<#Component#> argTexts = Lists.newArrayList();
                for (Object arg : args) {
                    argTexts.add(this.chatimage$replaceMessage((#Component#) arg));
                }
                return createTranslatableComponent(key, argTexts.toArray()).setStyle(style);
            }
        } else {
            checkedText = chatImage$getContents(text).toString();
        }

        // 尝试解析CQ码
        if (CONFIG.cqCode) checkedText = ChatImageCodeTool.checkCQCode(checkedText);

        // 是否全是文本
        ChatImageBoolean allString = new ChatImageBoolean(true);

        // 尝试解析CICode
        List<Object> texts = ChatImageCodeTool.sliceMsg(checkedText, isSelf, allString, (e) -> LOGGER.error(e.getMessage()));
        // 尝试解析URL
        if (CONFIG.checkImageUri) ChatImageCodeTool.checkImageUri(texts, isSelf, allString);

        // 无识别则返回原样
        if (allString.isValue()) {
            ChatImageCode action = style.getHoverEvent() == null ? null : style.getHoverEvent().getValue(SHOW_IMAGE);
            if (action != null) action.retry();
            try {
                #Component# showText = style.getHoverEvent() == null ? null : style.getHoverEvent().getValue(#HoverEvent#.Action.SHOW_TEXT);
                if (showText != null &&
                        chatImage$getContents(showText) instanceof #PlainTextContents#) {
                    originText.setStyle(
                            style.withHoverEvent(new #HoverEvent#(
                                    SHOW_IMAGE,
                                    createBuilder()
                                            .fromCode(chatImage$getText(
                                                    (#PlainTextContents#) chatImage$getContents(showText)))
                                            .setIsSelf(isSelf)
                                            .build())));
                }
            } catch (Exception e) {
                // LOGGER.error(e.getMessage());
            }
            return originText;
        }
        #MutableComponent# res = createLiteralComponent("");
        ChatImageCodeTool.buildMsg(texts,
                (obj) -> res.append(createLiteralComponent(obj).setStyle(style)),
                (obj) -> res.append(ChatImageStyle.messageFromCode(obj))
        );
        return player == null ? res : createTranslatableComponent(key, player, res).setStyle(style);
    }

    @SuppressWarnings("t")
    @Unique
    private #Component# chatImage$flattenTree(#Component# node, StringBuilder mergedText,boolean openUrlStyle) {
        #Style# tempStyle = node.getStyle();
        if (chatImage$getContents(node) instanceof #TranslatableContents#) {
            #TranslatableContents# ttc = (#TranslatableContents#) chatImage$getContents(node);
            Object[] args = ttc.getArgs();
            List<Object> argsNew = Lists.newArrayList();
            for (Object arg : args) {
                argsNew.add(chatImage$flattenTree((#Component#) arg, mergedText,false));
            }
            return createTranslatableComponent(ttc.getKey(), argsNew.toArray()).setStyle(tempStyle);
        } else {
            String t = chatImage$getText(chatImage$getContents(node));
            mergedText.append(t);
            // 没有子就返回本身
            if (node.getSiblings().isEmpty()) return node;
            // 有子则构建
            #MutableComponent# res = null;
            List<#Component#> children = Lists.newArrayList();

            StringBuilder childSb = new StringBuilder(t);
            for (int i = 0; i < node.getSiblings().size(); i++) {
                #Component# child_ = node.getSiblings().get(i);
                #Component# child = chatImage$flattenTree(child_, mergedText,(child_.getStyle().getClickEvent() != null &&
                        child_.getStyle().getClickEvent().getAction() == #ClickEvent#.Action.OPEN_URL));
                if(child == null) continue;
                #Style# childStyle = child.getStyle();
                if (tempStyle == null) tempStyle = childStyle;
                boolean isLiteral = chatImage$getContents(child) instanceof #PlainTextContents#;
                boolean check = isLiteral &&
                        (childStyle == tempStyle || openUrlStyle || (childStyle.getClickEvent() != null &&
                                childStyle.getClickEvent().getAction() == #ClickEvent#.Action.OPEN_URL));
                if (check) {
                    childSb.append(chatImage$getText(chatImage$getContents(child)));
                }
                // 检查成功并且没有子且不是最后一个直接跳过
                if (check && child.getSiblings().isEmpty() && i != node.getSiblings().size() - 1) continue;
                // 如果父级没创建就先创建父级
                if (res == null) res = createLiteralComponent(childSb.toString()).setStyle(tempStyle);
                // 有父级则加在子里
                else children.add(createLiteralComponent(childSb.toString()).setStyle(tempStyle));
                // 没识别到直接添加
                if (!check) children.add(child);
                for (#Component# child__ : child.getSiblings()) {
                    children.add(child__);
                }
                childSb = new StringBuilder();
                tempStyle = null;
            }
            for (#Component# child : children) {
                res.append(child);
            }
            return res;
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
            LOGGER.warn("识别失败:{}", e.getMessage());
            return message;
        }
    }
}