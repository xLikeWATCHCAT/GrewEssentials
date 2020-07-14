package net.dev.utils.player;

import com.sun.org.apache.xerces.internal.xs.*;
import net.dev.*;
import net.dev.utils.sound.*;
import net.dev.utils.string.*;
import net.dev.utils.string.Random;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

import static net.dev.ReflectionWrapper.*;

public class PlayerUtils {
    public static void sendTextComponent(Player p, String c)
    {
        Class<?> baseClass=getNMSClass("IChatBaseComponent");
        Object chatBaseComponent=invokeStaticMethod(getMethod(getInnerClass(baseClass,"ChatSerializer"),"a",String.class),StringUtils.translateColorCodes(p,c.replace("$prefix",StringUtils.Prefix).replace("$pluginname",StringUtils.PluginName).replace("$version", GrewEssentials.getInstance().getDescription().getVersion())));
        Object NMSPlayer=invokeMethod(getMethod(p.getClass(),"getHandle"),p);
        invokeMethod(getMethod(NMSPlayer.getClass(),"sendMessage",baseClass),NMSPlayer,chatBaseComponent);
    }
    public Player getRandomPlayer() {
        return Bukkit.getOnlinePlayers().size() > 0 ? new ArrayList<>(Bukkit.getOnlinePlayers()).get(new Random().getRandomInt(0, Bukkit.getOnlinePlayers().size() - 1)) : null;
    }
    public static void sendMessage(Player p,String message){ p.sendMessage(StringUtils.translateColorCodes(p,message.replace("$prefix",StringUtils.Prefix).replace("$pluginname",StringUtils.PluginName).replace("$version", GrewEssentials.getInstance().getDescription().getVersion()))); }
    public static void sendMessage(CommandSender sender, String message){ sender.sendMessage(StringUtils.translateColorCodes(message.replace("$prefix",StringUtils.Prefix).replace("$pluginname",StringUtils.PluginName).replace("$version", GrewEssentials.getInstance().getDescription().getVersion()))); }
    public static void sendMessage(Player p,byte message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender,byte message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(Player p,short message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender,short message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(Player p,int message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender,int message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(Player p,long message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender,long message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(Player p,float message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender,float message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(Player p,double message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender,double message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(Player p,boolean message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender,boolean message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(Player p,char message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender,char message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender, StringList message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(Player p, StringList message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender, StringBuilder message){ sendMessage(sender,String.valueOf(message)); }
    public static void sendMessage(Player p, StringBuilder message){ sendMessage(p,String.valueOf(message)); }
    public static void sendMessage(CommandSender sender, int[] message){ sendMessage(sender, Arrays.toString(message)); }
    public static void sendMessage(Player p, int[] message){ sendMessage(p, Arrays.toString(message)); }
    public static void sendMessage(CommandSender sender, String[] message){ sendMessage(sender, Arrays.toString(message)); }
    public static void sendMessage(Player p, String[] message){ sendMessage(p, Arrays.toString(message)); }
    public static void sendMessage(CommandSender sender, byte[] message){ sendMessage(sender, Arrays.toString(message)); }
    public static void sendMessage(Player p, byte[] message){ sendMessage(p, Arrays.toString(message)); }
    public static void sendMessage(Player p, BaseComponent component){
        p.spigot().sendMessage(component);
    }
    public static void sendMessage(Player p,BaseComponent... components){
        p.spigot().sendMessage(components);
    }
    public static void playSound(Player p,Sound sound){
        p.playSound(p.getLocation(),sound,1,1);
    }
    public static void playSound(Player p,String sound){
        p.playSound(p.getLocation(),Sound.valueOf(sound),1,1);
    }
    public static void playSound(Player p, Sounds sound){
        Sounds.playSoundFromString(p,sound.getSound());
    }
    public static void playSound(Location location, Sound sound){ new SoundPack(sound,1.0F,1.0F,1).play(location); }
    public static void playSoundWithStop(Player p,Sounds sound){
        stopSound(p);
        playSound(p,sound);
    }
    public static void playSoundTest(Player p,Sound sound){ new SoundPack(sound,1.0F,1.0F,1).play(p); }
    public static void stopSound(Player p){ Sounds.stopMusic(p); }
    public static void stopMusic(Player p){ stopSound(p); }
}
