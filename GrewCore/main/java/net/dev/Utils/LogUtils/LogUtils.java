package net.dev.Utils.LogUtils;

import net.dev.*;
import net.dev.Utils.StringUtils.*;

import java.io.*;
import java.util.*;

public class LogUtils {
    public static FileOutputStream log;
    public static void writeLog(String s)
    {
        new Thread(()->{
            if(GrewEssentials.getInstance().Config.getBoolean("log",true)){
                try {
                    log.write((new Date().toString()+" : "+ StringUtils.removeColorCodes(s)+"\n").getBytes("UTF8"));
                }catch(Throwable e){if(e instanceof RuntimeException)  throw (RuntimeException)e; throw new RuntimeException(e);}
            }
        }).start();
    }
    public static void log(){
        try {
            if(GrewEssentials.getInstance().Config.getBoolean("log",true)){
                Date d = new Date();
                File l = new File("plugins" + File.separator + "GrewEssentials" + File.separator + "logs" + File.separator + (d.getYear()+1900) +"-"+ (d.getMonth()+1) + "-" + d.getDate() + File.separator + ((d.getYear()+1900)+"-"+(d.getMonth()+1)+"-"+d.getDate()+"-"+d.getHours()+"-"+d.getMinutes()+"-"+d.getSeconds()+"-"+System.nanoTime()) + ".log");
                l.getParentFile().mkdirs();
                l.createNewFile();
                log=new FileOutputStream(l);
            }
        }catch(Throwable e){if(e instanceof RuntimeException)  throw (RuntimeException)e; throw new RuntimeException(e);}
    }
}
