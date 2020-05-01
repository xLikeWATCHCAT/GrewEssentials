package net.dev.Commands.Warp;

import net.dev.File.struct.*;
import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;

import java.util.*;
import java.util.stream.*;

public class RemoveWarpCommand implements CommandWithCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(GrewEssentials.getInstance().Message.getBoolean("Warp.Enable")){
            if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Warp.Admin"))){
                if(args.length >= 1){
                    String warpName = args[0];
                    if(!GrewEssentials.getInstance().data.getConfig().warps.containsKey(new WarpConfig.Text(warpName)))
                    {
                        PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("RemoveWarp.Failed").replace("$warpname",warpName));
                        return true;
                    }
                    try{
                        GrewEssentials.getInstance().data.getConfig().warps.remove(new WarpConfig.Text(warpName));
                        GrewEssentials.getInstance().data.saveConfig();
                        PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("RemoveWarp.Success").replace("$warpname",warpName));
                    }catch (Throwable e){
                        PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("RemoveWarp.Failed").replace("$warpname",warpName));
                    }
                }else{
                    sender.sendMessage(StringUtils.getCommandInfo("removewarp"));
                }
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else {
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length==1)
            return WarpCommand.textListToStringList(GrewEssentials.getInstance().data.getConfig().warps.keySet().parallelStream().filter(i->i.getText().toLowerCase(Locale.ENGLISH).startsWith(args[0].toLowerCase(Locale.ENGLISH))).collect(Collectors.toList()));
        else return ListUtil.toList();
    }
}
