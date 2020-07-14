package net.dev.action.acts;

import net.dev.utils.*;
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