package net.dev.Action.acts;

import net.dev.Utils.*;
import org.bukkit.entity.*;

public class ActionCleanCatchers extends AbstractAction {

    @Override
    public String getName() {
        return "catcher(s)?-(clean|clear)|(clear|clean)-catcher(s)?";
    }

    @Override
    public void onExecute(Player player) {
        Catchers.getPlayerdata().remove(player.getName());
    }

}