package net.dev.display;

import net.dev.Action.acts.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;

import java.util.*;

public class Icon {
    private int priority;
    private String requirement;
    private Item item;
    private HashMap<ClickType, List<AbstractAction>> actions;

    public Icon(int priority, String requirement, Item item, HashMap<ClickType, List<AbstractAction>> actions) {
        this.priority = priority;
        this.requirement = requirement;
        this.item = item;
        this.actions = actions;
    }

    public int getPriority() {
        return priority;
    }

    public String getRequirement() {
        return requirement;
    }

    public Item getItem() {
        return item;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public HashMap<ClickType, List<AbstractAction>> getActions() {
        return actions;
    }

}
