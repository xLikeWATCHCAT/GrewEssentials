package net.dev.Commands.Warp;

import net.dev.File.struct.*;
import net.dev.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class SetWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(GrewEssentials.getInstance().Message.getBoolean("Warp.Enable")){
            if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Warp.Admin"))){
                if(sender instanceof Player){
                    if(args.length >= 1){
                        String warpName = args[0];
                        Player p = (Player) sender;
                        try{
                            GrewEssentials.getInstance().data.getConfig().warps.put(new WarpConfig.Text(warpName),WarpConfig.WarpPoint.fromLocation(p.getLocation()));
                            GrewEssentials.getInstance().data.saveConfig();
                            sender.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("SetWarp.Success")).replace("$prefix",StringUtils.Prefix).replace("$warpname",warpName));
                        }catch (Throwable e){
                            //e.printStackTrace();
                            sender.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("SetWarp.Failed")).replace("$prefix",StringUtils.Prefix).replace("$warpname",warpName));
                        }
                    }else{
                        sender.sendMessage(StringUtils.getCommandInfo("setwarp"));
                    }
                }else{
                    sender.sendMessage(StringUtils.OnlyPlayer);
                }
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else {
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
}
