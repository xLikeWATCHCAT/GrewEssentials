package net.dev.Listeners;

import net.dev.API.Minecraft.*;
import net.dev.Database.*;
import net.dev.*;
import net.dev.Utils.DatabaseUtils.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import static net.dev.Utils.PlayerUtils.FlyUtils.FlyUtils.*;
import static net.dev.Utils.PlayerUtils.VanishUtils.VanishUtils.*;
import static net.dev.Utils.StringUtils.StringUtils.*;

public class PlayerJoinListener implements Listener {
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) throws Throwable {
        Player p = event.getPlayer();
        UUID u =p.getUniqueId();
        try{
            new Thread(()->{
                if(GrewEssentials.getInstance().Message.getBoolean("Fly.Fly_Allow")){
                    if(isFlight(u)){
                        if(GrewEssentials.getInstance().Message.getBoolean("Fly.Fly_Permission")){
                            if (p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Fly.Self")) || p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                                try {
                                    enableFlight(p);
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        }else{
                            try {
                                enableFlight(p);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }catch (Throwable e){ }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        UUID u = p.getUniqueId();
        try{
            if(GrewEssentials.getInstance().Message.getBoolean("Vanish.Enable")){
                if(isVanished(u)){
                    event.setJoinMessage("§f");
                    new Thread(()->{
                        vanishedset(event);
                    }).start();
                }
            }else{
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
            new Thread(()->{
                String name= LoadDatabase.db.dbSelectFirst("PlayerInfo","name",new KeyValue(){{ this.add("uuid",u.toString()); }});
                String ip= LoadDatabase.db.dbSelectFirst("PlayerInfo","ip",new KeyValue(){{ this.add("uuid",u.toString()); }});
                if(name == null || ip == null){
                    LoadDatabase.db.dbInsert("PlayerInfo",new KeyValue(){{
                        this.add("uuid",u.toString());
                        this.add("name",p.getName());
                        this.add("ip",p.getAddress().getHostName());
                    }});
                }else{
                    LoadDatabase.db.dbUpdate("PlayerInfo",new KeyValue(){{ this.add("name",p.getName()); }},new KeyValue(){{ this.add("uuid",u.toString()); }});
                    LoadDatabase.db.dbUpdate("PlayerInfo",new KeyValue(){{ this.add("ip",p.getAddress().getHostName()); }},new KeyValue(){{ this.add("uuid",u.toString()); }});
                }
            }).start();
            String worldName = p.getWorld().getName();
            new Thread(()->{
                if(GrewEssentials.getInstance().bossbar.getBoolean("BossBar"))
                {
                    try{
                        if(GrewEssentials.getInstance().bossbar.get("per-world")==null || isNullOrEmptyWithTrim(((MemorySection)GrewEssentials.getInstance().bossbar.get("per-world")).getString(worldName,"")) || GrewEssentials.getInstance().bossbar.get("bars."+((MemorySection)GrewEssentials.getInstance().bossbar.get("per-world")).getString(worldName,""),null)==null)
                        {
                            Object b=GrewEssentials.getInstance().bossbar.get("bars."+GrewEssentials.getInstance().bossbar.getString("default-bars",""),null);
                            if(b!=null)
                                new Thread(()->{
                                    DedicatedMethods.startPlayBossbar(p,(MemorySection)b);
                                }).start();
                        }else{
                            MemorySection ms=(MemorySection)GrewEssentials.getInstance().bossbar.get("per-world");
                            String bar=ms.getString(worldName);
                            MemorySection ms2=(MemorySection) GrewEssentials.getInstance().bossbar.get("bars."+bar);
                            new Thread(()->{
                            BossBar.getIndexesAtomicByPlayer(p, i->{
                                for(AtomicInteger a : i)
                                {
                                    Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->BossBar.removeBossbar(a));
                                }
                            });
                            }).start();
                            new Thread(()->{
                                DedicatedMethods.startPlayBossbar(p,ms2);
                            }).start();
                        }
                    }catch (Throwable e){ }
                }
            }).start();
        }catch (Throwable e){ }
    }
}
