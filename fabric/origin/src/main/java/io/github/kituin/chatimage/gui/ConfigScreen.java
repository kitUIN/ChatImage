package io.github.kituin.chatimage.gui;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.widget.GifSlider;
import io.github.kituin.chatimage.widget.TimeOutSlider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
// IF != fabric-1.16.5
//import net.minecraft.client.gui.Drawable;
//import net.minecraft.client.gui.Element;
//import net.minecraft.client.gui.Selectable;
// END IF
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.*;
// IF >= fabric-1.20
//import net.minecraft.client.gui.DrawContext;
// ELSE
//import net.minecraft.client.util.math.MatrixStack;
// END IF
// IF fabric-1.16.5
//import net.minecraft.client.gui.widget.ClickableWidget;
// ELSE IF >= fabric-1.19.3
//import net.minecraft.client.gui.tooltip.Tooltip;
//import net.minecraft.client.gui.widget.GridWidget;
//import net.minecraft.client.gui.widget.SimplePositioningWidget;
// END IF
import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;

@Environment(EnvType.CLIENT)
// IF <= fabric-1.19.2
//public class ConfigScreen extends ConfigRawScreen {
//    public ConfigScreen(Screen screen) {
//        super(createTranslatableComponent("config.chatimage.category"), screen);
//    }
// ELSE
//public class ConfigScreen extends Screen {
//    private final Screen parent;
//
//    public ConfigScreen(Screen screen) {
//        super(createTranslatableComponent("config.chatimage.category"));
//        parent = screen;
//    }
// END IF

    public ConfigScreen() {
        this(null);
    }
