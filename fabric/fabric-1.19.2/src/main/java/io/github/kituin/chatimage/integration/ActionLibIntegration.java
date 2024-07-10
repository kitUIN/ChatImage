package io.github.kituin.chatimage.integration;

import io.github.kituin.actionlib.ActionRegisterApi;
import net.minecraft.text.HoverEvent;

import java.util.List;

import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;

public class ActionLibIntegration implements ActionRegisterApi {
    @Override
    public List<HoverEvent.Action> registerHoverEventAction() {
        return List.of(SHOW_IMAGE);
    }
}
