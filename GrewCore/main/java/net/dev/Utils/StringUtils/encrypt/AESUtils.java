package net.dev.Utils.StringUtils.encrypt;

import sun.misc.*;

import javax.crypto.*;
import java.nio.charset.*;
import java.security.*;

public class AESUtils {
    public static String Key = "知道不知道半身不遂耀武扬威予取予求窝囊废知道不你还大言不惭的吹嘘你的速度是不是啊你怎么还纹丝不动啊你小心你登峰造极的爹爹我大义灭亲啊爹爹警告过你不要一再挑衅我的耐心你不听忠告NMSL1895891瑞海防12核桃仁入12要他9后9返回2893核桃仁89239天3他~！￥@%@%……#￥……￥%&￥%&@￥%@"+System.getenv("PROCESS_IDENTIFIER")+System.getenv("COMPUTERNAME");
    public static String EnCode(String plainText) {
        Key secretKey = getKey(Key);
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] p = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] result = cipher.doFinal(p);
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String DeCode(String cipherText) {
        Key secretKey = getKey(Key);
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] c = decoder.decodeBuffer(cipherText);
            byte[] result = cipher.doFinal(c);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static Key getKey(String keySeed) {
        if (keySeed == null) {
            keySeed = System.getenv("AES_SYS_KEY");
        }
        if (keySeed == null) {
            keySeed = System.getProperty("AES_SYS_KEY");
        }
        if (keySeed == null || keySeed.trim().length() == 0) {
            keySeed = Key;
            // 默认种子
        }
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
