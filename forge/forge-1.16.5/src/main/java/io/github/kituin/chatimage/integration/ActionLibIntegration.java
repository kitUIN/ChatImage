package io.github.kituin.chatimage.integration;

import com.google.common.collect.Lists;
import io.github.kituin.actionlib.AlPlugin;
import io.github.kituin.actionlib.IActionRegisterApi;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.util.text.event.HoverEvent;

import java.util.List;

@AlPlugin
public class ActionLibIntegration implements IActionRegisterApi {
    @Override
    public List<HoverEvent.Action> registerHoverEventAction() {
        return Lists.newArrayList(ChatImageStyle.SHOW_IMAGE);
    }
}
