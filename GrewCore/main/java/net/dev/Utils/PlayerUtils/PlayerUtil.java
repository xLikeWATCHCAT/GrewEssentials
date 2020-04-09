package net.dev.Utils.PlayerUtils;

import org.bukkit.entity.*;

import static net.dev.ReflectionWrapper.*;

public class PlayerUtil {
    public static void sendTextComponent(Player p, String c)
    {
        Class<?> baseClass=getNMSClass("IChatBaseComponent");
        Object chatBaseComponent=invokeStaticMethod(getMethod(getInnerClass(baseClass,"ChatSerializer"),"a",String.class),c);
        Object NMSPlayer=invokeMethod(getMethod(p.getClass(),"getHandle"),p);
        invokeMethod(getMethod(NMSPlayer.getClass(),"sendMessage",baseClass),NMSPlayer,chatBaseComponent);
    }
}
