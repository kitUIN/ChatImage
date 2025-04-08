package io.github.kituin.chatimage.mixin;

import com.google.common.collect.Lists;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import io.github.kituin.ChatImageCode.ChatImageBoolean;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeTool;
import #HoverEvent#;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import static io.github.kituin.ChatImageCode.ChatImageCode.pattern;
import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;
import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.createBuilder;

import static io.github.kituin.chatimage.tool.SimpleUtil.*;

/**
 * 注入修改文本显示,自动将CICode转换为可鼠标悬浮格式文字
 *
 * @author kitUIN
 */
@Mixin(#ChatComponent#.class)
public class #kituin$ChatComponentMixinClass# {
    @Shadow
    @Final
    private #MinecraftClient#;

    @ModifyVariable(at = @At("HEAD"),
            method = "#kituin$addMessageMixin#",
            argsOnly = true)
    public #Component# addMessage(#Component# message) {
        if (#kituin$ChatImageConfig#.experimentalTextComponentCompatibility) {
            try {
                StringBuilder sb = new StringBuilder();
                #Component# temp = chatImage$flattenTree(message, sb, false);
                ChatImageBoolean allString = new ChatImageBoolean(true);
                ChatImageCodeTool.sliceMsg(sb.toString(), true, allString, (e) -> LOGGER.error("slice msg error: {}",e));
                if (!allString.isValue()) message = temp;
            } catch (Exception e) {
                LOGGER.warn("experimentalTextComponentCompatibility 转换失败:{}", e.getMessage());
            }
        }
        return chatimage$replaceMessage(message);
    }

    // IF >= fabric-1.19
