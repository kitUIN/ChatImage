package io.github.kituin.chatimage.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.tool.SimpleUtil.*;

@OnlyIn(Dist.CLIENT)
public class ConfirmNsfwScreen extends #ConfirmScreen# {

    public ConfirmNsfwScreen(BooleanConsumer callback, String link) {
        this(callback, createTranslatableComponent("nsfw.chatimage.open"), createLiteralComponent(link));
    }

    public ConfirmNsfwScreen(BooleanConsumer callback, #Component# title, #Component# message) {
        super(callback, title, message);
        this.yesButton = createTranslatableComponent("gui.yes");
        this.noButton = createTranslatableComponent("gui.no");
    }

}