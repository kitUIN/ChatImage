package io.github.kituin.chatimage.integration;

import io.github.kituin.actionlib.AlPlugin;
import io.github.kituin.actionlib.IActionRegisterApi;
// IF forge-1.16.5
//import net.minecraft.util.text.event.HoverEvent;
// ELSE
//import net.minecraft.network.chat.HoverEvent;
// END IF

import java.util.List;

import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;

@AlPlugin
public class ActionLibIntegration implements IActionRegisterApi {
    @Override
    public List<HoverEvent.Action> registerHoverEventAction() {
        return List.of(SHOW_IMAGE);
    }
}