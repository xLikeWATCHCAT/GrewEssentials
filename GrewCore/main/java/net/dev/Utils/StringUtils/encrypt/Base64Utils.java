package net.dev.Utils.StringUtils.encrypt;

import org.apache.commons.codec.binary.*;

public class Base64Utils {
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }
    public static String decode(byte[] bytes) {
        return new String(Base64.decodeBase64(bytes));
    }
}
