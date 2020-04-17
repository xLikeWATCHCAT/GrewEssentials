package net.dev.JavaScript;

import org.apache.logging.log4j.core.helpers.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;

import javax.script.*;

public class JavaScript {
    private static SimpleBindings bindings = new SimpleBindings();

    static {
        bindings.put("bukkitServer", Bukkit.getServer());
    }

    public static Object run(Player player, String script) {
        return run(player, script, null, false);
    }

    public static Object run(Player player, String script, boolean silent) {
        return run(player, script, null, silent);
    }

    public static Object run(Player player, String script, InventoryClickEvent event) {
        return run(player, script, event, false);
    }

    public static Object run(Player player, String script, InventoryClickEvent event, boolean silent) {
        if (Strings.isEmpty(script) || "null".equalsIgnoreCase(script)) {
            return true;
        } else if (script.matches("true|false")) {
            return Boolean.parseBoolean(script);
        } else if (script.matches("(?i)no|yes")) {
            return !"no".equalsIgnoreCase(script);
        }
        bindings.put("player", player);
        if (event != null) {
            if (event instanceof InventoryClickEvent) {
                bindings.put("clickEvent", event);
                bindings.put("clickType", event.getClick());
                bindings.put("clickItemStack", event.getClickedInventory().getItem(event.getRawSlot()));
            }
        }

        try {
            return Scripts.compile(script).eval(bindings);
        } catch (Throwable e) {
            return false;
        }
    }

    public static SimpleBindings getBindings() {
        return bindings;
    }
}
