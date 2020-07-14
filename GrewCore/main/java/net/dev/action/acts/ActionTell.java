package net.dev.action.acts;

import org.bukkit.entity.*;

public class ActionTell extends AbstractAction {

    @Override
    public String getName() {
        return "talk|message|send|tell";
    }

    @Override
    public void onExecute(Player player) {
        player.sendMessage(getContent());
    }

}
