package net.dev.utils.player.vanish;

import net.dev.commands.*;
import net.dev.database.*;
import net.dev.utils.database.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;

import java.util.*;

public class VanishUtils {
    public static Vector<Player> VanishPlayerInThisServerList=new Vector<>();
    public static void vanishedset(PlayerJoinEvent event){
        Player p = event.getPlayer();
        UUID u = p.getUniqueId();
        if(isVanished(u)) {
            VanishCommand.setVanished(event.getPlayer(), true,false,false);
           }else{
            VanishCommand.setVanished(event.getPlayer(), false,false,false);
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
