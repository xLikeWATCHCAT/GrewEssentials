package net.dev.utils.http;

import java.io.*;
import java.net.*;

public class WebUtils {
    public static byte[] readURL(URL url)
    {
        try{
            URLConnection con=url.openConnection();
            con.addRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Mobile Safari/537.36");
            InputStream is=con.getInputStream();
            ByteArrayOutputStream bo=new ByteArrayOutputStream();
            while(true)
            {
                int b=is.read();
                if(b==-1)
                    break;
                bo.write(b);
            }
            return bo.toByteArray();
        }catch(Throwable e){throw new RuntimeException(e);}
    }
    public static String readURLString(URL url,String encode)
    {
        try{
            return new String(readURL(url),encode);
        }catch(Throwable e){throw new RuntimeException(e);}
    }
    public static String readURLUTF8(URL url) { return readURLString(url,"UTF8"); }
    public static String readURLGBK(URL url) { return readURLString(url,"GBK"); }
    public static String readURLUTF16(URL url)
    {
        return readURLString(url,"Unicode");
    }
    @Deprecated
    public static String readURLUTF8(String url) throws MalformedURLException {return readURLUTF8(new URL(url));}
    @Deprecated
    public static String readURLGBK(String url) throws MalformedURLException {return readURLGBK(new URL(url));}
    @Deprecated
    public static String readURLUTF16(String url) throws MalformedURLException {return readURLUTF16(new URL(url));}
    @Deprecated
    public static String get(String url) throws MalformedURLException {return readURLUTF8(url);}
}
