package net.dev.Utils.PlayerUtils;

import net.dev.*;
import net.dev.Utils.StringUtils.Random;
import net.dev.Utils.StringUtils.*;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

import static net.dev.ReflectionWrapper.*;

public class PlayerUtil {
    public static void sendTextComponent(Player p, String c)
    {
        Class<?> baseClass=getNMSClass("IChatBaseComponent");
        Object chatBaseComponent=invokeStaticMethod(getMethod(getInnerClass(baseClass,"ChatSerializer"),"a",String.class),StringUtils.translateColorCodes(p,c.replace("$prefix",StringUtils.Prefix).replace("$pluginname",StringUtils.PluginName).replace("$version", GrewEssentials.getInstance().getDescription().getVersion())));
        Object NMSPlayer=invokeMethod(getMethod(p.getClass(),"getHandle"),p);
        invokeMethod(getMethod(NMSPlayer.getClass(),"sendMessage",baseClass),NMSPlayer,chatBaseComponent);
    }
    public Player getRandomPlayer() {
        Random a = new Random();
        return Bukkit.getOnlinePlayers().size() > 0 ? new ArrayList<>(Bukkit.getOnlinePlayers()).get(a.getRandomInt(0, Bukkit.getOnlinePlayers().size() - 1)) : null;
    }
    public static void sendMessage(Player p,String message){
        p.sendMessage(StringUtils.translateColorCodes(p,message.replace("$prefix",StringUtils.Prefix).replace("$pluginname",StringUtils.PluginName).replace("$version", GrewEssentials.getInstance().getDescription().getVersion())));
    }
    public static void sendMessage(CommandSender sender,String message){
        sender.sendMessage(StringUtils.translateColorCodes(message.replace("$prefix",StringUtils.Prefix).replace("$pluginname",StringUtils.PluginName).replace("$version", GrewEssentials.getInstance().getDescription().getVersion())));
    }
}
