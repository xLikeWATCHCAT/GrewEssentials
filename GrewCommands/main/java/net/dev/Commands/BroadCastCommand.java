package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class BroadCastCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("BroadCast.Enable")){
                if (sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.BroadCast")) || sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                    StringBuilder bc = new StringBuilder();
                    if (args.length > 0) {
                        for(int i = 0; i < args.length; i++){
                            bc.append(args[i]);
                            if(i != args.length-1)
                            Utils.BroadCast("&E"+String.valueOf(bc).replace("$prefix", StringUtils.Prefix));
                            LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("BroadCast").replace("$player",sender.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip", ((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp()))));
                        }
                    } else {
                        sender.sendMessage(StringUtils.getCommandInfo("broadcast"));
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
