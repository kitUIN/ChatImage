package github.kituin.chatimage;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyBinding gatherManaKeyMapping;

    public static void init() {
        gatherManaKeyMapping = new KeyBinding(
                "config.chatimage.key",
                KeyConflictContext.IN_GAME,
                KeyModifier.NONE,
                InputMappings.Type.KEYSYM,
                GLFW.GLFW_KEY_END,
                "config.chatimage.category");
        ClientRegistry.registerKeyBinding(gatherManaKeyMapping);
    }

}
