package io.github.kituin.chatimage.gui;

import io.github.kituin.chatimage.widget.SettingSliderWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;


public abstract class ConfigRawScreen extends Screen {
    protected Screen parent;

    protected ConfigRawScreen(Component title, Screen screen) {
        super(title);
        this.parent = screen;
    }
    public void render(GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        renderBackground(p_281549_,p_281550_,p_282878_,p_282465_);
        super.render(p_281549_,p_281550_,p_282878_,p_282465_);
        p_281549_.drawCenteredString(this.font,
                title, this.width / 2, this.height / 4 - 16, 16764108);
    }
    protected SettingSliderWidget.OnTooltip createSliderTooltip(Component text) {
        return new SettingSliderWidget.OnTooltip() {
            public void onTooltip(SettingSliderWidget p_93753_, GuiGraphics p_283427_, int p_281447_, int p_282852_) {
                if (text != null) {
                    p_283427_.renderTooltip(ConfigRawScreen.this.font, ConfigRawScreen.this.font.split(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), p_281447_, p_282852_);
                }
            }

            public void narrateTooltip(Consumer<Component> p_168842_) {
                p_168842_.accept(text);
            }
        };
    }
}
