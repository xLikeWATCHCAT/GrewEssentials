package net.dev.action.acts;

import net.dev.api.plugins.*;
import org.apache.commons.lang.math.*;
import org.bukkit.entity.*;

public class ActionGiveMoney extends AbstractAction {

    private double value;

    @Override
    public String getName() {
        return "(give|add|deposit)(-)?(money|eco|coin)(s)?";
    }

    @Override
    public void onExecute(Player player) {
        double value = NumberUtils.toDouble(getContent(), -1);
        if (value > 0) {
            EconomyHook.add(player, value);
        }
    }
}
