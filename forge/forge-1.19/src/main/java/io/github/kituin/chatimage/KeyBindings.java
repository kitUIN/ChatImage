package io.github.kituin.chatimage;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyMapping gatherManaKeyMapping;

    public static void init(RegisterKeyMappingsEvent event) {
        gatherManaKeyMapping = new KeyMapping(
                "config.chatimage.key",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_END,
                "config.chatimage.category");
        event.register(gatherManaKeyMapping);
    }

}
