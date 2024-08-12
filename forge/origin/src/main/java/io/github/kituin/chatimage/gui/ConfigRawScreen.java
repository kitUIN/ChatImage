package io.github.kituin.chatimage.gui;

import io.github.kituin.chatimage.widget.SettingSliderWidget;

import java.util.function.Consumer;


public abstract class ConfigRawScreen extends #Screen# {
    protected #Screen# parent;

    protected ConfigRawScreen(#Component# title, #Screen# screen) {
        super(title);
        this.parent = screen;
    }
    public void render(#GuiGraphics# matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
// IF <= forge-1.19
//        renderBackground(matrixStack);
//        drawCenteredString(matrixStack, this.font,
//                title, this.width / 2, this.height / 4 - 16, 16764108);
// ELSE
//        renderBackground(matrixStack, mouseX, mouseY, partialTicks);
//        matrixStack.drawCenteredString(this.font,
//                title, this.width / 2, this.height / 4 - 16, 16764108);
// END IF

    }
    protected SettingSliderWidget.OnTooltip createSliderTooltip(#Component# text) {
        return new SettingSliderWidget.OnTooltip() {
            public void onTooltip(SettingSliderWidget p_93753_, #GuiGraphics# p_283427_, int p_281447_, int p_282852_) {
                if (text != null) {
// IF <= forge-1.19
//                    ConfigRawScreen.this.renderTooltip(p_283427_, ConfigRawScreen.this.font.split(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), p_281447_, p_282852_);
// ELSE
//                    p_283427_.renderTooltip(ConfigRawScreen.this.font, ConfigRawScreen.this.font.split(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), p_281447_, p_282852_);
// END IF
                }
            }

            public void narrateTooltip(Consumer<#Component#> p_168842_) {
                p_168842_.accept(text);
            }
        };
    }
// IF <= forge-1.19
//    protected #Button.OnTooltip# createButtonTooltip(#Component# text) {
//        return new #Button.OnTooltip#() {
//            public void onTooltip(#Button# p_93753_, #GuiGraphics# p_93754_, int p_93755_, int p_93756_) {
//                if (text != null) {
//                    ConfigRawScreen.this.renderTooltip(p_93754_, ConfigRawScreen.this.font.split(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), p_93755_, p_93756_);
//                }
//            }
//
//        };
//    }
// END IF
}