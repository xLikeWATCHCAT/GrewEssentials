package net.dev.action.acts;

import net.dev.data.*;
import org.bukkit.entity.*;

public class ActionSetArgs extends AbstractAction {

    @Override
    public String getName() {
        return "set(-)?args";
    }

    @Override
    public void onExecute(Player player) {
        ArgsCache.updateArgs(player.getUniqueId(), getContent().split(" "));
    }
}
