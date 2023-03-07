package github.kituin.chatimage.tool;

import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
@SuppressWarnings(value = {"unchecked"})
public class ChatImageFrame<T> {
    private int width, height;
    private int originalHeight, originalWidth;
    private T id;
    private final List<ChatImageFrame<T>> siblings = Lists.newArrayList();
    public static TextureHelper<?> textureHelper;
    private FrameError error = FrameError.OTHER_ERROR;
    private int index = 0;
    private int butter = 0;

    public ChatImageFrame(InputStream image) throws IOException {
        TextureReader<T> temp = (TextureReader<T>) textureHelper.loadTexture(image);
        this.id = temp.getId();
        this.originalWidth = temp.getWidth();
        this.originalHeight = temp.getHeight();
    }

    public ChatImageFrame(BufferedImage image) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        TextureReader<T> temp = (TextureReader<T>) textureHelper.loadTexture(new ByteArrayInputStream(os.toByteArray()));
        this.id = temp.getId();
        this.originalWidth = temp.getWidth();
        this.originalHeight = temp.getHeight();
    }

    public ChatImageFrame<T> append(ChatImageFrame<T> frame) {
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

    public T getId() {
        if (index == 0) {
            return id;
        } else {
            return (T) this.siblings.get(index - 1).getId();
        }
    }

    public int getHeight() {
        return height;
    }

    public List<ChatImageFrame<T>> getSiblings() {
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

    public static class TextureReader<T> {
        public T id;
        public int width;
        public int height;

        public TextureReader(T id, int width, int height) {
            this.id = id;
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public T getId() {
            return id;
        }
    }

    @FunctionalInterface
    public interface TextureHelper<T> {
        TextureReader<T> loadTexture(InputStream image) throws IOException;

    }
}
