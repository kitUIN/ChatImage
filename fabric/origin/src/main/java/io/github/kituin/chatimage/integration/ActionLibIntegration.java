// ONLY < fabric-1.21.5
package io.github.kituin.chatimage.integration;

import io.github.kituin.actionlib.IActionRegisterApi;
import #HoverEvent#;

import java.util.List;

import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;

public class ActionLibIntegration implements IActionRegisterApi {
    @Override
    public List<HoverEvent.Action> registerHoverEventAction() {
        return List.of(SHOW_IMAGE);
    }
}