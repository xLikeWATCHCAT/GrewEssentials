package net.dev.action.methods;

import net.dev.*;
import net.dev.utils.command.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class CommandOpAction {
    public static void command(Player p, String cmd){
        boolean isOp = p.isOp();
        p.setOp(true);
        Bukkit.getScheduler().runTask(GrewEssentials.getPlugin(), () -> Commands.dispatchCommand(p, cmd));
        p.setOp(isOp);
    }
}
