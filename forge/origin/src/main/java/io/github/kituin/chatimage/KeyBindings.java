package io.github.kituin.chatimage;

import com.mojang.blaze3d.platform.InputConstants;
// IF forge-1.16.5
//import net.minecraft.client.settings.KeyBinding;
// ELSE
//import net.minecraft.client.KeyMapping;
// END IF
// IF forge-1.16.5
//import net.minecraftforge.fml.client.registry.ClientRegistry;
// ELSE IF forge-1.18.2
//import net.minecraftforge.client.ClientRegistry;
// ELSE
//import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
// END IF
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
// IF forge-1.16.5
//    public static KeyBinding gatherManaKeyMapping;
// ELSE
//    public static KeyMapping gatherManaKeyMapping;
// END IF


    public static void init(
// IF > forge-1.18.2
//            RegisterKeyMappingsEvent event
// END IF
    ) {
// IF forge-1.16.5
// ELSE
//        gatherManaKeyMapping = new KeyMapping(
//                "config.chatimage.key",
//                InputConstants.Type.KEYSYM,
//                GLFW.GLFW_KEY_END,
//                "config.chatimage.category");
// END IF

// IF < forge-1.19
//        ClientRegistry.registerKeyBinding(gatherManaKeyMapping);
// ELSE
//        event.register(gatherManaKeyMapping);
// END IF
    }

}