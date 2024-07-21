package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
// IF >= fabric-1.19.4
//import net.minecraft.client.gui.tooltip.Tooltip;
// END IF
import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;

public class TimeOutSlider extends SettingSliderWidget {

// IF < fabric-1.19.4
//    public TimeOutSlider(int x, int y, int width, int height, TooltipSupplier tooltipSupplier) {
//        super(x, y, width, height, CONFIG.timeout, 3, 60, tooltipSupplier);
//        this.updateMessage();
//    }
// ELSE
//public TimeOutSlider() {
//    super(100, 100, 150, 20, CONFIG.timeout, 3, 60);
//    this.updateMessage();
//    this.tip = Tooltip.of(createTranslatableText("timeout.chatimage.tooltip"));
//    this.setTooltip(this.tip);
//}
// END IF
    @Override
    protected void updateMessage() {
        this.setMessage(composeGenericOptionText(
                createTranslatableText("timeout.chatimage.gui"),
                createLiteralText(String.valueOf(this.position)))
                .append(" ")
                .append(createTranslatableText("seconds.chatimage.gui")));
        CONFIG.timeout = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }
}