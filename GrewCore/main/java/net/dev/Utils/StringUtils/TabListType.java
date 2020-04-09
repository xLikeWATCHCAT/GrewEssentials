package net.dev.Utils.StringUtils;

import net.dev.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;

import java.util.*;

public class TabListType {
    public static ArrayList<String> offlinePlayerListToNameList(Vector<OfflinePlayer> v)
    {
        ArrayList<String> ret=new ArrayList<>();
        for(OfflinePlayer i : v)
            ret.add(i.getName());
        return ret;
    }

    public static ArrayList<String> getOfflinePlayersNameList()
    {
        return offlinePlayerListToNameList(VectorUtil.toVector(Bukkit.getOfflinePlayers()));
    }

    public static ArrayList<String> onlinePlayerListToNameList()
    {
        ArrayList<String> ret=new ArrayList<>();
        for(Player i : Bukkit.getOnlinePlayers())
            ret.add(i.getName());
        return ret;
    }
    public static ArrayList<String> getOnlinePlayersNameList()
    {
        return onlinePlayerListToNameList();
    }

    public static Vector<String> getFileType(){
        Vector<String> ret=new Vector<>();
        ret.add("config");
        ret.add("message");
        ret.add("database");
        ret.add("log");
        ret.add("bossbar");
        ret.add("javascript");
        ret.add("data");
        return ret;
    }
    public static ArrayList<String> getPlugins()
    {
        ArrayList<String> list = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
        {
            String pluginname = plugin.toString();
            String[] strs = pluginname.split(" ");
            list.add(strs[0]);
        }
        return list;
    }

    public static Vector<String> getTimingsType(){
        Vector<String> ret=new Vector<>();
        ret.add("paste");
        ret.add("start");
        ret.add("stop");
        return ret;
    }

    public static Vector<String> getTitles(){
        MemorySection ms=(MemorySection) GrewEssentials.getInstance().Message.get("Titles");
        return VectorUtil.toVector(ms.getKeys(false).parallelStream().toArray(String[]::new));
    }
}
