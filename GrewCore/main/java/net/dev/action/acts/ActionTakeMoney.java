package net.dev.action.acts;

import net.dev.api.plugins.*;
import org.apache.commons.lang.math.*;
import org.bukkit.entity.*;

public class ActionTakeMoney extends AbstractAction {

    @Override
    public String getName() {
        return "(take|remove|withdraw)(-)?(money|eco|coin)(s)?";
    }

    @Override
    public void onExecute(Player player) {
        double value = NumberUtils.toDouble(getContent(), -1);
        if (value > 0) {
            EconomyHook.remove(player, value);
        }
    }

}
