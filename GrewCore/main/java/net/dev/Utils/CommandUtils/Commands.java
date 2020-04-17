package net.dev.Utils.CommandUtils;

import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;

public class Commands {
    public static boolean dispatchCommand(CommandSender sender, String command) {
        try {
            if ((sender instanceof Player)) {
                PlayerCommandPreprocessEvent e = new PlayerCommandPreprocessEvent((Player) sender, "/" + command);
                Bukkit.getPluginManager().callEvent(e);
                if (e.isCancelled() || Strings.isBlank(e.getMessage()) || !e.getMessage().startsWith("/")) {
                    return false;
                }
                return Bukkit.dispatchCommand(e.getPlayer(), e.getMessage().substring(1));
            } else {
                ServerCommandEvent e = new ServerCommandEvent(sender, command);
                Bukkit.getPluginManager().callEvent(e);
                if (e.isCancelled() || Strings.isBlank(e.getCommand())) {
                    return false;
                }
                return Bukkit.dispatchCommand(e.getSender(), e.getCommand());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
