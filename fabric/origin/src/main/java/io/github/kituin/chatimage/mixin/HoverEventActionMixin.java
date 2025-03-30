// ONLY >= fabric-1.21.5
package io.github.kituin.chatimage.mixin;


import com.mojang.serialization.MapCodec;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import #HoverEvent#;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

@Mixin(HoverEvent.Action.class)
@Unique
public abstract class HoverEventActionMixin {

    @Shadow
    @Final
    @Mutable
    private static HoverEvent.Action[] $VALUES;

    private static final HoverEvent.Action SHOW_IMAGE = chatimage$addVariant("SHOW_IMAGE", "show_chatimage", true, ChatImageStyle.ShowImage.CODEC);

    @Invoker("<init>")
    public static HoverEvent.Action chatimage$invokeInit(String internalName, int internalId, String name, boolean parsable, MapCodec<? extends HoverEvent> codec) {
        throw new AssertionError();
    }

    private static HoverEvent.Action chatimage$addVariant(String internalName, String name, boolean parsable, MapCodec<? extends HoverEvent> codec) {
        try {
            // 获取枚举类的构造函数和静态字段
            Field valuesField = HoverEvent.Action.class.getDeclaredField("$VALUES");
            valuesField.setAccessible(true);  // 设置可访问

            HoverEvent.Action[] originalValues = (HoverEvent.Action[]) valuesField.get(null);

            // 创建新的 Action
            ArrayList<HoverEvent.Action> variants = new ArrayList<>(Arrays.asList(originalValues));
            HoverEvent.Action newAction = chatimage$invokeInit(internalName, variants.size(), name, parsable, codec);

            // 添加到列表并更新 $VALUES
            variants.add(newAction);
            valuesField.set(null, variants.toArray(new HoverEvent.Action[0]));

            return newAction;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}