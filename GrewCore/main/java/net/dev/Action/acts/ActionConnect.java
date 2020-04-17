package net.dev.Action.acts;

import net.dev.Utils.*;
import org.bukkit.entity.*;

public class ActionConnect extends AbstractAction {
    @Override
    public String getName() {
        return "connect|bungee|server";
    }

    @Override
    public void onExecute(Player player) {
        Bungees.connect(player, getContent());
    }
}
