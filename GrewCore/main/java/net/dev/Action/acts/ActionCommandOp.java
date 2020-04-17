package net.dev.Action.acts;

import net.dev.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class ActionCommandOp extends AbstractAction {
    @Override
    public String getName() {
        return "op";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.getScheduler().runTask(GrewEssentials.getPlugin(), () -> {
            boolean isOp = player.isOp();
            player.setOp(true);
            player.chat("/" + getContent());
            player.setOp(isOp);
        });
    }
}