//IF <= fabric-1.19.4
//    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        super.render(matrices, mouseX, mouseY, delta);
//        #drawCenteredTextWithShadow#(matrices, this.textRenderer, title, this.width / 2, this.height / 4 - 16, 16764108);
//    }
// ELSE
//    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
//        super.render(matrices, mouseX, mouseY, delta);
//        matrices.#drawCenteredTextWithShadow#(this.textRenderer, title, this.width / 2, this.height / 4 - 16, 16764108);
//    }
// END IF
// IF >= fabric-1.19.3
//    protected void init() {
//        super.init();
//        GridWidget gridWidget = new GridWidget();
//        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
//        GridWidget.Adder adder = gridWidget.createAdder(2);
//        adder.add(ButtonWidget.builder(getNsfw(CONFIG.nsfw), (button) -> {
//            CONFIG.nsfw = !CONFIG.nsfw;
//            button.setMessage(getNsfw(CONFIG.nsfw));
//            ChatImageConfig.saveConfig(CONFIG);
//        }).tooltip(Tooltip.of(Text.translatable("nsfw.chatimage.tooltip"))).build());
//        adder.add(new GifSlider());
//        adder.add(new TimeOutSlider());
//        adder.add(ButtonWidget.builder(Text.translatable("padding.chatimage.gui"), (button) -> {
//            if (this.client != null) {
//                setScreen(this.client, new LimitPaddingScreen(this));
//            }
//        }).tooltip(Tooltip.of(Text.translatable("padding.chatimage.tooltip"))).build());
//        adder.add(ButtonWidget.builder(getCq(CONFIG.cqCode), (button) -> {
//            CONFIG.cqCode = !CONFIG.cqCode;
//            button.setMessage(getCq(CONFIG.cqCode));
//            ChatImageConfig.saveConfig(CONFIG);
//        }).tooltip(Tooltip.of(Text.translatable("cq.chatimage.tooltip"))).build());
//        adder.add(ButtonWidget.builder(getUri(CONFIG.checkImageUri), (button) -> {
//            CONFIG.checkImageUri = !CONFIG.checkImageUri;
//            button.setMessage(getUri(CONFIG.checkImageUri));
//            ChatImageConfig.saveConfig(CONFIG);
//        }).build());
//        adder.add(ButtonWidget.builder(getDrag(CONFIG.dragUseCicode), (button) -> {
//            CONFIG.dragUseCicode = !CONFIG.dragUseCicode;
//            button.setMessage(getDrag(CONFIG.dragUseCicode));
//            ChatImageConfig.saveConfig(CONFIG);
//        }).tooltip(Tooltip.of(Text.translatable("drag.chatimage.tooltip"))).build());
//        adder.add(ButtonWidget.builder(getDragImage(CONFIG.dragImage), (button) -> {
//            CONFIG.dragImage = !CONFIG.dragImage;
//            button.setMessage(getDragImage(CONFIG.dragImage));
//            ChatImageConfig.saveConfig(CONFIG);
//        }).tooltip(Tooltip.of(Text.translatable("image.drag.chatimage.tooltip"))).build());
//        adder.add(ButtonWidget.builder(Text.translatable("gui.back"), (button) -> {
//            if (this.client != null) {
//                setScreen(this.client, this.parent);
//            }
//        }).build(), 2);
// IF > fabric-1.19.3
//        gridWidget.refreshPositions();
//        SimplePositioningWidget.setPos(gridWidget, 0, this.height / 3 - 12, this.width, this.height, 0.5F, 0.0F);
//        gridWidget.forEachChild(this::addDrawableChild);
// ELSE IF fabric-1.19.3
//        gridWidget.recalculateDimensions();
//        SimplePositioningWidget.setPos(gridWidget, 0, this.height / 3 - 12, this.width, this.height, 0.5F, 0.0F);
//        this.addDrawableChild(gridWidget);
// END IF
// ELSE
//    protected void init() {
//        super.init();
//        addDrawableWeight(new ButtonWidget(this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20, getNsfw(CONFIG.nsfw), (button) -> {
//            CONFIG.nsfw = !CONFIG.nsfw;
//            button.setMessage(getNsfw(CONFIG.nsfw));
//            ChatImageConfig.saveConfig(CONFIG);
//        }, getButtonTooltip(createTranslatableComponent("nsfw.chatimage.tooltip"))));
//        addDrawableWeight(new GifSlider(this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20, getSliderTooltip(createTranslatableComponent("gif.chatimage.tooltip"))));
//        addDrawableWeight(new TimeOutSlider(this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20, getSliderTooltip(createTranslatableComponent("timeout.chatimage.tooltip"))));
//        addDrawableWeight(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20, createTranslatableComponent("padding.chatimage.gui"), (button) -> {
//            if (this.client != null) {
//                setScreen(this.client, new LimitPaddingScreen(this));
//            }
//        }, getButtonTooltip(createTranslatableComponent("padding.chatimage.tooltip"))));
//        addDrawableWeight(new ButtonWidget(this.width / 2 - 154, this.height / 4 + 72 - 16, 150, 20, getCq(CONFIG.cqCode), (button) -> {
//            CONFIG.cqCode = !CONFIG.cqCode;
//            button.setMessage(getCq(CONFIG.cqCode));
//            ChatImageConfig.saveConfig(CONFIG);
//        }, getButtonTooltip(createTranslatableComponent("cq.chatimage.tooltip"))));
//        addDrawableWeight(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 72 - 16, 150, 20, getUri(CONFIG.checkImageUri), (button) -> {
//            CONFIG.checkImageUri = !CONFIG.checkImageUri;
//            button.setMessage(getUri(CONFIG.checkImageUri));
//            ChatImageConfig.saveConfig(CONFIG);
//        }));
//        addDrawableWeight(new ButtonWidget(this.width / 2 - 154, this.height / 4 + 96 - 16, 150, 20, getDrag(CONFIG.dragUseCicode), (button) -> {
//            CONFIG.dragUseCicode = !CONFIG.dragUseCicode;
//            button.setMessage(getDrag(CONFIG.dragUseCicode));
//            ChatImageConfig.saveConfig(CONFIG);
//        }, getButtonTooltip(createTranslatableComponent("drag.chatimage.tooltip"))));
//        addDrawableWeight(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 96 - 16, 150, 20, getDragImage(CONFIG.dragImage), (button) -> {
//            CONFIG.dragImage = !CONFIG.dragImage;
//            button.setMessage(getDragImage(CONFIG.dragImage));
//            ChatImageConfig.saveConfig(CONFIG);
//        }, getButtonTooltip(createTranslatableComponent("image.drag.chatimage.tooltip"))));
//        addDrawableWeight(new ButtonWidget(this.width / 2 - 77, this.height / 4 + 120 + -16, 150, 20, createTranslatableComponent("gui.back"), (button) -> {
//            if (this.client != null) {
//                setScreen(this.client, this.parent);
//            }
//        }));
// END IF
    }

    private MutableText getCq(boolean enable) {
        return getEnable("cq.chatimage.gui", enable);
    }

    private MutableText getNsfw(boolean enable) {
        return getEnable("nsfw.chatimage.gui", !enable);
    }

    private MutableText getDrag(boolean enable) {
        return getEnable("drag.chatimage.gui", enable);
    }
    private MutableText getDrag(boolean enable) {
        return getEnable("image.drag.chatimage.gui", enable);
    }

    private MutableText getUri(boolean enable) {
        return getEnable("uri.chatimage.gui", enable);
    }

    public static MutableText getEnable(String key, boolean enable) {
        return composeGenericOptionText(createTranslatableComponent(key), createTranslatableComponent((enable ? "open" : "close") + ".chatimage.common"));
    }

// IF fabric-1.16.5
//    public <T extends ClickableWidget> T addDrawableWeight(T element)
//    {
//        return this.addButton(element);
// ELSE
//    public <T extends Element & Drawable & Selectable> T addDrawableWeight(T element)
//    {
//        return this.addDrawableChild(element);
// END IF
    }
}