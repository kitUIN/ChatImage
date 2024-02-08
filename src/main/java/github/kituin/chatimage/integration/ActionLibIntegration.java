package github.kituin.chatimage.integration;

import com.google.common.collect.Lists;
import io.github.kituin.actionlib.IActionRegisterApi;
import net.minecraft.text.HoverEvent;

import java.util.List;

import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;

public class ActionLibIntegration implements IActionRegisterApi {
    @Override
    public List<HoverEvent.Action> registerHoverEventAction() {
        return Lists.newArrayList(SHOW_IMAGE);
    }
}
