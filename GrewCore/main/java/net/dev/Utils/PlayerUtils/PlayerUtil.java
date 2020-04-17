package net.dev.Utils.PlayerUtils;

import net.dev.Utils.StringUtils.Random;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

import static net.dev.ReflectionWrapper.*;

public class PlayerUtil {
    public static void sendTextComponent(Player p, String c)
    {
        Class<?> baseClass=getNMSClass("IChatBaseComponent");
        Object chatBaseComponent=invokeStaticMethod(getMethod(getInnerClass(baseClass,"ChatSerializer"),"a",String.class),c);
        Object NMSPlayer=invokeMethod(getMethod(p.getClass(),"getHandle"),p);
        invokeMethod(getMethod(NMSPlayer.getClass(),"sendMessage",baseClass),NMSPlayer,chatBaseComponent);
    }
    public Player getRandomPlayer() {
        Random a = new Random();
        return Bukkit.getOnlinePlayers().size() > 0 ? new ArrayList<>(Bukkit.getOnlinePlayers()).get(a.getRandomInt(0, Bukkit.getOnlinePlayers().size() - 1)) : null;
    }

}
