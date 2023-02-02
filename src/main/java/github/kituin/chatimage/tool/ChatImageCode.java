package github.kituin.chatimage.tool;

import github.kituin.chatimage.exception.InvalidChatImageCodeException;
import github.kituin.chatimage.exception.InvalidChatImageUrlException;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static github.kituin.chatimage.tool.HttpUtils.CACHE_MAP;


/**
 * @author kitUIN
 */
public class ChatImageCode {
    public static Pattern pattern = Pattern.compile("\\[CICode,(.+)\\]");
    private ChatImageUrl url;
    private boolean nsfw = false;

    private String name = "[" + Text.translatable("codename.chatimage.default").getString() + "]";

    ChatImageCode() {
    }

    public ChatImageCode(String url) throws InvalidChatImageUrlException {
        this.url = new ChatImageUrl(url);
    }

    public ChatImageCode(String url, @Nullable String name) throws InvalidChatImageUrlException {
        this.url = new ChatImageUrl(url);
        if (name != null) {
            this.name = "[" + name + "]";
        }

    }

    public ChatImageCode(ChatImageUrl url) {
        this.url = url;
    }

    public ChatImageCode(ChatImageUrl url, String name) {
        this.url = url;
        this.name = "[" + name + "]";
    }

    /**
     * 从字符串 加载 {@link ChatImageCode}
     *
     * @param code 字符串模式的 {@link ChatImageCode}
     * @return {@link ChatImageCode}
     * @throws InvalidChatImageCodeException 加载失败
     */
    public static ChatImageCode of(String code) throws InvalidChatImageCodeException {
        ChatImageCode chatImageCode = new ChatImageCode();
        chatImageCode.match(code);
        return chatImageCode;
    }

    /**
     * 加载只带{@link #url}的{@link ChatImageCode}
     *
     * @param url url {@link ChatImageUrl}
     * @return {@link ChatImageCode}
     */
    public static ChatImageCode ofUrl(ChatImageUrl url) {
        return new ChatImageCode(url);
    }

    /**
     * 载入纹理<br/>
     * 注意特判 Identifier 不为 null
     *
     * @return Identifier(如果加载失败返回null)
     */
    public ChatImageFrame getFrame() {
        String useUrl = this.url.getUrl();
        if (CACHE_MAP.containsKey(useUrl)) {
            return CACHE_MAP.get(useUrl);
        } else {
            return new ChatImageFrame(ChatImageFrame.FrameError.ID_NOT_FOUND);
        }
    }


    /**
     * 匹配 {@link ChatImageCode}
     *
     * @param originalCode 字符串模式的 {@link ChatImageCode}
     * @throws InvalidChatImageCodeException 匹配失败
     */
    private void match(String originalCode) throws InvalidChatImageCodeException {
        Matcher matcher = pattern.matcher(originalCode);
        if (matcher.find()) {
            slice(matcher.group(1));
        } else {
            throw new InvalidChatImageCodeException(originalCode + "<-can not find any String to ChatImageCode, Please Recheck");
        }
    }

    /**
     * 切片每个code属性
     *
     * @param rawCode 原始code
     * @throws InvalidChatImageCodeException 切片失败
     */
    private void slice(String rawCode) throws InvalidChatImageCodeException {
        if (!rawCode.contains("url")) {
            throw new InvalidChatImageCodeException("not match url in ChatImageCode, Please Recheck");
        }
        String[] raws = rawCode.split(",");
        for (String raw : raws) {
            String[] temps = raw.split("=", 2);
            if (temps.length == 2) {
                String value = temps[0].trim();
                switch (value) {
                    case "url":
                        try {
                            this.url = new ChatImageUrl(temps[1].trim());
                        } catch (InvalidChatImageUrlException e) {
                            throw new InvalidChatImageCodeException(e.getMessage(), e.getMode());
                        }
                        break;
                    case "nsfw":
                        this.nsfw = Boolean.parseBoolean(temps[1].trim());
                        break;
                    case "name":
                        this.name = "[" + temps[1].trim() + "]";
                        break;
                    default:
                        break;
                }
            } else {
                throw new InvalidChatImageCodeException(raw + "<-can not match the value of ChatImageCode, Please Recheck");
            }
        }
    }


    private static String parse(String url, boolean nsfw, @Nullable String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("[CICode,url=").append(url);
        if (nsfw) {
            sb.append(",nsfw=true");
        }
        if (name != null) {
            sb.append(",name=").append(name);
        }
        return sb.append("]").toString();
    }

    public String getOriginalUrl() {
        return this.url.getOriginalUrl();
    }

    public ChatImageUrl getChatImageUrl() {
        return this.url;
    }

    public ChatImageUrl.UrlMethod getUrlMethod() {
        return this.url.getUrlMethod();
    }

    public boolean getNsfw() {
        return this.nsfw;
    }

    @Override
    public String toString() {
        return parse(url.getOriginalUrl(), nsfw, name.substring(1, name.length() - 1));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = "[" + name + "]";
    }


}
