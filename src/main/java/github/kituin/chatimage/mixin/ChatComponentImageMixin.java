package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;

import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static net.minecraft.client.gui.DrawableHelper.fill;

import github.kituin.chatimage.tools.ChatImageView;


/**
 * @author kitUIN
 */
@Mixin(ChatHud.class)
public abstract class ChatComponentImageMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow protected abstract boolean isChatHidden();

    @Shadow public abstract int getVisibleLineCount();

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;

    @Shadow protected abstract boolean isChatFocused();

    @Shadow public abstract double getChatScale();

    @Shadow public abstract int getWidth();

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract int getMessageIndex(double chatLineX, double chatLineY);

    @Shadow protected abstract double toChatLineX(double x);

    @Shadow protected abstract double toChatLineY(double y);

    @Shadow protected abstract int getLineHeight();

    @Shadow private int scrolledLines;


    @Shadow protected abstract int getIndicatorX(ChatHudLine.Visible line);

    @Shadow protected abstract void drawIndicatorIcon(MatrixStack matrices, int x, int y, MessageIndicator.Icon icon);

    @Shadow private boolean hasUnreadNewMessages;

    @Shadow @Final private List<ChatHudLine> messages;

    private HashMap<String, ChatImageView> chatImageCache = new HashMap<>();
    /**
     * @author kitUIN
     * @reason
     */
    @Overwrite
    public void render(MatrixStack matrices, int currentTick, int mouseX, int mouseY) {
        // 聊天栏是否隐藏
        if (!this.isChatHidden()) {
            // 所有消息
            int i = this.getVisibleLineCount();
            int j = this.visibleMessages.size();
            if (j > 0) {
                // 顶层是否是聊天栏
                boolean bl = this.isChatFocused();
                float f = (float)this.getChatScale();

                int k = MathHelper.ceil((float)this.getWidth() / f);
                int l = this.client.getWindow().getScaledHeight();
                matrices.push();
                matrices.scale(f, f, 1.0F);
                matrices.translate(4.0F, 0.0F, 0.0F);
                int m = MathHelper.floor((float)(l - 40) / f);
                int n = this.getMessageIndex(this.toChatLineX(mouseX), this.toChatLineY(mouseY));
                double d = this.client.options.getChatOpacity().getValue() * 0.9 + 0.1;
                double e = this.client.options.getTextBackgroundOpacity().getValue();
                double g = this.client.options.getChatLineSpacing().getValue();
                int o = this.getLineHeight();
                int p = (int)Math.round(-8.0 * (g + 1.0) + 4.0 * g);
                int q = 0;
                int t;
                int u;
                int v;
                int x;
                int extraHeight  = 0;
                for(int r = 0; r + this.scrolledLines < this.visibleMessages.size() && r < i; ++r) {
                    int s = r + this.scrolledLines;
                    ChatHudLine.Visible visible;
                    ChatHudLine chatLine;
                    if (s < this.visibleMessages.size() && s < this.messages.size()) {
                        visible = this.visibleMessages.get(s);
                        chatLine = this.messages.get(s);

                    } else {
                        continue;
                    }
                    if (visible != null) {
                        t = currentTick - visible.addedTime();
                        // 是否被隐藏了
                        if (t < 200 || bl) {
                            u = (int) (255.0 * d);
                            v = (int) (255.0 * e);
                            ++q;
                            if (u > 3) {
                                matrices.push();
                                boolean imageFlag = false;
                                String imageMessage = chatLine.content().getString();
                                String preMessages = "";
                                ChatImageView chatImageView = null;
                                Identifier id = null;
                                if(imageMessage.contains("[chatimage,url=") )
                                {
                                    String[] rawMessages = imageMessage.split("\\[chatimage,url=");
                                    System.out.println(rawMessages[1].substring(rawMessages[1].length()-1));
                                    if(rawMessages.length >= 2 && rawMessages[1].substring(rawMessages[1].length()-1).equals("]"))
                                    {
                                        preMessages = rawMessages[0] + "[图片]";
                                        String key = rawMessages[1].substring(0,rawMessages[1].length()-1).replace(" ", "");
                                        LOGGER.info(key);
                                        if(!chatImageCache.containsKey(key))
                                        {
                                            chatImageView = new ChatImageView(key);
                                        }
                                        else
                                        {
                                            chatImageView = chatImageCache.get(key);
                                        }
                                        try {
                                            id = chatImageView.getTexture();
                                            imageFlag = true;
                                            if(!chatImageCache.containsKey(key))
                                            {
                                                chatImageCache.put(key,chatImageView);
                                            }

                                        } catch (IOException ex) {
                                            String errorMessage = chatImageView.getMessage();
                                            if(errorMessage != "")
                                            {
                                                LOGGER.error(errorMessage);
                                            }
                                            imageFlag = false;
                                        }

                                    }


                                }
                                if(imageFlag) {
                                    int width = chatImageView.getWidth();
                                    int height = chatImageView.getHeight();
                                    BigDecimal b = new BigDecimal((float)height/width);
                                    double hx = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
                                    // 图片大于 消息框一半尺寸 时候 自适应
                                    if(chatImageView.getWidth() > k / 2 )
                                    {
                                        width = k / 2;
                                        height = (int) (width * hx);
                                    }

                                    extraHeight += height;
                                    q += MathHelper.floor((double) height / this.getLineHeight());
                                    x = m - r * o - extraHeight;
                                    int y = x + p;
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    fill(matrices, -4, x - o, k + 4 + 4, x, v << 24);
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    fill(matrices, -4, x, k + 4 + 4, x + height, v << 24);
                                    RenderSystem.enableBlend();
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    this.client.textRenderer.drawWithShadow(matrices, Text.of(preMessages).asOrderedText(), 0.0F, (float) y, 16777215 + (u << 24));
                                    RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                                    RenderSystem.setShaderTexture(0, id);
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    Screen.drawTexture(matrices, -4, x, 0, 0, width, height, width, height);
                                }
                                else{
                                    x = m - r * o - extraHeight;
                                    int y = x + p;
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    fill(matrices, -4, x - o, k + 4 + 4, x, v << 24);
                                    MessageIndicator messageIndicator = visible.indicator();
                                    if (messageIndicator != null) {
                                        int z = messageIndicator.indicatorColor() | u << 24;
                                        fill(matrices, -4, x - o, -2, x, z);
                                        if (s == n && messageIndicator.icon() != null) {
                                            int aa = this.getIndicatorX(visible);
                                            Objects.requireNonNull(this.client.textRenderer);
                                            int ab = y + 9;
                                            this.drawIndicatorIcon(matrices, aa, ab, messageIndicator.icon());
                                        }
                                    }
                                    RenderSystem.enableBlend();
                                    matrices.translate(0.0F, 0.0F, 50.0F);
                                    this.client.textRenderer.drawWithShadow(matrices, visible.content(), 0.0F, (float) y, 16777215 + (u << 24));
                                }
                                RenderSystem.disableBlend();
                                matrices.pop();

                            }
                        }
                    }
                }
                // 暂时未知
                long ac = this.client.getMessageHandler().getUnprocessedMessageCount();
                int ad;
                if (ac > 0L) {
                    ad = (int)(128.0 * d);
                    t = (int)(255.0 * e);
                    matrices.push();
                    matrices.translate(0.0F, (float)m, 50.0F);
                    fill(matrices, -2, 0, k + 4, 9, t << 24);
                    RenderSystem.enableBlend();
                    matrices.translate(0.0F, 0.0F, 50.0F);
                    Text text = Text.translatable("chat.queue", ac);
                    System.out.println("getUnprocessedMessageCount"+text.getString());
                    this.client.textRenderer.drawWithShadow(matrices, text, 0.0F, 1.0F, 16777215 + (ad << 24));
                    matrices.pop();
                    RenderSystem.disableBlend();
                }
                //
                if (bl) {
                    ad = this.getLineHeight();
                    t = j * ad;
                    int ae = q * ad;
                    int af = this.scrolledLines * ae / j - m;
                    u = ae * ae / t;
                    if (t != ae) {
                        v = af > 0 ? 170 : 96;
                        int w = this.hasUnreadNewMessages ? 13382451 : 3355562;
                        x = k + 4;
                        fill(matrices, x, -af, x + 2, -af - u, w + (v << 24));
                        fill(matrices, x + 2, -af, x + 1, -af - u, 13421772 + (v << 24));
                    }
                }

                matrices.pop();
            }
        }
    }
}
