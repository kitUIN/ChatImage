package github.kituin.chatimage.tool;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static github.kituin.chatimage.Chatimage.MOD_ID;


public class ChatImageFrame {
    private int width, height;
    private int originalHeight, originalWidth;
    private ResourceLocation id;
    private List<ChatImageFrame> siblings = Lists.newArrayList();
    private final Minecraft minecraft = Minecraft.getInstance();
    private FrameError error = FrameError.OTHER_ERROR;
    private int index = 0;
    private int butter = 0;

    public ChatImageFrame(InputStream image) throws IOException {
        NativeImage nativeImage = NativeImage.read(image);
        this.id = this.minecraft.getTextureManager().register(MOD_ID + "/chatimage",
                new DynamicTexture(nativeImage));
        this.originalWidth = nativeImage.getWidth();
        this.originalHeight = nativeImage.getHeight();
    }

    public ChatImageFrame(BufferedImage image) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        NativeImage nativeImage = NativeImage.read(new ByteArrayInputStream(os.toByteArray()));
        this.id = this.minecraft.getTextureManager().register(MOD_ID + "/chatimage",
                new DynamicTexture(nativeImage));
        this.originalWidth = nativeImage.getWidth();
        this.originalHeight = nativeImage.getHeight();
    }

    public ChatImageFrame append(ChatImageFrame frame) {
        this.siblings.add(frame);
        return this;
    }

    public ChatImageFrame(FrameError error) {
        this.error = error;
    }

    /**
     * 载入图片
     *
     * @param limitWidth  限制的横向长度
     * @param limitHeight 限制的纵向长度
     * @return 载入成功返回true, 失败则为false
     */
    public boolean loadImage(int limitWidth, int limitHeight) {
        if (id == null) {
            return false;
        }
        if (index == 0) {
            limitSize(limitWidth, limitHeight);
        } else {
            this.siblings.get(index - 1).limitSize(limitWidth, limitHeight);
            ;
        }
        return true;
    }

    /**
     * 限制显示图片的长宽
     *
     * @param limitWidth  限制的横向长度
     * @param limitHeight 限制的纵向长度
     */
    public void limitSize(int limitWidth, int limitHeight) {
        this.width = originalWidth;
        this.height = originalHeight;
        if (limitWidth == 0) {
            limitWidth = this.minecraft.getWindow().getScreenWidth() / 2;
        }
        if (limitHeight == 0) {
            limitHeight = this.minecraft.getWindow().getScreenHeight() / 2;
        }
        BigDecimal b = new BigDecimal((float) originalHeight / originalWidth);
        double hx = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        if (width > limitWidth) {
            width = limitWidth;
            height = (int) (limitWidth * hx);
        }
        if (height > limitHeight) {
            height = limitHeight;
            width = (int) (limitHeight / hx);
        }
    }

    public FrameError getError() {
        return error;
    }

    public ResourceLocation getId() {
        if (index == 0) {
            return id;
        } else {
            return this.siblings.get(index - 1).getId();
        }
    }

    public int getHeight() {
        return height;
    }

    public List<ChatImageFrame> getSiblings() {
        return siblings;
    }

    public int getWidth() {
        return width;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getButter() {
        return butter;
    }

    public void setButter(int butter) {
        this.butter = butter;
    }

    public enum FrameError {
        FILE_NOT_FOUND,
        ID_NOT_FOUND,
        FILE_LOAD_ERROR,
        OTHER_ERROR,
        SERVER_FILE_LOAD_ERROR,

    }
}
