package net.dev.Action.Methods;

import net.dev.*;
import net.dev.Utils.CommandUtils.*;
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
