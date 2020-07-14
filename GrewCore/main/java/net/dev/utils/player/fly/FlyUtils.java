package net.dev.utils.player.fly;

import net.dev.database.*;
import net.dev.utils.database.*;
import org.bukkit.entity.*;

import java.lang.reflect.*;
import java.util.*;

public class FlyUtils {
    public static void enableFlight(Player p)  throws Throwable {
        p.setAllowFlight(true);
        Class<?> cpClass=p.getClass();
        Method getHandle=cpClass.getDeclaredMethod("getHandle");
        getHandle.setAccessible(true);
        Object NMSPlayer=getHandle.invoke(p);
        Field abi=NMSPlayer.getClass().getSuperclass().getDeclaredField("abilities");
        abi.setAccessible(true);
        Object abiObj=abi.get(NMSPlayer);
        Field isFlying=abiObj.getClass().getDeclaredField("isFlying");
        isFlying.setAccessible(true);
        isFlying.set(abiObj,true);
        Method update=NMSPlayer.getClass().getSuperclass().getDeclaredMethod("updateAbilities");
        update.setAccessible(true);
        update.invoke(NMSPlayer);
    }
    public static boolean isFlight(UUID u){
        String fly= LoadDatabase.db.dbSelectFirst("CommandsEnable","fly",new KeyValue(){{ this.add("uuid",u.toString()); }});
        if(fly!=null && fly.equals("1")){
            return true;
        }else{
            return false;
        }
    }
}
