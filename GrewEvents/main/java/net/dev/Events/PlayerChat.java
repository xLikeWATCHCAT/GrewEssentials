package net.dev.Events;

import org.bukkit.entity.*;
import org.bukkit.event.player.*;

public class PlayerChat {
    public static String getPlayerChatMessage(PlayerChatEvent event){
        return event.getMessage();
    }
    public static void stopPlayerChat(PlayerChatEvent event){event.setCancelled(true);}
    public static void allowPlayerChat(PlayerChatEvent evnet){evnet.setCancelled(false);}
}
