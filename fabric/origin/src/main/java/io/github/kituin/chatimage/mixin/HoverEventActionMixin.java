// ONLY >= fabric-1.21.5
package io.github.kituin.chatimage.mixin;


import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import #HoverEvent#;
import net.minecraft.util.StringIdentifiable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(HoverEvent.Action.class)
@Unique
public abstract class HoverEventActionMixin {

    @Shadow
    @Final
    @Mutable
    private static HoverEvent.Action[] field_55910;
    private static final HoverEvent.Action SHOW_IMAGE = chatimage$addVariant("SHOW_IMAGE", "show_chatimage", true, ChatImageStyle.ShowImage.CODEC);

    @Shadow
    @Final
    @Mutable
    public static Codec<HoverEvent.Action> UNVALIDATED_CODEC;
    @Shadow
    @Final
    @Mutable
    public static Codec<HoverEvent.Action> CODEC;


    @Invoker("<init>")
    public static HoverEvent.Action chatimage$invokeInit(String internalName, int internalId, String name, boolean parsable, MapCodec<? extends HoverEvent> codec) {
        throw new AssertionError();
    }

    @Unique
    private static HoverEvent.Action chatimage$addVariant(String internalName, String name, boolean parsable, MapCodec<? extends HoverEvent> codec) {
        ArrayList<HoverEvent.Action> variants = new ArrayList<>(Arrays.asList(HoverEventActionMixin.field_55910));
        HoverEvent.Action newAction = chatimage$invokeInit(internalName, variants.size(), name, parsable, codec);

        variants.add(newAction);
        HoverEventActionMixin.field_55910 = variants.toArray(new HoverEvent.Action[0]);
        UNVALIDATED_CODEC = StringIdentifiable.createBasicCodec(() -> HoverEventActionMixin.field_55910);
        CODEC = UNVALIDATED_CODEC.validate(HoverEventActionMixin::invokeValidate);
        return newAction;
    }

    @Invoker("validate")
    public static DataResult<HoverEvent.Action> invokeValidate(HoverEvent.Action action) {
        throw new AssertionError();
    }
}