package net.dev.Action.acts;

import net.dev.JavaScript.*;
import org.bukkit.entity.*;

public class ActionJs extends AbstractAction {

    @Override
    public String getName() {
        return "js|javascript";
    }

    @Override
    public void onExecute(Player player) {
        JavaScript.run(player, getContent(), getContent().contains("$input"));
    }
}
