package net.dev.file;

import net.dev.utils.http.*;
import net.dev.utils.string.*;

import java.net.*;

public class LoadJavaScriptFile {
    public static URL loadurl;
    public static String load;
    public static boolean loadc;
    public static String Encryption;
    public static boolean Encryptiona;
    public static String JsURL;
    public static String JavaScript;
    static{
        try{
            loadurl =new URL("https://gitee.com/GrewMC/GrewEssentials/raw/master/LoadwebJavaScript.boolean");
            load = StringUtils.replaceBlank(WebUtils.readURLUTF8(loadurl));
            loadc = load.toLowerCase().contains("true");
            Encryption = WebUtils.readURLUTF8(new URL("https://gitee.com/GrewMC/GrewEssentials/raw/master/EncryptionJSCode.boolean"));
            Encryptiona = Encryption.toLowerCase().contains("true");
            JsURL = "https://gitee.com/GrewMC/GrewEssentials/raw/master/GrewEssentials.js";
            JavaScript = WebUtils.readURLUTF8(new URL(JsURL));
        }catch(Throwable e){throw new RuntimeException(e);}
    }
}
