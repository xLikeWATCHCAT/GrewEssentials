package net.dev.action.methods;

import net.dev.*;
import net.dev.utils.command.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class CommandAction {
    public static void command(Player p,String cmd){
        Bukkit.getScheduler().runTask(GrewEssentials.getPlugin(), () -> Commands.dispatchCommand(p, cmd));
    }
}
