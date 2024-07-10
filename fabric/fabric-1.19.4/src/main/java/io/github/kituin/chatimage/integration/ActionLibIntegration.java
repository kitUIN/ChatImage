package io.github.kituin.chatimage.integration;

import io.github.kituin.actionlib.ActionRegisterApi;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.text.HoverEvent;

import java.util.List;

public class ActionLibIntegration implements ActionRegisterApi {
    @Override
    public List<HoverEvent.Action> registerHoverEventAction() {
        return List.of(ChatImageStyle.SHOW_IMAGE);
    }
}
