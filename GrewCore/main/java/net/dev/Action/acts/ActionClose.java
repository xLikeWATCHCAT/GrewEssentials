package net.dev.Action.acts;

import net.dev.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class ActionClose extends AbstractAction {

    @Override
    public String getName() {
        return "close|shut";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.getScheduler().runTask(GrewEssentials.getPlugin(), player::closeInventory);
    }
}
