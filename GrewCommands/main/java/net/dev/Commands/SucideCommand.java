package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class SucideCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("Sucide.Enable")){
            if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Sucide"))){
                if(sender instanceof Player){
                    if(GrewEssentials.getInstance().Message.getBoolean("Sucide.BroadCast")){
                        Utils.BroadCast(GrewEssentials.getInstance().Message.getString("Sucide.BroadCastMessage").replace("$prefix",StringUtils.Prefix).replace("$playername",sender.getName()));
                    }
                    Player p = (Player) sender;
                    p.setHealth(0);
                    PlayerUtil.sendMessage(p,GrewEssentials.getInstance().Message.getString("Sucide.Message"));
                    LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Sucide").replace("$player",sender.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip", ((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp()))));
                }
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
}
