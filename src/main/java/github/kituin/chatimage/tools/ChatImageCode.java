package github.kituin.chatimage.tools;

import github.kituin.chatimage.Exceptions.InvalidChatImageCodeException;
import github.kituin.chatimage.Exceptions.InvalidChatImageUrlException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static github.kituin.chatimage.client.ChatImageClient.LOGGER;
import static github.kituin.chatimage.client.ChatImageClient.MOD_ID;

import static github.kituin.chatimage.tools.HttpUtils.CLOCK_MAP;


/**
 * @author kitUIN
 */
public class ChatImageCode {
    private final Pattern pattern = Pattern.compile("\\[CICode,(.+)\\]");
    private ChatImageUrl url;
    private boolean nsfw;
    private MinecraftClient minecraft = MinecraftClient.getInstance();
    private int width;
    private  Identifier identifier;
    private int height;
    private int originalWidth;
    private int originalHeight;
    ChatImageCode() { }
    public ChatImageCode(String url) throws InvalidChatImageUrlException {
        this.url = ChatImageUrl.of(url);
    }
    public ChatImageCode(ChatImageUrl url)
    {
        this.url = url;
    }
    /**
     * 载入纹理<br/>
     * 注意特判 Identifier 不为 null
     * @return Identifier(如果加载失败返回null)
     */
    private Identifier loadTexture()  {
        InputStream inputStream = null;
        String httpUrl =  this.getOriginalUrl();
        if(CLOCK_MAP.containsKey(httpUrl))
        {
            return CLOCK_MAP.get(httpUrl);
        }
        try{
            switch (this.getUrlMethod())
            {
                case HTTP:
                    File temp = new File(this.url.getCachePathUrl());
                    if(temp.exists()) {
                        inputStream =  new FileInputStream(temp);
                    }
                    else {

                        if(!CLOCK_MAP.containsKey(httpUrl))
                        {
                            HttpUtils.getInputStream(httpUrl);
                        }
                        return null;
                    }
                    break;
                case BASE64:
                    byte[] bytes = Base64.getDecoder().decode(this.url.getBase64Data());
                    inputStream = new ByteArrayInputStream(bytes);
                    break;
                default:
                    return null;
            }
            NativeImage nativeImage = NativeImage.read(inputStream);
            return this.minecraft.getTextureManager().registerDynamicTexture(MOD_ID + "/chatimage",
                    new NativeImageBackedTexture(nativeImage));
        }catch ( IOException ep)
        {

        }
        return null;
    }


    /**
     * 匹配 {@link ChatImageCode}
     * @param originalCode 字符串模式的 {@link ChatImageCode}
     * @throws InvalidChatImageCodeException 匹配失败
     */
    private void match(String originalCode) throws InvalidChatImageCodeException {
        Matcher matcher = pattern.matcher(originalCode);
        if(matcher.find())
        {
            slice(matcher.group(1));
        }
        else
        {
            throw new InvalidChatImageCodeException("can not match ChatImageCode, please recheck");
        }
    }

    /**
     * 切片每个code属性
     * @param rawCode 原始code
     * @throws InvalidChatImageCodeException 切片失败
     */
    private void slice(String rawCode) throws InvalidChatImageCodeException {
        String[] raws = rawCode.split(",");
        for (String raw:raws) {
            String[] temps = raw.split("=",2);
            if(temps.length == 2)
            {
                String name = temps[0].replace(" ","");
                switch (name)
                {
                    case "url":
                        try {
                            this.url = ChatImageUrl.of(temps[1]);
                        } catch (InvalidChatImageUrlException e) {
                            throw new InvalidChatImageCodeException(e.getMessage());
                        }
                        break;
                    case "nsfw":
                        this.nsfw = Boolean.getBoolean(temps[1]);
                        break;
                    default:
                        break;
                }
            }
            else
            {
                throw new InvalidChatImageCodeException("can not match the value of ChatImageCode, please recheck");
            }
        }
    }



    /**
     * 载入图片
     * @param limitWidth 限制的横向长度
     * @param limitHeight 限制的纵向长度
     * @return 载入成功返回true,失败则为false
     */
    public boolean loadImage(int limitWidth,int limitHeight)
    {
        identifier = loadTexture();
        if(identifier == null) {
            return false;
        }
        NativeImageBackedTexture texture = (NativeImageBackedTexture) minecraft.getTextureManager().getTexture(identifier);
        NativeImage image = texture.getImage();
        originalWidth = image.getWidth();
        originalHeight = image.getHeight();
        limitSize(limitWidth,limitHeight);
        return true;
    }
    /** 限制显示图片的长宽
     * @param limitWidth 限制的横向长度
     * @param limitHeight 限制的纵向长度
     */
    private void limitSize(int limitWidth,int limitHeight)
    {
        width = originalWidth;
        height = originalHeight;
        if(limitWidth == 0 && limitHeight == 0)
        {
            limitWidth = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2;
            limitHeight = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2;
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
    /**
     * 从字符串 加载 {@link ChatImageCode}
     * @param code 字符串模式的 {@link ChatImageCode}
     * @return {@link ChatImageCode}
     * @throws InvalidChatImageCodeException 加载失败
     */
    public static ChatImageCode of(String code) throws InvalidChatImageCodeException
    {
        ChatImageCode chatImageCode = new ChatImageCode();
        chatImageCode.match(code);
        return chatImageCode;
    }

    /**
     * 加载只带{@link #url}的{@link ChatImageCode}
     * @param url url
     * @return {@link ChatImageCode}
     */
    public static ChatImageCode ofUrl(String url) throws InvalidChatImageUrlException {
        return new ChatImageCode(ChatImageUrl.of(url));
    }
    public String getOriginalUrl()
    {
        return this.url.getOriginalUrl();
    }
    public ChatImageUrl getUrl()
    {
        return this.url;
    }
    public ChatImageUrl.UrlMethod getUrlMethod()
    {
        return this.url.getUrlMethod();
    }
    public boolean getNsfw()
    {
        return this.nsfw;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[CICode,url=").append(this.url.getOriginalUrl());
        if(nsfw)
        {
            builder.append("nsfw=true");
        }
        return builder.append("]").toString();
    }
    public int getWidth()
    {
        return this.width;
    }
    public int getHeight()
    {
        return this.height;
    }
    public int getOriginalWidth()
    {
        return this.originalWidth;
    }
    public int getOriginalHeight()
    {
        return this.originalHeight;
    }
    public Identifier getIdentifier()
    {
        return this.identifier;
    }
}
