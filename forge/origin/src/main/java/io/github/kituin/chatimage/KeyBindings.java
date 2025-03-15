package io.github.kituin.chatimage;

import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static #KeyMapping# gatherManaKeyMapping;

    public static void init(
// IF > forge-1.18.2
//            net.minecraftforge.client.event.RegisterKeyMappingsEvent event
// END IF
    ) {
       gatherManaKeyMapping = new #KeyMapping#(
               "config.chatimage.key",
               #InputConstants#.Type.KEYSYM,
               GLFW.GLFW_KEY_END,
               "config.chatimage.category");

// IF < forge-1.19
//        #ClientRegistry#.registerKeyBinding(gatherManaKeyMapping);
// ELSE
//        event.register(gatherManaKeyMapping);
// END IF
    }

}