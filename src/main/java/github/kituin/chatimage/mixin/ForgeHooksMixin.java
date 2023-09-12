package github.kituin.chatimage.mixin;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeHooks.class)
public abstract class ForgeHooksMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static ITextComponent newChatWithLinks(String string){
        return new StringTextComponent(string);
    }

}
