package net.dev.Listeners;

import net.dev.*;
import net.dev.API.Minecraft.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import static net.dev.ReflectionWrapper.*;
import static net.dev.Utils.StringUtils.StringUtils.*;

class DedicatedMethods {
    private static final Object DEFAULT_WITHER=newInstance(getConstructor(getNMSClass("EntityWither"),getNMSClass("World")),new Object[]{null});
    private static final Method getMaxHealth=getMethod(getNMSClass("EntityLiving"),"getMaxHealth");
    private static final float MAX_HEALTH=invokeMethod(getMaxHealth,DEFAULT_WITHER);
    protected static void startPlayBossbar(Player p,MemorySection ms)
    {
        boolean show=false;
        if(isNullOrEmptyWithTrim(((String)ms.get("permission"))) || p.hasPermission((String)ms.get("permission")))
            show=true;
        if(show)
        {
            Reference<AtomicInteger> id=new Reference<>(null);
            Reference<Boolean> removed=new Reference<>(false);
            if(ms.get("text") instanceof String)
            {
                id.value= BossBar.sendBossbar(p, StringUtils.translateColorCodes(p,(String)ms.get("text")),MAX_HEALTH*Float.valueOf(ms.getString("progress")));
            }else{
                List<String> frames=(List<String>)ms.get("text");
                new Thread(()->{
                    try{
                        while(!removed.value)
                        {
                            Iterator<String> it=frames.iterator();
                            while(it.hasNext())
                            {
                                if(removed.value)
                                    break;
                                String s=it.next();
                                if(id.value==null)
                                {
                                    Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->
                                    {
                                        try{
                                            id.value=BossBar.sendBossbar(p, StringUtils.translateColorCodes(p,s),MAX_HEALTH*Float.valueOf(ms.getString("progress")));
                                        }catch(Throwable e){removed.value=true;}
                                    });
                                }else{
                                    Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->{
                                        try{
                                            BossBar.updateBossbarWithName(id.value,StringUtils.translateColorCodes(p,s));
                                        }catch(Throwable e){removed.value=true;}
                                    });
                                }
                                try{
                                    Thread.sleep(ms.getLong("time-per-frame"));
                                }catch(Throwable e){throw new RuntimeException(e);}
                            }
                        }
                    }catch(Throwable e){}
                }).start();
            }
            if(!Objects.equals(ms.getLong("render-time"),-1L))
            {
                Bukkit.getScheduler().runTaskLater(GrewEssentials.getInstance(),()->{
                    try {
                        removed.value=true;
                        BossBar.removeBossbar(id.value);
                    }catch(Throwable e){}
                }, ms.getLong("render-time"));
            }
        }
    }
}
