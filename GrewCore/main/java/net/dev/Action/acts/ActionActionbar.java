package net.dev.Action.acts;

import net.dev.API.Minecraft.*;
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
