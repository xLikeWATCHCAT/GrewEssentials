package net.dev.API.Minecraft;


import org.bukkit.entity.*;

import java.lang.reflect.*;
import java.util.*;

import static net.dev.ReflectionWrapper.*;
import static net.dev.Utils.Utils.*;
import static org.bukkit.ChatColor.*;

public class TitleAPI {
    /**
     * 使用方法例示
     * TitleAPI.sendFullTitle(player,100,100,100,"显示在最上面的大Title","显示在大Title下面的小Title");
     *
     **/

    public static void sendFullTitle(Player player, Integer fadeIn, Integer stay,
                                     Integer fadeOut, String title, String subtitle)
    {
        sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
    }
    public static void sendTitle(Player player, String MainTitle, String SubTitle){
        sendFullTitle(player,-1,-1,-1,MainTitle,SubTitle);
    }

    @SuppressWarnings("rawtypes")
    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle)
    {
        try
        {
            if (title != null)
            {
                title = getString(player, fadeIn, stay, fadeOut, title, title);
                Object e;
                Object chatTitle;
                Constructor subtitleConstructor;
                Object titlePacket;
                e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TITLE").get(null);
                chatTitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
                titlePacket = subtitleConstructor.newInstance(e, chatTitle);
                sendPacket(player, titlePacket);
            }
            if (subtitle != null)
            {
                subtitle = getString(player, fadeIn, stay, fadeOut, title, subtitle);
                Object e;
                Object chatSubtitle;
                Constructor subtitleConstructor;
                Object subtitlePacket;
                e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                chatSubtitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);
            }
        }
        catch (Exception var11)
        {
            var11.printStackTrace();
        }
    }

    private static String getString(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) throws IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        subtitle = translateAlternateColorCodes('&', subtitle);
        subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
        Object e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TIMES").get(null);
        Object chatSubtitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
        Constructor subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
        Object subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
        sendPacket(player, subtitlePacket);
        return subtitle;
    }
}
