package net.dev.action.acts;

import net.dev.*;
import net.dev.utils.command.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class ActionCommand extends AbstractAction {

    @Override
    public String getName() {
        return "player|command|execute";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.getScheduler().runTask(GrewEssentials.getPlugin(), () -> Commands.dispatchCommand(player, getContent()));
    }
}
