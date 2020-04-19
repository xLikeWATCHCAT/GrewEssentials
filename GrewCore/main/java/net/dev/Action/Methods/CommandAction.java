package net.dev.Action.Methods;

import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class CommandAction {
    public static void command(Player p,String cmd){
        Bukkit.getScheduler().runTask(GrewEssentials.getPlugin(), () -> Commands.dispatchCommand(p, cmd));
    }
}
