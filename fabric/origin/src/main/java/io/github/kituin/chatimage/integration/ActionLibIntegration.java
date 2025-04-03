package io.github.kituin.chatimage.integration;

import io.github.kituin.actionlib.IActionRegisterApi;
import io.github.kituin.chatimage.tool.ChatImageStyle;

import java.util.List;


public class ActionLibIntegration implements IActionRegisterApi {
// IF >= fabric-1.21.5
//    @Override
//    public List<io.github.kituin.actionlib.HoverEventActionVariant> registerHoverEventAction() {
//        return List.of(new io.github.kituin.actionlib.HoverEventActionVariant("SHOW_IMAGE", "show_chatimage", true, ChatImageStyle.ShowImage.CODEC));
//    }
// ELSE
//    @Override
//    public List<#HoverEvent#.Action> registerHoverEventAction() {
//        return List.of(ChatImageStyle.SHOW_IMAGE);
//    }
// END IF
}