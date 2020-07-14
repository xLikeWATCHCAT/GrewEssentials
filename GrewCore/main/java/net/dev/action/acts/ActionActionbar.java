package net.dev.action.acts;

import net.dev.api.minecraft.*;
import org.bukkit.entity.*;

public class ActionActionbar extends AbstractAction {

    @Override
    public String getName() {
        return "actionbar";
    }

    @Override
    public void onExecute(Player player) {
        ActionBar.sendActionBar(player, getContent());
    }

}
