package net.dev.utils;

import net.dev.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

import java.lang.reflect.*;

import static net.dev.ReflectionWrapper.*;
import static net.dev.utils.string.StringUtils.*;

public class Utils {
    private static final Method getHandle=getMethod(getCraftClass("entity.CraftPlayer"),"getHandle");
    public static final Method sp=getMethod(getNMSClass("PlayerConnection"), "sendPacket", getNMSClass("Packet"));
    public static final Field pcon=getField(getNMSClass("EntityPlayer"), "playerConnection");
    public static void sendConsole(String msg) { Bukkit.getConsoleSender().sendMessage(translateColorCodes(msg)); }
    public static void notOnline(CommandSender sender, String notOnlinePlayer){ sender.sendMessage(NotOnline.replace("$playername",notOnlinePlayer)); }
    public static void broadcast(String string){ Bukkit.broadcastMessage(translateColorCodes(string)); }
    public static void createHelix(final Block block1, final Object pa, final int number, final double radius) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = block1.getLocation();
                //System.out.println(getNMSClass("PacketPlayOutWorldParticles").getDeclaredConstructors()[1].toGenericString());
                Object packet=newInstance(getConstructor(getNMSClass("PacketPlayOutWorldParticles"),getNMSClass("EnumParticle"),boolean.class,float.class,float.class,float.class,float.class,float.class,float.class,float.class,int.class,int[].class),pa,true,loc.getX(),loc.getY(),loc.getZ(),0,0,0,0,1,new int[0]);
                for (Player online : Bukkit.getOnlinePlayers()) {
                    Object NMSPlayer=invokeMethod(getMethod(online.getClass(),"getHandle"),online);
                    Object con=getFieldValue(getField(NMSPlayer.getClass(),"playerConnection"),NMSPlayer);
                    invokeMethod(getMethod(con.getClass(),"sendPacket",getNMSClass("Packet")),con,packet);
                }
                this.cancel();
                return;
            }
        }.runTaskAsynchronously(GrewEssentials.instance);
    }
    public static void createPlayerHelix(final Player player, final Object pa, final int number, final double radius) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = player.getLocation();
                for (double y = 0; y <= number; y += 0.1) {
                    double x = radius * Math.cos(y);
                    double z = radius * Math.sin(y);
                    //System.out.println(getNMSClass("PacketPlayOutWorldParticles").getDeclaredConstructors()[1].toGenericString());
                    Object packet=newInstance(getConstructor(getNMSClass("PacketPlayOutWorldParticles"),getNMSClass("EnumParticle"),boolean.class,float.class,float.class,float.class,float.class,float.class,float.class,float.class,int.class,int[].class),pa, true, (float) (loc.getX() + x), (float) (loc.getY()), (float) (loc.getZ() + z), 0, 0, 0, 0, 1,new int[0]);

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        Object NMSPlayer=invokeMethod(getMethod(online.getClass(),"getHandle"),online);
                        Object con=getFieldValue(getField(NMSPlayer.getClass(),"playerConnection"),NMSPlayer);
                        invokeMethod(getMethod(con.getClass(),"sendPacket",getNMSClass("Packet")),con,packet);
                    }
                }
                this.cancel();
                return;
            }
        }.runTaskAsynchronously(GrewEssentials.instance);
    }
    public static void sendPacketToAllPlayers(Object pack)
    {
        for(Player po : Bukkit.getOnlinePlayers())
        {
            sendPacket(po,pack);
        }
    }
    public static void sendPacket(Player p, Object packet) {
        try {
            Object NMSPlayer = getHandle.invoke(p);
            Object con = pcon.get(NMSPlayer);
            sp.invoke(con,packet);
        }catch(Throwable e){throw new RuntimeException(e);}
    }
    public static void removePlayerInTabList(Player p){
        new Thread(()->{
            Object NMSPlayer=invokeMethod(getHandle,p);
            Class<?> action=getInnerClass(getNMSClass("PacketPlayOutPlayerInfo"),"EnumPlayerInfoAction");
            Object ps= Array.newInstance(NMSPlayer.getClass(),1);
            Array.set(ps,0,NMSPlayer);
            Object pack=newInstance(getConstructor(getNMSClass("PacketPlayOutPlayerInfo"),action, ps.getClass()),Enum.valueOf((Class)action,"REMOVE_PLAYER"),ps);
            sendPacketToAllPlayers(pack);
        }).start();
    }
}
