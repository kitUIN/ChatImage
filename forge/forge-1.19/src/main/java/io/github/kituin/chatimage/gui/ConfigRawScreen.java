package io.github.kituin.chatimage.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.kituin.chatimage.widget.SettingSliderWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;


public abstract class ConfigRawScreen extends Screen {
    protected Screen parent;

    protected ConfigRawScreen(Component title, Screen screen) {
        super(title);
        this.parent = screen;
    }

    public void render(PoseStack p_96249_, int p_96250_, int p_96251_, float p_96252_) {
        renderBackground(p_96249_);
        drawCenteredString(p_96249_, this.font,
                title, this.width / 2, this.height / 4 - 16, 16764108);
        super.render(p_96249_, p_96250_, p_96251_, p_96252_);
    }

    protected SettingSliderWidget.OnTooltip createSliderTooltip(Component text) {
        return new SettingSliderWidget.OnTooltip() {
            public void onTooltip(SettingSliderWidget p_93753_, PoseStack p_93754_, int p_93755_, int p_93756_) {
                if (text != null) {
                    ConfigRawScreen.this.renderTooltip(p_93754_, ConfigRawScreen.this.font.split(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), p_93755_, p_93756_);
                }
            }

            public void narrateTooltip(Consumer<Component> p_168842_) {
                p_168842_.accept(text);
            }
        };
    }

    protected Button.OnTooltip createButtonTooltip(Component text) {
        return new Button.OnTooltip() {
            public void onTooltip(Button p_93753_, PoseStack p_93754_, int p_93755_, int p_93756_) {
                if (text != null) {
                    ConfigRawScreen.this.renderTooltip(p_93754_, ConfigRawScreen.this.font.split(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), p_93755_, p_93756_);
                }
            }

            public void narrateTooltip(Consumer<Component> p_168842_) {
                p_168842_.accept(text);
            }
        };
    }
}
