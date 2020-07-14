package net.dev.action.acts;

import net.dev.javascript.*;
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
