package net.dev.Listeners;

import net.dev.API.Minecraft.*;
import net.dev.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.concurrent.atomic.*;

import static net.dev.Utils.StringUtils.StringUtils.*;

public class PlayerMoveListener implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled = false)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player p = event.getPlayer();
        try{
            BossBar.getIndexesAtomicByPlayer(p,i->{
                for(AtomicInteger a : i)
                {
                    Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->BossBar.updateBossbar(a));
                }
            });
        }catch (Throwable e){ }
    }
    @EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled = false)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        Player p = event.getPlayer();
        try{
            BossBar.getIndexesAtomicByPlayer(p,i->{
                for(AtomicInteger a : i)
                {
                    Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->BossBar.updateBossbar(a));
                }
            });
        }catch (Throwable e){ }
    }
    @EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled = false)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event)
    {
        Player p = event.getPlayer();
        String worldName = p.getWorld().getName();
        try{
            if(GrewEssentials.getInstance().bossbar.get("per-world")==null || isNullOrEmptyWithTrim(((MemorySection)GrewEssentials.getInstance().bossbar.get("per-world")).getString(worldName,"")) || GrewEssentials.getInstance().bossbar.get("bars."+((MemorySection)GrewEssentials.getInstance().bossbar.get("per-world")).getString(worldName,""),null)==null)
                BossBar.getIndexesAtomicByPlayer(p,i->{
                    for(AtomicInteger a : i)
                    {
                        Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->BossBar.updateBossbar(a));
                    }
                });
            else if(GrewEssentials.getInstance().bossbar.getBoolean("BossBar"))
            {
                MemorySection ms=(MemorySection)GrewEssentials.getInstance().bossbar.get("per-world");
                String bar=ms.getString(worldName);
                MemorySection ms2=(MemorySection) GrewEssentials.getInstance().bossbar.get("bars."+bar);
                BossBar.getIndexesAtomicByPlayer(p,i->{
                    for(AtomicInteger a : i)
                    {
                        Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->BossBar.removeBossbar(a));
                    }
                });
                new Thread(()->{
                    DedicatedMethods.startPlayBossbar(p,ms2);
                }).start();
            }
        }catch (Throwable e){}
    }
}
