package net.dev.utils.string.encrypt;

import java.nio.charset.*;

public class Base64Utils {
    public static String DeCode(String b) { return new String(java.util.Base64.getDecoder().decode(b), StandardCharsets.UTF_8); }
    public static String EnCode(String codes){ return java.util.Base64.getEncoder().encodeToString(codes.getBytes(StandardCharsets.UTF_8)); }
}
