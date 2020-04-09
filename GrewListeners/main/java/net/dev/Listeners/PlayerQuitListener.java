package net.dev.Listeners;

import net.dev.API.Minecraft.*;
import net.dev.Database.*;
import net.dev.Utils.DatabaseUtils.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import static net.dev.Utils.PlayerUtils.VanishUtils.VanishUtils.*;

public class PlayerQuitListener implements Listener {
    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerQuitRealTime(PlayerQuitEvent event)
    {
        Player p = event.getPlayer();
        UUID u = p.getUniqueId();
        if(isVanished(u))
            event.setQuitMessage("");
        BossBar.getIndexesAtomicByPlayer(p,i->{
            for(AtomicInteger i2 : i)
            {
                BossBar.removeBossbar(i2);
            }
        });
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        UUID u =p.getUniqueId();
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
    }
}
