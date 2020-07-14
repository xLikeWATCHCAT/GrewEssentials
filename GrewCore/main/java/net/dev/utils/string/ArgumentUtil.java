package net.dev.utils.string;

import org.bukkit.*;

import java.util.*;

public class ArgumentUtil {
    public static boolean isOnlinePlayerExists(UUID uuid)
    {
        return Bukkit.getPlayer(uuid)!=null;
    }
    public static boolean isOnlinePlayerExists(String name)
    {
        return Bukkit.getPlayer(name)!=null;
    }
    public static boolean isDouble(String s)
    {
        try {
            return !Double.valueOf(s).equals(Double.NaN);
        }catch(Throwable e) {return false;}
    }
    public static boolean isLong(String s)
    {
        try {
            Long.valueOf(s);
            return true;
        }catch(Throwable e) {return false;}
    }
    public static boolean isInteger(String s)
    {
        try {
            Integer.valueOf(s);
            return true;
        }catch(Throwable e) {return false;}
    }
    public static boolean isUUID(String s)
    {
        try {
            UUID.fromString(s);
            return true;
        }catch(Throwable e) {return false;}
    }
}
