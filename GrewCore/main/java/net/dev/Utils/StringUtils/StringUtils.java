package net.dev.Utils.StringUtils;

import com.google.common.collect.*;
import me.clip.placeholderapi.*;
import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.io.*;
import java.util.*;

public class StringUtils {
    public static String PluginName;
    static{
        PluginName = GrewEssentials.getInstance().getDescription().getName();
        init();
    }
    public static void init()
    {
        try {
            GrewEssentials.getInstance().Message.reload();
            Prefix = translateColorCodes(GrewEssentials.getInstance().Message.getString("Prefix")).replace("$pluginname", StringUtils.PluginName);
            DoNotHavePerMission = translateColorCodes(GrewEssentials.getInstance().Message.getString("DoNotHavePerMission").replace("$prefix",Prefix).replace("$pluginname", StringUtils.PluginName));
            HelpMid = translateColorCodes(GrewEssentials.getInstance().Message.getString("Help.Mid"));
            OnlyPlayer = translateColorCodes(GrewEssentials.getInstance().Message.getString("OnlyPlayer").replace("$prefix",Prefix).replace("$pluginname", StringUtils.PluginName));
            NotOnline = translateColorCodes(GrewEssentials.getInstance().Message.getString("NotOnline").replace("$prefix",Prefix).replace("$pluginname", StringUtils.PluginName));
            NoHavePlayer = translateColorCodes(GrewEssentials.getInstance().Message.getString("NoHavePlayer").replace("$prefix",Prefix).replace("$pluginname", StringUtils.PluginName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String Prefix,DoNotHavePerMission,HelpMid,NotOnline,OnlyPlayer,NoHavePlayer;

    private static String getColoredString(String string) { return string.replace("&", "§"); }
    public static String translateColorCodes(String string){ return ChatColor.translateAlternateColorCodes('&', string); }
    public static String translateColorCodes(Player p,String string){ return translateColorCodes(PlaceholderAPI.setPlaceholders(p,string)); }
    public static String removeColorCodes(String string){ return ChatColor.stripColor(getColoredString(string)); }
    public static String getNanon(){ return String.valueOf(System.nanoTime()); }
    public static String configStringBuilder(String path) { return StringUtils.translateColorCodes(StringUtils.Prefix+GrewEssentials.getInstance().Message.getString(path)); }
    public static String getConfig(String path) { return StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString(path)); }
    public static List<String> toLowerCase(List<String> s)
    {
        List<String> ret=new ArrayList<>();
        ret.addAll(s);
        return ret;
    }
    public static String getCommandInfo(String com)
    {
        for(Command i : RegisterCommands.cmds)
        {
            if(i.getName().equalsIgnoreCase(com) || toLowerCase(i.getAliases()).contains(com.toLowerCase())) {
                return StringUtils.translateColorCodes("&r&a" + i.getUsage() + " "+StringUtils.HelpMid+" &7" + i.getDescription()) + "\n";
            }
        }
        return StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Help.Cant_Find")).replace("$prefix",StringUtils.Prefix);
    }
    public static boolean isNullOrEmpty(String s)
    {
        return s==null || s.isEmpty();
    }
    public static boolean isNullOrEmptyWithTrim(String s)
    {
        return s==null||isNullOrEmpty(s.trim());
    }
    public static <T> ArrayList<T> toArrayList(Collection<T> c) { return (ArrayList<T>) Lists.newArrayList(c.parallelStream().toArray(Object[]::new)); }
}
