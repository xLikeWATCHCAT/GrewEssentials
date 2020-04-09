package net.dev.API.Minecraft;

import net.dev.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

import static net.dev.ReflectionWrapper.*;

public class ActionBar {
    public static void sendActionBar(final Player p, final String s) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Class<?> cbc=getNMSClass("IChatBaseComponent");
                Object packet=newInstance(getConstructor(getNMSClass("PacketPlayOutChat"),cbc,byte.class), invokeStaticMethod(getMethod(getInnerClass(cbc,"ChatSerializer"),"a",String.class),"{\"text\":\"" + s.replace("\\","\\\\").replace("\"","\\\"") + "\"}"),(byte)2);
                Object NMSPlayer=invokeMethod(getMethod(p.getClass(),"getHandle"),p);
                Object con=getFieldValue(getField(NMSPlayer.getClass(),"playerConnection"),NMSPlayer);
                invokeMethod(getMethod(con.getClass(),"sendPacket",getNMSClass("Packet")),con,packet);
            }
        }.runTaskAsynchronously(GrewEssentials.instance);
    }
}
