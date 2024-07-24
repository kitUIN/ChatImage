package io.github.kituin.chatimage.integration;

import io.github.kituin.actionlib.AlPlugin;
import io.github.kituin.actionlib.IActionRegisterApi;
import net.minecraft.network.chat.HoverEvent;

import java.util.List;

import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;

@AlPlugin
public class ActionLibIntegration implements IActionRegisterApi {
    @Override
    public List<HoverEvent.Action> registerHoverEventAction() {
        return List.of(SHOW_IMAGE);
    }
}