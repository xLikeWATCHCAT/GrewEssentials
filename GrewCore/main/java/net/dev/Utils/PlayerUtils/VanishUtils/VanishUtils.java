package net.dev.Utils.PlayerUtils.VanishUtils;

import net.dev.Commands.*;
import net.dev.Database.*;
import net.dev.Utils.DatabaseUtils.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;

import java.lang.reflect.*;
import java.util.*;

import static net.dev.ReflectionWrapper.*;
import static net.dev.Utils.Utils.*;

public class VanishUtils {
    public static void vanishedset(PlayerJoinEvent event){
        Player p = event.getPlayer();
        UUID u = p.getUniqueId();
        Object NMSPlayer=invokeMethod(getMethod(p.getClass(),"getHandle"),p);
        Class<?> action=getInnerClass(getNMSClass("PacketPlayOutPlayerInfo"),"EnumPlayerInfoAction");
        Object ps= Array.newInstance(NMSPlayer.getClass(),1);
        Array.set(ps,0,NMSPlayer);
        if(isVanished(u)) {
            VanishCommand.setVanished(event.getPlayer(), true);
            new Thread(()->{
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Object pack=newInstance(getConstructor(getNMSClass("PacketPlayOutPlayerInfo"),action, ps.getClass()),Enum.valueOf((Class)action,"REMOVE_PLAYER"),ps);
                sendPacketToAllPlayers(pack);
            }).start();
           }else{
            VanishCommand.setVanished(event.getPlayer(), false);
        }
    }
    public static boolean isVanished(UUID u){
        String vanish= LoadDatabase.db.dbSelectFirst("CommandsEnable","vanish",new KeyValue(){{ this.add("uuid",u.toString()); }});
        if(vanish!=null && vanish.equals("1")) {
            return true;
        }else{
            return false;
        }
    }
}
