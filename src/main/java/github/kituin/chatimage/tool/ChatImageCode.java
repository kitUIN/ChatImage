package github.kituin.chatimage.tool;

import github.kituin.chatimage.exception.InvalidChatImageCodeException;
import github.kituin.chatimage.exception.InvalidChatImageUrlException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static github.kituin.chatimage.Chatimage.CONFIG;


/**
 * @author kitUIN
 */
public class ChatImageCode {
    public static Pattern pattern = Pattern.compile("\\[\\[CICode,(.+)\\]\\]");
    public static HashMap<String, ChatImageFrame> CACHE_MAP = new HashMap<>();
    private ChatImageUrl url;
    private boolean nsfw = false;
    private final boolean isSelf;
    private final long timestamp;
    private String name = "codename.chatimage.default";

    ChatImageCode(boolean isSelf) {
        this.isSelf = isSelf;
        this.timestamp = System.currentTimeMillis();
    }

    public ChatImageCode(String url) throws InvalidChatImageUrlException {
        this(new ChatImageUrl(url), null, false);
    }

    public ChatImageCode(String url, @Nullable String name) throws InvalidChatImageUrlException {
        this(new ChatImageUrl(url), name, false);
    }

    public ChatImageCode(ChatImageUrl url) {
        this(url, null, false);
    }

    public ChatImageCode(ChatImageUrl url, @Nullable String name, boolean isSelf) {
        this.url = url;
        if (name != null) {
            this.name = name;
        }
        this.timestamp = System.currentTimeMillis();
        this.isSelf = isSelf;
    }

    /**
     * 从字符串 加载 {@link ChatImageCode}
     *
     * @param code 字符串模式的 {@link ChatImageCode}
     * @return {@link ChatImageCode}
     * @throws InvalidChatImageCodeException 加载失败
     */
    public static ChatImageCode of(String code) throws InvalidChatImageCodeException {
        return ChatImageCode.of(code, false);
    }

    public static ChatImageCode of(String code, boolean self) throws InvalidChatImageCodeException {
        ChatImageCode chatImageCode = new ChatImageCode(self);
        chatImageCode.match(code);
        return chatImageCode;
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
                        this.name = temps[1].trim();
                        break;
                    default:
                        break;
                }
            } else {
                throw new InvalidChatImageCodeException(raw + "<-can not match the value of ChatImageCode, Please Recheck");
            }
        }
    }


    public String getOriginalUrl() {
        return this.url.getOriginalUrl();
    }

    public ChatImageUrl getChatImageUrl() {
        return this.url;
    }

    public boolean getNsfw() {
        return this.nsfw;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[[CICode,url=").append(this.url.getOriginalUrl());
        if (nsfw) {
            sb.append(",nsfw=true");
        }
        if (name != null) {
            sb.append(",name=").append(name);
        }
        return sb.append("]]").toString();
    }

    public String getName() {
        return this.name;
    }

    public boolean isSendFromSelf() {
        return isSelf;
    }


    public boolean isTimeout() {
        return System.currentTimeMillis() > this.timestamp + 1000L * CONFIG.timeout;
    }
}
