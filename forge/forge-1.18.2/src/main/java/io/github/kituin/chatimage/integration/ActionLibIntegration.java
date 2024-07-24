package io.github.kituin.chatimage.integration;

import io.github.kituin.actionlib.AlPlugin;
import io.github.kituin.actionlib.IActionRegisterApi;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.network.chat.HoverEvent;

import java.util.List;

@AlPlugin
public class ActionLibIntegration implements IActionRegisterApi {
    @Override
    public List<HoverEvent.Action> registerHoverEventAction() {
        return List.of(ChatImageStyle.SHOW_IMAGE);
    }
}
