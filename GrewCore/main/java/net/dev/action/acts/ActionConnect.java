package net.dev.action.acts;

import net.dev.utils.*;
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
