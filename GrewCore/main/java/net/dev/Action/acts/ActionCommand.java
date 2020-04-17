package net.dev.Action.acts;

import net.dev.*;
import net.dev.Utils.CommandUtils.*;
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