//    @Unique
//    private #Component#Content chatImage$getContents(#Component# text) {
//        return text.getContent();
//    }
// ELSE IF >= forge-1.19 || > neoforge-1.20.1
//    @Unique
//    private #Component#Contents chatImage$getContents(#Component# text) {
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
//            #Component#Content text
// ELSE IF >= forge-1.19 || > neoforge-1.20.1
//            #Component#Contents text
// ELSE
//            #Component# text
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
        if (#kituin$ChatImageConfig#.cqCode)
            checkedText = ChatImageCodeTool.checkCQCode(checkedText);

        // 是否全是文本
        ChatImageBoolean allString = new ChatImageBoolean(true);

        // 尝试解析CICode
        List<Object> texts = ChatImageCodeTool.sliceMsg(checkedText, isSelf, allString, (e) -> LOGGER.error("slice msg error: {}", e));
        // 尝试解析URL
        if (#kituin$ChatImageConfig#.checkImageUri)
            ChatImageCodeTool.checkImageUri(texts, isSelf, allString);

        // 无识别则返回原样
        if (allString.isValue()) {
// IF >= fabric-1.21.5 || >= neoforge-1.21.5
//            if (style.getHoverEvent() instanceof ChatImageStyle.ShowImage(ChatImageCode action)) action.retry();
// ELSE
//              if (style.getHoverEvent() != null && style.getHoverEvent().getValue(ChatImageStyle.SHOW_IMAGE) !=null) style.getHoverEvent().getValue(ChatImageStyle.SHOW_IMAGE).retry();
// END IF
            try {
// IF >= fabric-1.21.5 || >= neoforge-1.21.5
//                if (style.getHoverEvent() != null &&
//                        style.getHoverEvent() instanceof HoverEvent.ShowText(#Component# showText) &&
//                        chatImage$getContents(showText) instanceof #PlainTextContents#) {
// ELSE
//              #Component# showText = style.getHoverEvent() == null ? null : style.getHoverEvent().getValue(#HoverEvent#.Action.SHOW_TEXT);
//              if (showText != null &&
//                      chatImage$getContents(showText) instanceof #PlainTextContents#) {
// END IF

                    String originalCode = chatImage$getText((#PlainTextContents#) chatImage$getContents(showText));
                    Matcher matcher = pattern.matcher(originalCode);
                    if (matcher.find()) {
                    originText.setStyle(
                            style.withHoverEvent(
// IF >= fabric-1.21.5 || >= neoforge-1.21.5
//                                    new ChatImageStyle.ShowImage(
// ELSE
//              new #HoverEvent#(
//                                     ChatImageStyle.SHOW_IMAGE,
//
// END IF
                                            createBuilder()
                                                    .fromCode(originalCode)
                                                    .setIsSelf(isSelf)
                                                    .build())));
                }
                }
            } catch (Exception e) {
                LOGGER.error("返回原样失败:{}", e);
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
    private #Component# chatImage$flattenTree(Object originNode, StringBuilder mergedText, boolean openUrlStyle) {
        if (originNode instanceof String) {
            return createLiteralComponent((String) originNode);
        } else if (originNode instanceof Integer) {
            return createLiteralComponent(originNode.toString());
        }
        #Component# node = (#Component#) originNode;
        #Style# tempStyle = node.getStyle();
        if (chatImage$getContents(node) instanceof #TranslatableContents#) {
            #TranslatableContents# ttc = (#TranslatableContents#) chatImage$getContents(node);
            Object[] args = ttc.getArgs();
            List<Object> argsNew = Lists.newArrayList();
            for (Object arg : args) {
                argsNew.add(chatImage$flattenTree(arg, mergedText, false));
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
                #Component# child = chatImage$flattenTree(child_, mergedText, (child_.getStyle().getClickEvent() != null &&
// IF >= neoforge-1.21.5
//                        child_.getStyle().getClickEvent().action() == #ClickEvent#.Action.OPEN_URL));
// ELSE
//                        child_.getStyle().getClickEvent().getAction() == #ClickEvent#.Action.OPEN_URL));
// END IF
                if (child == null) continue;
                #Style# childStyle = child.getStyle();
                if (tempStyle == null) tempStyle = childStyle;
                boolean isLiteral = chatImage$getContents(child) instanceof #PlainTextContents#;
                boolean check = isLiteral &&
                        (chatImage$isSame(childStyle, tempStyle) || openUrlStyle || (childStyle.getClickEvent() != null &&
// IF >= neoforge-1.21.5
//                childStyle.getClickEvent().action() == #ClickEvent#.Action.OPEN_URL));
// ELSE
//                                childStyle.getClickEvent().getAction() == #ClickEvent#.Action.OPEN_URL));
// END IF
                if (check) {
                    childSb.append(chatImage$getText(chatImage$getContents(child)));
                    // 检查成功并且没有子且不是最后一个直接跳过
                    if (child.getSiblings().isEmpty() && i != node.getSiblings().size() - 1) continue;
                }
                // 如果父级没创建就先创建父级
                if (res == null) res = createLiteralComponent(childSb.toString()).setStyle(tempStyle);
                    // 有父级则加在子里
                else children.add(createLiteralComponent(childSb.toString()).setStyle(tempStyle));
                childSb = new StringBuilder();
                tempStyle = null;
                // 没识别到直接添加
                if (!check) children.add(child);
                // for (#Component# child__ : child.getSiblings()) {
                //     children.add(child__);
                // }
            }
            for (#Component# child : children) {
                res.append(child);
            }
            return res;
        }
    }

    @Unique
    private boolean chatImage$isSame(#Style# childStyle, #Style# tempStyle) {
        if (childStyle == null || tempStyle == null) return false;
        return childStyle.isBold() == tempStyle.isBold() &&
                Objects.equals(childStyle.getColor(), tempStyle.getColor()) &&
                childStyle.isItalic() == tempStyle.isItalic() &&
                childStyle.isObfuscated() == tempStyle.isObfuscated() &&
                childStyle.isStrikethrough() == tempStyle.isStrikethrough() &&
                childStyle.isUnderlined() == tempStyle.isUnderlined() &&
                Objects.equals(childStyle.getClickEvent(), tempStyle.getClickEvent()) &&
                Objects.equals(childStyle.getHoverEvent(), tempStyle.getHoverEvent()) &&
                Objects.equals(childStyle.getInsertion(), tempStyle.getInsertion()) &&
                Objects.equals(childStyle.getFont(), tempStyle.getFont());
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