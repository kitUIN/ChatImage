package github.kituin.chatimage.tool;

import net.minecraft.text.Text;

public class ChatImageTool {
    public static final String DEFAULT_CHAT_IMAGE_SHOW_NAME = Text.translatable("codename.chatimage.default").getString();

    /**
     * bytes to Hex String
     *
     * @param bytes bytes
     * @return HEX String
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
