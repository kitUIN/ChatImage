package github.kituin.chatimage.tool;

public class ChatImageTool {
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
