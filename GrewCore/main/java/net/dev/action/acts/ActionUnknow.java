package net.dev.action.acts;

import org.bukkit.entity.*;

public class ActionUnknow extends AbstractAction {

    @Override
    public String getName() {
        return "unknown";
    }

    @Override
    public void onExecute(Player player) {
        player.sendMessage("§cUNKNOW | §7Input action: §f" + getContent());
    }
}
